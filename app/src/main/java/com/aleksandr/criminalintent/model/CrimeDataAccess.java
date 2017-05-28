package com.aleksandr.criminalintent.model;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Aleksandr on 13.05.17.
 */

public interface CrimeDataAccess {
    @Nullable ArrayList<Crime> getCrimes();

    Crime getCrime(UUID uuid);

    void addCrime(Crime crime);

    boolean updateCrime(Crime crime);

    void deleteCrime(UUID uuid);
}
