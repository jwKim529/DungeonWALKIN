package com.example.dungeonwalkin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "StepPerDay";
    public static final String _ID = "_id";
    public static final String DATE = "time";
    public static final String STEPS = "steps";
    public DBOpenHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override // 부모 메소드 재정의
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE + " TEXT,"
                + STEPS + " INTEGER);" );
    }
    @Override // 부모 메소드 재정의
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}