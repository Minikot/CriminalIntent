package com.aleksandr.criminalintent.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aleksandr on 15.05.17.
 */

public class CrimeDbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "crime.sqlite";

    public CrimeDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CrimeTable.NAME
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, "
                + CrimeTable.Cols.UUID + " TEXT NOT NULL, "
                + CrimeTable.Cols.TITLE + " TEXT, "
                + CrimeTable.Cols.DATE + " INTEGER NOT NULL, "
                + CrimeTable.Cols.SOLVED + " INTEGER NOT NULL,"
                + CrimeTable.Cols.SUSPECT + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CrimeTable.NAME);
        onCreate(db);
    }
}
