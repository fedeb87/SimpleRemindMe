package com.federicoberon.simpleremindme.repositories;

import com.federicoberon.simpleremindme.datasource.AppDatabase;
import com.federicoberon.simpleremindme.datasource.MilestoneDao;
import com.federicoberon.simpleremindme.model.Milestone;
import com.federicoberon.simpleremindme.model.MilestoneXType;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Using the Room database as a data source.
 */
public class MilestoneRepository implements MilestoneDataSource {

    private static MilestoneRepository sInstance;
    private final MilestoneDao mMilestoneDao;

    private MilestoneRepository(MilestoneDao milestoneDao) {
        mMilestoneDao = milestoneDao;
    }

    public static MilestoneRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (MilestoneRepository.class) {
                if (sInstance == null) {
                    sInstance = new MilestoneRepository(database.milestoneDao());
                }
            }
        }
        return sInstance;
    }

    @Override
    public Flowable<List<MilestoneXType>> getAllMilestone() {
        return mMilestoneDao.getAllMilestone();
    }

    @Override
    public Flowable<MilestoneXType> getMilestoneById(long id) {
        return mMilestoneDao.getMilestoneById(id);
    }

    @Override
    public Flowable<List<MilestoneXType>> getMilestones(String filter) {
        String finalFilter = "%" +
                filter +
                "%";
        return mMilestoneDao.getMilestones(finalFilter);
    }

    @Override
    public Maybe<Long> insertOrUpdateMilestone(Milestone milestone) {
       return mMilestoneDao.insertMilestone(milestone);
    }

    @Override
    public Completable deleteMilestone(long currentMilestoneId) {
        return mMilestoneDao.deleteMilestone(currentMilestoneId);
    }
}
