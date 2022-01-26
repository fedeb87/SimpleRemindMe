package com.federicoberon.simpleremindme;

import android.app.Application;

import com.federicoberon.simpleremindme.datasource.AppDatabase;
import com.federicoberon.simpleremindme.repositories.MilestoneRepository;
import com.federicoberon.simpleremindme.repositories.MilestoneTypeRepository;


/**
 * Android Application class. Used for accessing singletons.
 */
public class SimpleRemindMeApplication extends Application {

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public MilestoneRepository getMilestonesRepository() {
        return MilestoneRepository.getInstance(getDatabase());
    }

    public MilestoneTypeRepository getMilestonesTypeRepository() {
        return MilestoneTypeRepository.getInstance(getDatabase());
    }
}
