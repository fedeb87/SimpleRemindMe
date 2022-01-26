package com.federicoberon.simpleremindme.ui.home;

import androidx.lifecycle.ViewModel;
import androidx.work.WorkManager;
import com.federicoberon.simpleremindme.model.MilestoneXType;
import com.federicoberon.simpleremindme.repositories.MilestoneRepository;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class HomeViewModel extends ViewModel {

    private final WorkManager mWorkManager;
    private final MilestoneRepository mMilestoneRepository;
    private String mQuery;
    private long currentMilestoneId;

    public HomeViewModel(MilestoneRepository repo, WorkManager workManager) {
        this.mWorkManager = workManager;
        this.mMilestoneRepository = repo;
        this.currentMilestoneId = -1;
    }

    public HomeViewModel(MilestoneRepository repo) {
        this.mMilestoneRepository = repo;
        mWorkManager = null;
        this.currentMilestoneId = -1;
    }

    public Flowable<List<MilestoneXType>> getMilestones(String query) {
        if (query.isEmpty()) return mMilestoneRepository.getAllMilestone();
        if (mQuery!=null && !mQuery.isEmpty()) return mMilestoneRepository.getMilestones(mQuery);
        mQuery = query;
        return mMilestoneRepository.getMilestones(query);
    }

    public long getCurrentMilestoneId(){
        return currentMilestoneId;
    }

    public Flowable<MilestoneXType> getMilestoneById(long id) {
        this.currentMilestoneId = id;
        return mMilestoneRepository.getMilestoneById(id);
    }

    public WorkManager getWorkManager() {
        return mWorkManager;
    }

    public MilestoneRepository getMilestoneRepository() {
        return mMilestoneRepository;
    }

    public Completable deleteMilestone() {
        return mMilestoneRepository.deleteMilestone(currentMilestoneId);
    }
}