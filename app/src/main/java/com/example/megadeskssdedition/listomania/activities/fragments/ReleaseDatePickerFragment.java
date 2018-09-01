package com.example.megadeskssdedition.listomania.activities.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;

import com.example.megadeskssdedition.listmania.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Megadesk SSD Edition on 7/28/2018.
 */

public class ReleaseDatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "date_extra";
    private Date date;

    public static ReleaseDatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        ReleaseDatePickerFragment fragment = new ReleaseDatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //create view first
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        date = (Date)getArguments().getSerializable(EXTRA_DATE);

        //create calendar fields to instantiate the datePicker.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        datePicker.setMaxDate(new Date().getTime());

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                date = new GregorianCalendar(year, month, day).getTime();

                getArguments().putSerializable(EXTRA_DATE, date);
            }
        });
        
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.release_date)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                }).create();
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent dateIntent = new Intent();
        dateIntent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, dateIntent);
    }
}
