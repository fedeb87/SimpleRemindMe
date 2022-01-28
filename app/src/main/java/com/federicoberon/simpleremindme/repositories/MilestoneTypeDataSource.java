package com.federicoberon.simpleremindme.repositories;

import com.federicoberon.simpleremindme.model.MilestoneType;

import java.util.List;
import io.reactivex.Flowable;

/**
 * Access point for managing user data.
 */
public interface MilestoneTypeDataSource {

    /**
     * Gets the milestoneType from the data source.
     *
     * @return the milestoneType from the data source.
     */
    Flowable<List<MilestoneType>> getAllTypes();
}
