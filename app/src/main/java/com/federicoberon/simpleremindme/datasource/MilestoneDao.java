package com.federicoberon.simpleremindme.datasource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.federicoberon.simpleremindme.model.Milestone;
import com.federicoberon.simpleremindme.model.MilestoneXType;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Data Access Object for the users table.
 */
@Dao
public interface MilestoneDao {

    /**
     * Get the user from the table. Since for simplicity we only have one user in the database,
     * this query gets all milestones from the table, but limits the result to just the 1st milestone.
     *
     * @return the user from the table
     */
    @Query("SELECT m.*, mt.title as typeName, mt.color FROM milestones m, milestone_types mt WHERE m.type = mt.id ORDER BY m.milestone_date DESC")
    Flowable<List<MilestoneXType>> getAllMilestone();

    @Query("SELECT m.*, mt.title as typeName, mt.color FROM milestones m, milestone_types mt WHERE m.type = mt.id AND (m.title LIKE :filter OR m.description LIKE :filter) ORDER BY m.milestone_date DESC")
    Flowable<List<MilestoneXType>> getMilestones(String filter);

    /**
     * Insert a user in the database. If the user already exists, replace it.
     *
     * @param milestone the milestone to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insertMilestone(Milestone milestone);

    @Query("SELECT m.*, mt.title as typeName, mt.color FROM milestones m, milestone_types mt WHERE m.type = mt.id AND m.id = :id")
    Flowable<MilestoneXType> getMilestoneById(long id);

    @Query("DELETE FROM milestones WHERE id = :currentMilestoneId")
    Completable deleteMilestone(long currentMilestoneId);
}
