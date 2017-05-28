package com.aleksandr.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aleksandr on 25.04.17.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_CRIME_DATE = "EXTRA_CRIME_DATE";

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date currentDate = (Date) getArguments().getSerializable(EXTRA_CRIME_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.data_picker);
        datePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.crime_pick_date)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Calendar calendar1 = Calendar.getInstance();
                                calendar1.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                Date date = calendar1.getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        }
                )
                        .setView(view)
                        .create();

    }

    private void sendResult(int resultCod, Date date) {
        if (getTargetFragment() == null) return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CRIME_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCod, intent);
    }


}
