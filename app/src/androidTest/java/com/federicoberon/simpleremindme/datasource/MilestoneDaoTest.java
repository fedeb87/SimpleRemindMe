package com.federicoberon.simpleremindme.datasource;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.federicoberon.simpleremindme.TestDataHelper;
import com.federicoberon.simpleremindme.TestUtils;
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
        mDatabase = TestUtils.initDb();
        mMilestoneDao = mDatabase.milestoneDao();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void getAllMilestonesWhenNoMilestoneInserted() {
        // The emitted type is the expected one
        mMilestoneDao.getAllMilestone().
                test().
                assertValue(List::isEmpty);
    }

    @Test
    public void getFilterMilestoneTest(){
        MilestoneXType milestone = TestDataHelper.MILESTONE_1;
        mMilestoneDao.getMilestones(milestone.getTitle()).
                test().
                assertValue(List::isEmpty);

        mMilestoneDao.insertMilestone(milestone.getMilestone())
                .test()
                .assertComplete();

        mMilestoneDao.getMilestones(milestone.getTitle()).
                test().
                assertValue(milestone2 -> {
                    if (milestone2.size()==1)
                        return milestone2.get(0).equals(TestDataHelper.MILESTONE_1);
                    return false;
                });

    }

    @Test
    public void writeUserAndReadInList() {

        MilestoneXType milestone = TestDataHelper.MILESTONE_1;
        mMilestoneDao.insertMilestone(milestone.getMilestone()).
            test().assertComplete();


        mMilestoneDao.getMilestoneById(TestDataHelper.MILESTONE_1.getId()).
                test().
                assertValue(milestone1 -> {
                    // The emitted user is the expected one
                    return milestone1.equals(TestDataHelper.MILESTONE_1);
                });
    }

    @Test
    public void deleteMilestoneTest() {
        MilestoneXType milestone = TestDataHelper.MILESTONE_1;

        //check database are empty
        mMilestoneDao.getMilestones(milestone.getTitle()).
                test().
                assertValue(List::isEmpty);

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
