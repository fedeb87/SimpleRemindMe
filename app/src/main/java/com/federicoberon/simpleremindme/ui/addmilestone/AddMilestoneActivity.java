package com.federicoberon.simpleremindme.ui.addmilestone;

import static java.lang.String.format;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.federicoberon.simpleremindme.ViewModelFactory;
import com.federicoberon.simpleremindme.databinding.ActivityAddMilestoneBinding;

import java.util.Calendar;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddMilestoneActivity extends AppCompatActivity {

    private static String LOG_TAG = "AddMilestoneActivity";
    public static final String KEY_TYPE_ID = "idType";
    private AddMilestoneViewModel addMilestoneViewModel;
    private ActivityAddMilestoneBinding binding;
    private MilestoneTypeAdapter milestoneTypeAdapter;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMilestoneViewModel = new ViewModelProvider(this, ViewModelFactory.
                getInstance(this.getApplication())).get(AddMilestoneViewModel.class);

        binding = ActivityAddMilestoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        milestoneTypeAdapter = new MilestoneTypeAdapter();
        binding.recyclerViewMilestoneType.setAdapter(milestoneTypeAdapter);
        binding.recyclerViewMilestoneType.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * Go Home
     * @param item option selected
     * @return always return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDisposable.add(addMilestoneViewModel.getAllTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(types -> milestoneTypeAdapter.setTypes(types),
                        throwable -> Log.e(LOG_TAG, "Unable to get milestones: ",
                                throwable)));

        // DatePicker
        binding.editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // TimePicker
        binding.editTextTime.setOnClickListener(v -> showTimePicker());

        binding.okButton.setOnClickListener(v -> onSaveButtonClicked());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        mDisposable.clear();
    }

    private void onSaveButtonClicked() {
        /*
         TODO: make all necessary validations over fields to ensure the data integrity.
          I assume that all fields are load correctly
        */
        mDisposable.add(addMilestoneViewModel.saveMilestone(
                Objects.requireNonNull(binding.editTextMilestoneTitle.getText()).toString(),
                Objects.requireNonNull(binding.editTextDescriptionMilestone.getText()).toString(),
                addMilestoneViewModel.getDate(),
                (int) milestoneTypeAdapter.getItemSelected().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {addMilestoneViewModel.scheduledMilestone(id);
                        finish();
                        },
                        throwable -> Log.e(LOG_TAG, "Unable to save Milestone", throwable)));

    }

    public void showDatePickerDialog(){
        int year;
        int month;
        int day;

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    addMilestoneViewModel.setDate(year1, month1, dayOfMonth);
                    month1++;

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year1);
                    cal.set(Calendar.MONTH, month1);
                    cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    binding.editTextDate.setText(DateFormat.getDateFormat(this.getApplicationContext()).format(cal.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(){
        int hora;
        int minute;

        Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timeGetter = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
            cal.set(Calendar.MINUTE, minute1);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);

            addMilestoneViewModel.setTime(hourOfDay, minute1);
            binding.editTextTime.setText(format("%s", DateFormat.getTimeFormat(this).format(cal.getTime())));

        }, hora, minute, false);

        timeGetter.show();
    }
}