package com.example.covidvaccine;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    public static final int FLAG_REGISTER = 0;
    public static final int FLAG_ADD_SLOT = 1;
    public static final int FLAG_USER_EDIT = 2;
    private int flag = 0;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it.
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        return dialog;

    }
    public void setFlag(int i) {
        flag = i;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (flag == FLAG_REGISTER) {
            // Set the activity to the Main Activity.
            register activity = (register) getActivity();
            // Invoke Register Activity's processDatePickerResult() method.
            activity.processDatePickerResult(year, month, dayOfMonth);
        }
        else if (flag == FLAG_ADD_SLOT) {
            // Set the activity to the Main Activity.
            add_new_slot activity = (add_new_slot) getActivity();
            // Invoke Register Activity's processDatePickerResult() method.
            activity.processDatePickerResult(year, month, dayOfMonth);
        }
    }
}
