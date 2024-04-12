package com.example.dungeonwalkin;

import android.app.Application;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import static com.example.dungeonwalkin.DBOpenHelper.TABLE_NAME;
import static com.example.dungeonwalkin.DBOpenHelper._ID;
import static com.example.dungeonwalkin.DBOpenHelper.DATE;
import static com.example.dungeonwalkin.DBOpenHelper.STEPS;

public class DWApp extends Application {
    private static final String DB_NAME = "DW_DB";
    private static final int DB_VERSION = 1;
    private final DBOpenHelper openHelper = new DBOpenHelper(this,DB_NAME,null, DB_VERSION);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("DWApp","Application onCreate() called");
    }

    public void writeDB(int steps, String date) {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(STEPS, steps);
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    public void readDB() {
    }
}
