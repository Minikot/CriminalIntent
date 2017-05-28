package com.aleksandr.criminalintent;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.aleksandr.criminalintent.model.Crime;
import com.aleksandr.criminalintent.model.CrimeDataAccess;
import com.aleksandr.criminalintent.model.CrimeRepository;

import java.util.Date;
import java.util.UUID;

import static com.aleksandr.criminalintent.DatePickerFragment.EXTRA_CRIME_DATE;

/**
 * Created by Aleksandr on 04.11.16.
 */

public class CrimeFragment extends Fragment {

    public static final String EXTRA_CRIME_UUID = "EXTRA_CRIME_UUID";
    public static final String DIALOG_PICK_DATE = "DIALOG_PICK_DATE";
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_CONTACT = 1;

    private EditText etTitle;
    private Button btnDate;
    private CheckBox cbSolved;

    private Button btnChooseS;
    private Button btnSendR;


    private Crime crime;

    public static CrimeFragment newInstance(UUID uuid) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_UUID, uuid);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable(EXTRA_CRIME_UUID);
        CrimeDataAccess crimeDataAccess = CrimeRepository.getCrimeDataAccess(
                getActivity().getApplicationContext());
        crime = crimeDataAccess.getCrime(uuid);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        etTitle = (EditText) v.findViewById(R.id.et_title);
        btnDate = (Button) v.findViewById(R.id.btn_date);
        cbSolved = (CheckBox) v.findViewById(R.id.cb_solved);
        btnChooseS = (Button) v.findViewById(R.id.btn_choose_suspect);
        btnSendR = (Button) v.findViewById(R.id.btn_send_report);


        /**
         * При нажатии кнопки выбора даты, мы получаем с текущего Крайма нудную нам дату. С помощью статического метода newInstans мы
         * формируем новый фрагмент с арументами в котором предоставляется дата текущего крайма и запустили даный Dialog.
         */
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(crime.getDate());

                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);

                datePickerFragment.show(fragmentManager, DIALOG_PICK_DATE);
            }
        });


        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cbSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
            }
        });

        btnChooseS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChooseSuspect = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intentChooseSuspect, REQUEST_CONTACT);

            }
        });

        btnSendR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intentSendReport = new Intent(Intent.ACTION_SEND);
                    intentSendReport.setType("text/plain");
                    intentSendReport.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                    intentSendReport.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                    startActivity(Intent.createChooser(intentSendReport, getString(R.string.crime_report_send)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "you have not proper application for send report", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (crime.getSuspect() != null){
            btnChooseS.setText(crime.getSuspect());
        }

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        updateDate();
        etTitle.setText(crime.getTitle());
        cbSolved.setChecked(crime.isSolved());
        Toast.makeText(getActivity(), "Data updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeDataAccess crimeDataAccess = CrimeRepository.getCrimeDataAccess(
                getActivity().getApplicationContext());
        crimeDataAccess.updateCrime(crime);
    }

    // Переопределяем метод. Даный метод будет принимать возвращаемые данные.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_DATE:
                    Date date = (Date) data.getSerializableExtra(EXTRA_CRIME_DATE);
                    crime.setDate(date);
                    updateDate();
                    break;
                case REQUEST_CONTACT:
                    if (data != null){
                        Uri contactUri = data.getData();
                        String[] projections = new String[]{ContactsContract.Contacts.DISPLAY_NAME}; // СТолбцы которые будут выбраны
                        Cursor cursor = getActivity().getContentResolver().query(contactUri, projections, null, null, null);

                        if (cursor.getCount() == 0) return;
                        try {
                            cursor.moveToFirst();
                            String name = cursor.getString(0);
                            crime.setSuspect(name);
                            btnChooseS.setText(name);
                        } finally {
                            cursor.close();
                        }
                    }
                    break;
            }
        }

    }

    private void updateDate() {
        btnDate.setText(crime.getDate().toString());
    }

    private String getCrimeReport() {
        String crimeSolved = null;
        if (crime.isSolved()) {
            crimeSolved = getString(R.string.crime_report_solved);
        } else {
            crimeSolved = getString(R.string.crime_report_not_solved);
        }

        String crimeSuspect = crime.getSuspect();
        if (crimeSuspect == null) {
            crimeSuspect = getString(R.string.crime_report_no_suspect);
        } else {
            crimeSuspect = getString(R.string.crime_report_suspect, crimeSuspect);
        }

        String report = getString(R.string.crime_report_body,
                crime.getTitle(),
                crimeSolved,
                crime.getDate("dd/MM/yy"),
                crimeSuspect);
        return report;
    }
}
