package com.example.dungeonwalkin;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import static com.example.dungeonwalkin.StepsDBOpenHelper.TABLE_NAME;
import static com.example.dungeonwalkin.StepsDBOpenHelper.START_DATE;
import static com.example.dungeonwalkin.StepsDBOpenHelper.END_DATE;
import static com.example.dungeonwalkin.StepsDBOpenHelper.STEPS;
import static com.example.dungeonwalkin.StepsDBOpenHelper.DATE;
import static com.example.dungeonwalkin.StepsDBOpenHelper.TOTALSTEP;

public class DWApp extends Application {
    private final String TAG = "DWApp";
    private static final String DB_NAME = "DW_DB";
    private static final int DB_VERSION = 1;
    private final StepsDBOpenHelper stepsOpenHelper = new StepsDBOpenHelper(this,DB_NAME,null, DB_VERSION);
    private CurrentStep currentStep = new CurrentStep(0,0,0);

    private static final String PREFERENCE_NAME = "DWPreference";
    private static final String STEP_KEY = "CurrentStep";
    private static final String STARTDATE_KEY = "StartDate";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Application onCreate() called");
        loadPreference();
        Log.i(TAG,"Preference Loaded");
        clearPreference();
        Log.i(TAG,"Preference Cleared");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG,"Low Memory... Do Something!");
    }

    public void writeCurrentStepsInDB(CurrentStep getCurrent) {
        SQLiteDatabase db = stepsOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(START_DATE, getCurrent.getStartTime());
        values.put(END_DATE, getCurrent.getEndTime());
        values.put(STEPS, getCurrent.getSteps());
        db.insertOrThrow(TABLE_NAME, null, values);
        setCurrentStep(getCurrent);
    }

    public Cursor readAllStepsPerDateInDB() {
        SQLiteDatabase db = stepsOpenHelper.getReadableDatabase();
        String[] from = { START_DATE, END_DATE, STEPS, };
        String myquery = "SELECT strftime('%Y-%m-%d', datetime( "
                + START_DATE + " / 1000, 'unixepoch')) AS " + DATE + ", "
                + "SUM( " + STEPS + " ) AS " + TOTALSTEP
                + " FROM " + TABLE_NAME + " GROUP BY " + DATE;
        Cursor cursor = db.rawQuery(myquery,null);
        return cursor;
    }

    public void setCurrentStep(CurrentStep step) {
        this.currentStep = step;
    }

    public CurrentStep getCurrentStep(){
        return this.currentStep;
    }

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
