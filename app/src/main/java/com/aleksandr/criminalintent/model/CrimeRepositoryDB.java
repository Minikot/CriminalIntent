package com.aleksandr.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Aleksandr on 19.05.17.
 */

public class CrimeRepositoryDB implements CrimeDataAccess {

    private static CrimeRepositoryDB crimeRepositoryDB;
    private Context context;
    private CrimeDbHelper dbHelper;
    private SQLiteDatabase database;

    private CrimeRepositoryDB(Context context) {
        this.context = context;
        this.dbHelper = new CrimeDbHelper(context.getApplicationContext());
    }

    public static CrimeRepositoryDB getInstance(Context context) {
        if (crimeRepositoryDB == null) {
            crimeRepositoryDB = new CrimeRepositoryDB(context);
        }
        return crimeRepositoryDB;
    }

    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        database.close();
    }

    @Nullable
    @Override
    public ArrayList<Crime> getCrimes() {
        ArrayList<Crime> crimes = new ArrayList<>();

        open();

        Cursor cursor = null;
        try {
            cursor =
                    database.query(
                            CrimeTable.NAME, // Table name
                            null, //null = all columns or String[]{col1, col2, ...}
                            null, // if null all will be selected // == SELECT * FROM table
                            null, // where arguments
                            null, // group by / null = not used
                            null, // having by / null = not used
                            null); // order by / null = not used

            if (cursor.moveToFirst()){
                do{
                    Crime crime = getCrimeFromCursor(cursor);
                    crimes.add(crime);
                }while (cursor.moveToNext());
            }
            return crimes;
        } finally {
            cursor.close();
            close();
        }
    }

    @Override
    public Crime getCrime(UUID uuid) {
        open();

        Cursor cursor = null;
        try {
            cursor =
                    database.query(
                            CrimeTable.NAME, // Table name
                            null, //null = all columns or String[]{col1, col2, ...}
                            CrimeTable.Cols.UUID + " =? ", // where clause
                            new String[]{uuid.toString()}, // where arguments
                            null, //group by / null = not used
                            null, // having by / null = not used
                            null); // order by / null = not used

            if (cursor.getCount() == 0) return null;
            cursor.moveToFirst();
            return getCrimeFromCursor(cursor);
        } finally {
            cursor.close();
            close();
        }
    }

    @Override
    public void addCrime(Crime crime) {
        open();
        ContentValues contentValues = getContentValues(crime);
        database.insert(CrimeTable.NAME, null, contentValues); //Добавили строку в БД
        close();
    }

    @Override
    public boolean updateCrime(Crime crime) {
        boolean result = false;
        open();
        String sUUID = crime.getUuid().toString();
        ContentValues contentValues = getContentValues(crime);
        int rows = database.update(CrimeTable.NAME, contentValues, CrimeTable.Cols.UUID + " =?", new String[]{sUUID});
        if (rows > 0) result = true;
        close();
        return result;
    }

    @Override
    public void deleteCrime(UUID uuid) {
        open();
        String sUUID = uuid.toString();
        database.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " =?", new String[]{sUUID});
        close();
    }

    private ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getUuid().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return contentValues;
    }

    private Crime getCrimeFromCursor (Cursor cursor){
        UUID uuid = UUID.fromString(cursor.getString(1));
        Crime crime = new Crime(uuid);
        crime.setTitle(cursor.getString(2));
        crime.setDate(new Date(cursor.getLong(3)));
        crime.setSolved(cursor.getInt(4) == 1);
        return crime;
    }
}
