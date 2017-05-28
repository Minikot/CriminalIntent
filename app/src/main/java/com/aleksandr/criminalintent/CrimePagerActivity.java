package com.aleksandr.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aleksandr.criminalintent.model.Crime;
import com.aleksandr.criminalintent.model.CrimeDataAccess;
import com.aleksandr.criminalintent.model.CrimeRepository;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Aleksandr on 22.04.17.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager vpCrimes;
    private ArrayList<Crime> crimes = new ArrayList<>();

    public static Intent newIntent (Context context, UUID uuid){
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_UUID, uuid);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        vpCrimes = (ViewPager) findViewById(R.id.vp_crimes);

        CrimeDataAccess crimeDataAccess = CrimeRepository.getCrimeDataAccess(
                getApplicationContext());
        crimes = crimeDataAccess.getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        vpCrimes.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = crimes.get(position);
                UUID uuid = crime.getUuid();
                return CrimeFragment.newInstance(uuid);
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });

        UUID uuid = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_UUID);
        for (int i = 0, total = crimes.size(); i < total; i++) {
            Crime crime = crimes.get(i);
            if (crime.getUuid().equals(uuid)) {
                vpCrimes.setCurrentItem(i);
                break;
            }
        }

    }
}
