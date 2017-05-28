package com.aleksandr.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Aleksandr on 04.12.16.
 */

public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
