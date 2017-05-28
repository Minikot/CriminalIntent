package com.aleksandr.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aleksandr.criminalintent.model.Crime;
import com.aleksandr.criminalintent.model.CrimeDataAccess;
import com.aleksandr.criminalintent.model.CrimeRepository;

import java.util.ArrayList;

/**
 * Created by Aleksandr on 04.12.16.
 */

public class CrimeListFragment extends Fragment {

    private static final String SAVE_SHOW_SUBTITLE = "SAVE_SHOW_SUBTITLE";
    private RecyclerView rvCrimeList;
    private CrimeListAdapter adapter;
    private boolean showSubtitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        rvCrimeList = (RecyclerView) v.findViewById(R.id.rv_crime_list);
        rvCrimeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            showSubtitle = savedInstanceState.getBoolean(SAVE_SHOW_SUBTITLE);
        }
        CommonUtils.saveDateFormat(getActivity(), "dd-MMM-yyyy");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        if (showSubtitle) {
            updateTitle();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime_list, menu);

        MenuItem menuItem = menu.findItem((R.id.menu_item_show_subtitle));
        if (showSubtitle) {
            menuItem.setTitle((R.string.menu_hide_subtitle));
        } else {
            menuItem.setTitle((R.string.menu_show_subtitle));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_crime:
                Crime crime = new Crime();
                CrimeDataAccess crimeDataAccess = CrimeRepository.getCrimeDataAccess(
                        getActivity().getApplicationContext());
                crimeDataAccess.addCrime(crime);
                startActivity(CrimePagerActivity.newIntent(getActivity(), crime.getUuid()));
                return true;

            case R.id.menu_item_show_subtitle:
                showSubtitle ^= true; //showSubtitle = !showSubtitle;
                updateTitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_SHOW_SUBTITLE, showSubtitle);
    }

    private void updateUI() {
        if (adapter == null) {
            CrimeDataAccess crimeDataAccess = CrimeRepository.getCrimeDataAccess(
                    getActivity().getApplicationContext());
            ArrayList<Crime> crimes = crimeDataAccess.getCrimes();
            adapter = new CrimeListAdapter(getActivity(), crimes);
            rvCrimeList.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateTitle() {

        CrimeDataAccess crimeDataAccess = CrimeRepository.getCrimeDataAccess(
                getActivity().getApplicationContext());

        int crimeCount = crimeDataAccess.getCrimes().size();
        String subtitle = String.format(getString(R.string.subtitle_format), String.valueOf(crimeCount));
        if (!showSubtitle) {
            subtitle = null;
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(subtitle);
        getActivity().invalidateOptionsMenu();
    }
}
