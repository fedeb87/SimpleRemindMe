package com.federicoberon.simpleremindme.datasource;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.federicoberon.simpleremindme.TestDataHelper;
import com.federicoberon.simpleremindme.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MilestoneTypeDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase mDatabase;

    private MilestoneTypeDao milestoneTypeDao;

    @Before
    public void initDb() {
        mDatabase = TestUtils.initDb();
        milestoneTypeDao = mDatabase.milestoneTypeDao();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void getAllMilestonesTypes() {
        milestoneTypeDao.getAllType().
            test().
            assertValue(types -> {
                // The emitted type is the expected one
                return types.contains(TestDataHelper.MILESTONETYPE_1) && types.size() == 1;
            });
    }
}
