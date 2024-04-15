package com.example.dungeonwalkin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatusDBOpenHelper extends SQLiteOpenHelper {

    public static final String STATUS_TABLE_NAME = "status";
    public static final String _ID = "_id";
    public static final String OBJNAME = "name";
    public static final String LEVEL = "level";
    public static final String EXP = "exp";
    public static final String ATTACK = "attack";
    public static final String DEFENCE = "defence";
    public static final String LIFE = "life";
    public static final String GOLD = "gold";
    public static final String CLEARED = "cleared";
    public static final String SAVEDATE = "savetime";



    public StatusDBOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + STATUS_TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        OBJNAME + " TEXT, " +
                        LEVEL + " INTEGER, " +
                        EXP + " INTEGER, " +
                        ATTACK + " INTEGER, " +
                        DEFENCE + " INTEGER, " +
                        LIFE + " INTEGER, " +
                        GOLD + " INTEGER, " +
                        CLEARED + " INTEGER, " +
                        SAVEDATE + " INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STATUS_TABLE_NAME);
        onCreate(db);
    }
}
