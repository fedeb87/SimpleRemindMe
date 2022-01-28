package com.federicoberon.simpleremindme.ui.addmilestone;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.federicoberon.simpleremindme.R;
import com.federicoberon.simpleremindme.model.Milestone;
import com.federicoberon.simpleremindme.model.MilestoneType;
import com.federicoberon.simpleremindme.repositories.MilestoneRepository;
import com.federicoberon.simpleremindme.repositories.MilestoneTypeRepository;
import com.federicoberon.simpleremindme.workmanager.NotificationsWorker;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class AddMilestoneViewModel extends ViewModel {
    private final WorkManager mWorkManager;
    private final MilestoneTypeRepository mMilestoneTypeRepository;
    private final MilestoneRepository mMilestoneRepository;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private Milestone insertedMilestone;
    private int mHour;
    private int mMinutes;
    private int mYear;
    private int mMonth;
    private int mDay;

    public AddMilestoneViewModel(MilestoneRepository milestoneRepository,
                                 MilestoneTypeRepository repo, WorkManager workManager,
                                 Application mApplication) {
        this.mWorkManager = workManager;
        this.mMilestoneTypeRepository = repo;
        this.mMilestoneRepository = milestoneRepository;
        this.context = mApplication;
    }

    public AddMilestoneViewModel(MilestoneRepository milestoneRepository,
                                 MilestoneTypeRepository repo, WorkManager workerManager) {
        this.mMilestoneRepository = milestoneRepository;
        this.mMilestoneTypeRepository = repo;
        mWorkManager = workerManager;
        this.context = null;
    }


    public void setTime(int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinutes = minute;
    }

    public Date getDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinutes);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public void setDate(int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
    }

    public Flowable<List<MilestoneType>> getAllTypes() {
        return mMilestoneTypeRepository.getAllTypes();
    }

    public Milestone getInsertedMilestone(){
        return this.insertedMilestone;
    }

    public Maybe<Long> saveMilestone(String title, String description,
                                     Date milestoneDate, int type) {
        insertedMilestone = new Milestone(title, description, milestoneDate, type);
        return mMilestoneRepository.insertOrUpdateMilestone(insertedMilestone);
    }

    private String getTimeToNotificationMessage(Date milestoneDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(milestoneDate);
        String formattedHour =  (calendar.get(Calendar.HOUR) < 10)? "0"
                + calendar.get(Calendar.HOUR) : String.valueOf(calendar.get(Calendar.HOUR));
        String formattedMinute = (calendar.get(Calendar.MINUTE) < 10)? "0"
                + calendar.get(Calendar.MINUTE) :String.valueOf(calendar.get(Calendar.MINUTE));
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH)
                + "/" + calendar.get(Calendar.YEAR) + ", " + formattedHour + ":" + formattedMinute;
    }

    public void scheduledMilestone(long id){

        String milestone_name = "-" + id + "-";

        long currentTime = System.currentTimeMillis();
        long specificTimeToTrigger = insertedMilestone.getMilestoneDate().getTime();

        assert context != null;
        Data args = new Data.Builder()
                .putString("title", context.getString(R.string.remember_alarm)
                        + getTimeToNotificationMessage(insertedMilestone.getMilestoneDate()))
                .putString("message", insertedMilestone.getTitle())
                .putLong("id", id)
                .build();

        if (currentTime<specificTimeToTrigger) {
            long delayToPass = specificTimeToTrigger - currentTime;
            OneTimeWorkRequest milestoneWork =
                    new OneTimeWorkRequest.Builder(NotificationsWorker.class)
                            .setInputData(args)
                            .addTag(String.valueOf(id))
                            .setInitialDelay(delayToPass, TimeUnit.MILLISECONDS)
                            .build();

            mWorkManager.enqueueUniqueWork(milestone_name,
                    ExistingWorkPolicy.REPLACE, milestoneWork);
        }
    }

    public WorkManager getWorkManager() {
        return mWorkManager;
    }

    public MilestoneRepository getMilestoneRepository() {
        return mMilestoneRepository;
    }

    public MilestoneTypeRepository getMilestoneTypeRepository() {
        return mMilestoneTypeRepository;
    }
}