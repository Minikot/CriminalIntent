package com.aleksandr.criminalintent;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Aleksandr on 23.02.17.
 */

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID uuid = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_UUID);

        return CrimeFragment.newInstance(uuid);
    }
}
