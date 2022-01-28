package com.federicoberon.simpleremindme.repositories;

import com.federicoberon.simpleremindme.model.Milestone;
import com.federicoberon.simpleremindme.model.MilestoneType;
import com.federicoberon.simpleremindme.model.MilestoneXType;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Access point for managing user data.
 */
public interface MilestoneDataSource {

    /**
     * Gets the milestone from the data source.
     *
     * @return the milestone from the data source.
     */
    Flowable<List<MilestoneXType>> getAllMilestone();

    Flowable<List<MilestoneXType>> getMilestones(String filter);

    Flowable<MilestoneXType> getMilestoneById(long id);


    /**
     * Inserts the milestone into the data source, or, if this is an existing user, updates it.
     *
     * @param milestone the user to be inserted or updated.
     */
    Maybe<Long> insertOrUpdateMilestone(Milestone milestone);

    Completable deleteMilestone(long currentMilestoneId);
}
