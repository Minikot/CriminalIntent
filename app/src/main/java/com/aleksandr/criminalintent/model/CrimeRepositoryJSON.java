package com.aleksandr.criminalintent.model;

import android.content.Context;

import com.aleksandr.criminalintent.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Aleksandr on 04.12.16.
 */

public class CrimeRepositoryJSON implements CrimeDataAccess {

    private static CrimeRepositoryJSON crimeRepositoryJSON;
    private Context context;
    private Gson gson = new Gson();
    private Type crimesType = new TypeToken<ArrayList<Crime>>(){
    }.getType();


    private CrimeRepositoryJSON(Context context) {
        this.context = context;
    }

    public static CrimeRepositoryJSON getInstance(Context context) {
        if (crimeRepositoryJSON == null) {
            crimeRepositoryJSON = new CrimeRepositoryJSON(context);
        }
        return crimeRepositoryJSON;
    }


    @Override
    public ArrayList<Crime> getCrimes() {
        String serCrimes = CommonUtils.loadCrimes(context);
        ArrayList<Crime> crimes = gson.fromJson(serCrimes, crimesType);
        if (crimes == null) return new ArrayList<Crime>();
        return crimes;
    }

    @Override
    public Crime getCrime(UUID uuid) {
        ArrayList<Crime> crimes = getCrimes();
        for (Crime crime : crimes) {
            if (crime.getUuid().equals(uuid))
                return crime;
        }
        return null;
    }

    @Override
    public void addCrime(Crime crime) {
        ArrayList<Crime> crimes = getCrimes();
        crimes.add(crime);
        setCrimes(crimes);
    }

    @Override
    public boolean updateCrime(Crime crime) {
        ArrayList<Crime> crimes = getCrimes();
        for (int i = 0, total = crimes.size(); i < total; i++) {
            if (crime.getUuid().equals(crimes.get(i).getUuid())){
                crimes.set(i, crime);
                setCrimes(crimes);
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteCrime(UUID uuid) {

    }

    public void setCrimes(ArrayList<Crime> crimes){
        String serCrimes = gson.toJson(crimes, crimesType);
        CommonUtils.saveCrimes(context,serCrimes);
    }
}
