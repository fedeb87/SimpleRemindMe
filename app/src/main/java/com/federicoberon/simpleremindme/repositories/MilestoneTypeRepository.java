package com.federicoberon.simpleremindme.repositories;

import com.federicoberon.simpleremindme.datasource.AppDatabase;
import com.federicoberon.simpleremindme.datasource.MilestoneTypeDao;
import com.federicoberon.simpleremindme.model.MilestoneType;
import java.util.List;
import io.reactivex.Flowable;

/**
 * Using the Room database as a data source.
 */
public class MilestoneTypeRepository implements MilestoneTypeDataSource{

    private static MilestoneTypeRepository sInstance;
    private final MilestoneTypeDao mMilestoneTypeDao;

    private MilestoneTypeRepository(MilestoneTypeDao milestoneTypeDao) {
        mMilestoneTypeDao = milestoneTypeDao;
    }

    public static MilestoneTypeRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (MilestoneTypeRepository.class) {
                if (sInstance == null) {
                    sInstance = new MilestoneTypeRepository(database.milestoneTypeDao());
                }
            }
        }
        return sInstance;
    }

    @Override
    public Flowable<List<MilestoneType>> getAllTypes() {
        return mMilestoneTypeDao.getAllType();
    }

}
