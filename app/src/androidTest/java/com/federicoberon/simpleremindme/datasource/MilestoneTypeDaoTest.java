package com.federicoberon.simpleremindme.datasource;

import android.content.ContentValues;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.federicoberon.simpleremindme.TestData;
import com.federicoberon.simpleremindme.datasource.AppDatabase;
import com.federicoberon.simpleremindme.datasource.MilestoneTypeDao;
import com.federicoberon.simpleremindme.model.MilestoneType;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;

import io.reactivex.functions.Predicate;

@RunWith(AndroidJUnit4.class)
public class MilestoneTypeDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase mDatabase;

    private MilestoneTypeDao milestoneTypeDao;

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

        milestoneTypeDao = mDatabase.milestoneTypeDao();
    }

    @After
    public void closeDb() {
        mDatabase.close();
    }

    @Test
    public void getAllMilestonesWhenNoMilestoneInserted() {
        milestoneTypeDao.getAllType().
            test().
            assertValue(new Predicate<List<MilestoneType>>() {
                @Override
                public boolean test(@NonNull List<MilestoneType> types) {
                    // The emitted user is the expected one
                    return types.contains(TestData.MILESTONETYPE_1) && types.size() == 1;
                }
        });
    }
}
