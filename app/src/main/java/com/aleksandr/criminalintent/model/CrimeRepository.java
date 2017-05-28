package com.aleksandr.criminalintent.model;

import android.content.Context;

/**
 * Created by Aleksandr on 13.05.17.
 */

public class CrimeRepository {

    public static final Source CURRENT_SOURCE = Source.DB;

    public enum Source{
        JSON, DB
    }

    public static CrimeDataAccess getCrimeDataAccess(Context context){
        switch (CURRENT_SOURCE){
            default:
            case JSON:
                return CrimeRepositoryJSON.getInstance(context);
            case DB:
                return CrimeRepositoryDB.getInstance(context);
        }

    }

}
