package com.federicoberon.simpleremindme;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkManager;

import com.federicoberon.simpleremindme.repositories.MilestoneRepository;
import com.federicoberon.simpleremindme.repositories.MilestoneTypeRepository;
import com.federicoberon.simpleremindme.ui.about.AboutViewModel;
import com.federicoberon.simpleremindme.ui.addmilestone.AddMilestoneViewModel;
import com.federicoberon.simpleremindme.ui.home.HomeViewModel;

/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;
    private final MilestoneRepository mMilestoneRepository;
    private final MilestoneTypeRepository mMilestoneTypeRepository;
    private final WorkManager mWorkManager;
    private final Application mApplication;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(
                            ((SimpleRemindMeApplication)application.getApplicationContext()).
                                    getMilestonesRepository(),
                            ((SimpleRemindMeApplication)application.getApplicationContext()).
                                    getMilestonesTypeRepository(),
                            WorkManager.getInstance(application),
                            application);
                }
            }
        }
        return INSTANCE;
    }

    public MilestoneRepository getMilestonesRepository() {
        return mMilestoneRepository;
    }

    public MilestoneTypeRepository getMilestonesTypeRepository() {
        return mMilestoneTypeRepository;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    public ViewModelFactory(
                            MilestoneRepository milestonesRepository,
                            MilestoneTypeRepository milestoneTypeRepository,
                            WorkManager instance, Application application) {
        mMilestoneRepository = milestonesRepository;
        mMilestoneTypeRepository = milestoneTypeRepository;
        mWorkManager = instance;
        mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            //noinspection unchecked
            return (T) new HomeViewModel(mMilestoneRepository, mWorkManager);
        } else if (modelClass.isAssignableFrom(AddMilestoneViewModel.class)) {
            return (T) new AddMilestoneViewModel(mMilestoneRepository, mMilestoneTypeRepository, mWorkManager, mApplication);
        } else if (modelClass.isAssignableFrom(AboutViewModel.class)) {
            return (T) new AboutViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
