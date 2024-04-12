package com.example.dungeonwalkin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StatusDBOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "PlayerStatus";
    public static final String _ID = "_id";
    public static final String ATTACK = "attack";
    public static final String DEFENCE = "defence";
    public static final String LIFE = "LIFE";


    public StatusDBOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATTACK + " INTEGER, "
                + DEFENCE + " INTEGER, "
                + LIFE + " INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
