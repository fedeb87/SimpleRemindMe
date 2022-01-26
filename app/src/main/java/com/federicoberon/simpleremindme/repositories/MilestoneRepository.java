/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.federicoberon.simpleremindme.repositories;

import com.federicoberon.simpleremindme.datasource.AppDatabase;
import com.federicoberon.simpleremindme.datasource.MilestoneDao;
import com.federicoberon.simpleremindme.datasource.MilestoneTypeDao;
import com.federicoberon.simpleremindme.model.Milestone;
import com.federicoberon.simpleremindme.model.MilestoneType;
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
    private final MilestoneTypeDao mMilestoneTypeDao;

    private MilestoneRepository(MilestoneDao milestoneDao, MilestoneTypeDao milestoneTypeDao) {
        mMilestoneDao = milestoneDao;
        mMilestoneTypeDao = milestoneTypeDao;
    }

    public static MilestoneRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (MilestoneRepository.class) {
                if (sInstance == null) {
                    sInstance = new MilestoneRepository(database.milestoneDao(), database.milestoneTypeDao());
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
    public Flowable<List<MilestoneType>> getMilestoneType() {
        return mMilestoneTypeDao.getAllType();
    }

    @Override
    public Completable deleteMilestone(long currentMilestoneId) {
        return mMilestoneDao.deleteMilestone(currentMilestoneId);
    }
}
