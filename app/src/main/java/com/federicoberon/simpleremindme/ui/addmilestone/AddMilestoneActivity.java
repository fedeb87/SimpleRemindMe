package com.federicoberon.simpleremindme.ui.addmilestone;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.federicoberon.simpleremindme.ViewModelFactory;
import com.federicoberon.simpleremindme.databinding.ActivityAddMilestoneBinding;
import com.federicoberon.simpleremindme.ui.CustomDatePicker;
import com.federicoberon.simpleremindme.ui.CustomTimePicker;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddMilestoneActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AddMilestoneActivity";
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
        new CustomDatePicker(addMilestoneViewModel, binding.editTextDate)
                .show(getSupportFragmentManager(), "datePicker");
    }

    private void showTimePicker(){
        new CustomTimePicker(addMilestoneViewModel, binding.editTextTime)
                .show(getSupportFragmentManager(), "timePicker");
    }
}