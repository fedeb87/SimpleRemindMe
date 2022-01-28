package com.federicoberon.simpleremindme.datasource;

import androidx.room.Dao;
import androidx.room.Query;
import com.federicoberon.simpleremindme.model.MilestoneType;
import java.util.List;
import io.reactivex.Flowable;

/**
 * Data Access Object for the users table.
 */
@Dao
public interface MilestoneTypeDao {

    /**
     * Get the user from the table. Since for simplicity we only have one user in the database,
     * this query gets all milestones from the table, but limits the result to just the 1st milestone.
     *
     * @return the user from the table
     */
    @Query("SELECT * FROM milestone_types")
    Flowable<List<MilestoneType>> getAllType();

    // TODO Insert / Update and Delete events type are not current developed

}
