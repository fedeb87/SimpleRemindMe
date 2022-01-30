package com.federicoberon.simpleremindme.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.federicoberon.simpleremindme.ui.addmilestone.AddMilestoneViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CustomDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private final TextInputEditText editTextTime;
    private final AddMilestoneViewModel addMilestoneViewModel;

    public CustomDatePicker(AddMilestoneViewModel addMilestoneViewModel, TextInputEditText editTextTime){
        super();
        this.addMilestoneViewModel = addMilestoneViewModel;
        this.editTextTime = editTextTime;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        addMilestoneViewModel.setDate(year, month, day);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH,day);
        editTextTime.setText(DateFormat.getDateFormat(requireContext()).format(cal.getTime()));
    }
}