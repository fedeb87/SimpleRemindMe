package com.federicoberon.simpleremindme.datasource;

import android.content.ContentValues;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.federicoberon.simpleremindme.TestData;
import com.federicoberon.simpleremindme.datasource.AppDatabase;
import com.federicoberon.simpleremindme.datasource.MilestoneDao;
import com.federicoberon.simpleremindme.model.MilestoneXType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MilestoneDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase mDatabase;

    private MilestoneDao mMilestoneDao;

    @Before
    public void initDb() {

        RoomDatabase.Callback rdc = new RoomDatabase.Callback(){
            public void onCreate (SupportSQLiteDatabase db){
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", "Family");
                contentValues.put("color", "#F06423");
                db.insert("milestone_types", OnConflictStrategy.IGNORE, contentValues);
            }
        };

        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class)
                .addCallback(rdc)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        mMilestoneDao = mDatabase.milestoneDao();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void getAllMilestonesWhenNoMilestoneInserted() {
        mMilestoneDao.getAllMilestone().
                test().
                assertEmpty();
    }

    @Test
    public void getFilterMilestoneTest(){
        MilestoneXType milestone = TestData.MILESTONE_2;
        mMilestoneDao.getMilestones(milestone.getTitle()).
                test().
                assertEmpty();

        mMilestoneDao.insertMilestone(milestone.getMilestone());

        mMilestoneDao.getMilestones(milestone.getTitle()).
                test().
                assertValue(milestone2 -> {
                    if (milestone2.size()==1)
                        return milestone2.get(0).equals(TestData.MILESTONE_2);
                    return false;
                });

    }

    @Test
    public void writeUserAndReadInList() {

        MilestoneXType milestone = TestData.MILESTONE_1;
        mMilestoneDao.insertMilestone(milestone.getMilestone()).
            test().assertComplete();


        mMilestoneDao.getMilestoneById(TestData.MILESTONE_1.getId()).
                test().
                assertValue(milestone1 -> {
                    // The emitted user is the expected one
                    return milestone1.equals(TestData.MILESTONE_1);
                });
    }

    @Test
    public void deleteMilestoneTest() {
        MilestoneXType milestone = TestData.MILESTONE_1;

        //check database are empty
        mMilestoneDao.getMilestones(milestone.getTitle()).
                test().
                assertEmpty();

        // insert one element
        mMilestoneDao.insertMilestone(milestone.getMilestone()).
                test().assertComplete();

        // check that element are inserted
        mMilestoneDao.getAllMilestone().
                test().
                assertValue(milestoneXTypes -> {
                    // The emitted user is the expected one
                    return milestoneXTypes.size()==1;
                });

        // delete inserted element
        mMilestoneDao.deleteMilestone(milestone.getId()).
                test().assertComplete();

        // check the database are empty
        // The emitted user is the expected one
        mMilestoneDao.getAllMilestone().
                test().
                assertValue(List::isEmpty);

    }
}
