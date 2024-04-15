package com.example.dungeonwalkin;

import static com.example.dungeonwalkin.StatusDBOpenHelper.ATTACK;
import static com.example.dungeonwalkin.StatusDBOpenHelper.CLEARED;
import static com.example.dungeonwalkin.StatusDBOpenHelper.DEFENCE;
import static com.example.dungeonwalkin.StatusDBOpenHelper.EXP;
import static com.example.dungeonwalkin.StatusDBOpenHelper.GOLD;
import static com.example.dungeonwalkin.StatusDBOpenHelper.LEVEL;
import static com.example.dungeonwalkin.StatusDBOpenHelper.LIFE;
import static com.example.dungeonwalkin.StatusDBOpenHelper.OBJNAME;
import static com.example.dungeonwalkin.StatusDBOpenHelper.SAVEDATE;
import static com.example.dungeonwalkin.StatusDBOpenHelper.STATUS_TABLE_NAME;
import static com.example.dungeonwalkin.StepsDBOpenHelper.DATE;
import static com.example.dungeonwalkin.StepsDBOpenHelper.END_DATE;
import static com.example.dungeonwalkin.StepsDBOpenHelper.START_DATE;
import static com.example.dungeonwalkin.StepsDBOpenHelper.STEPS;
import static com.example.dungeonwalkin.StepsDBOpenHelper.STEP_TABLE_NAME;
import static com.example.dungeonwalkin.StepsDBOpenHelper.TOTALSTEP;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DWApp extends Application {
    private final String TAG = "DWApp";

    public static final int REQUEST_CODE = 123;
    public static final int RESULT_CODE = 123;

    private static final String STEP_DB_NAME = "DW_STEP_DB";
    private static final String STATUS_DB_NAME = "DW_STATUS_DB";
    private static final int DB_VERSION = 3;

    //만보기DB
    private final StepsDBOpenHelper stepsOpenHelper = new StepsDBOpenHelper(this,STEP_DB_NAME,null, DB_VERSION);
    private CurrentStep currentStep = new CurrentStep(0,0,0);

    private static final String PREFERENCE_NAME = "DWPreference";
    private static final String STEP_KEY = "CurrentStep";
    private static final String STARTDATE_KEY = "StartDate";

    //플레이어DB
    private final StatusDBOpenHelper statusOpenHelper = new StatusDBOpenHelper(this,STATUS_DB_NAME, null, DB_VERSION);
    private ActiveObject playerData = new ActiveObject("player");

    //현재 던전 데이터
    private Dungeon currentDungeon;
    private int currentPlayerLocation=0;
    private int currentDead = 0;
    private boolean currentCleared = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Application onCreate() called");
        loadPreference();
        Log.i(TAG,"Preference Loaded");
        clearPreference();
        Log.i(TAG,"Preference Cleared");
        SQLiteDatabase stepDB = stepsOpenHelper.getWritableDatabase();
        SQLiteDatabase statusDB = statusOpenHelper.getWritableDatabase();
        stepDB.close();
        statusDB.close();
        Log.i(TAG,"DB Exist Checked");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG,"Low Memory... Do Something!");
    }

    // 걸음 수 저장
    public void writeCurrentStepsInDB(CurrentStep getCurrent) {
        SQLiteDatabase db = stepsOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(START_DATE, getCurrent.getStartTime());
        values.put(END_DATE, getCurrent.getEndTime());
        values.put(STEPS, getCurrent.getSteps());
        db.insertOrThrow(STEP_TABLE_NAME, null, values);
        setCurrentStep(getCurrent);
        db.close();
    }

    // 걸음 수 불러오기
    public Cursor readAllStepsPerDateInDB() {
        SQLiteDatabase db = stepsOpenHelper.getReadableDatabase();
        //날짜별 걸음수 합계(시작일기준) 출력
        String myquery = "SELECT strftime('%Y-%m-%d', datetime( "
                + START_DATE + " / 1000, 'unixepoch')) AS " + DATE + ", "
                + "SUM( " + STEPS + " ) AS " + TOTALSTEP
                + " FROM " + STEP_TABLE_NAME + " GROUP BY " + DATE;
        return db.rawQuery(myquery,null);
    }

    // 내정보 저장
    public void writePlayerStatusInDB(ActiveObject getPlayerData) {
        SQLiteDatabase db = statusOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OBJNAME,getPlayerData.getObjectName());
        values.put(LEVEL,getPlayerData.getCurrentLevel());
        values.put(EXP,getPlayerData.getMyExperience());
        values.put(ATTACK,getPlayerData.getCurrentAttack());
        values.put(DEFENCE,getPlayerData.getCurrentDefence());
        values.put(LIFE,getPlayerData.getCurrentLife());
        values.put(GOLD,getPlayerData.getCurrentGold());
        values.put(CLEARED,getPlayerData.getClearedHighestDungeon());
        values.put(SAVEDATE,System.currentTimeMillis());
        db.insertOrThrow(STATUS_TABLE_NAME, null, values);
        setPlayerData(getPlayerData);
    }

    // 내정보 불러오기
    public ActiveObject readPlayerStatusInDB() {
        SQLiteDatabase db = statusOpenHelper.getReadableDatabase();
        String myquery = "SELECT * FROM " + STATUS_TABLE_NAME
                + " ORDER BY " + SAVEDATE
                +" DESC LIMIT 1;";
        Cursor cursor = db.rawQuery(myquery,null);
        Log.i(TAG, "Cursor NUM : "+cursor.getCount());
        if(cursor != null && cursor.getCount() == 0 ){
            playerData = new ActiveObject("player");
        }else {
            cursor.moveToNext();
            this.playerData = new ActiveObject(cursor.getString(1));
            playerData.setCurrentLevel(cursor.getInt(2));
            playerData.setMyExperience(cursor.getInt(3));
            playerData.setCurrentAttack(cursor.getInt(4));
            playerData.setCurrentDefence(cursor.getInt(5));
            playerData.setCurrentLife(cursor.getInt(6));
            playerData.addCurrentGold(cursor.getInt(7));
            playerData.setClearedHighestDungeon(cursor.getInt(8));
        }

        return playerData;
    }

    public void setCurrentDungeon(String name) {
        Log.i(TAG,"CreateDungeon : LV "+playerData.getClearedHighestDungeon()+1);
        this.currentDungeon = new Dungeon(name, playerData.getClearedHighestDungeon()+1);
    }

    public Dungeon getCurrentDungeon () {
        return currentDungeon;
    }

    public int getCurrentPlayerLocation() {
        return currentPlayerLocation;
    }

    public void setCurrentPlayerLocation(int currentPlayerLocation) {
        this.currentPlayerLocation = currentPlayerLocation;
    }

    public void setCurrentStep(CurrentStep step) {
        this.currentStep = step;
    }

    public CurrentStep getCurrentStep(){
        return this.currentStep;
    }

    public ActiveObject getPlayerData() {
        return playerData;
    }

    public void setPlayerData(ActiveObject playerData) {
        this.playerData = playerData;
    }

    public int getCurrentDead() {
        return currentDead;
    }

    public void setCurrentDead(int currentDead) {
        this.currentDead = currentDead;
    }

    public boolean isCurrentCleared() {
        return currentCleared;
    }

    public void setCurrentCleared(boolean currentCleared) {
        this.currentCleared = currentCleared;
    }

    //preference 관련 메소드(3가지)
    private void clearPreference() {
        SharedPreferences preferences =
                getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    private void loadPreference() {
        SharedPreferences preferences =
                getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        currentStep.setStartTime(preferences.getLong(STARTDATE_KEY,0));
        currentStep.setSteps(preferences.getInt(STEP_KEY,0));
    }

    public void savePreference(CurrentStep steps) {
        SharedPreferences preferences =
                getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(STEP_KEY, steps.getSteps());
        editor.putLong(STARTDATE_KEY, steps.getStartTime());
        editor.apply();
    }
}
