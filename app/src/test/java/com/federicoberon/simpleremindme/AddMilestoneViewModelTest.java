package com.federicoberon.simpleremindme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.work.WorkManager;
import com.federicoberon.simpleremindme.model.MilestoneType;
import com.federicoberon.simpleremindme.model.MilestoneXType;
import com.federicoberon.simpleremindme.repositories.MilestoneRepository;
import com.federicoberon.simpleremindme.repositories.MilestoneTypeRepository;
import com.federicoberon.simpleremindme.ui.addMilestone.AddMilestoneViewModel;
import com.federicoberon.simpleremindme.ui.home.HomeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Predicate;

/**
 * Unit test for {@link HomeViewModel}
 */
public class AddMilestoneViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private WorkManager mWorkManager;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private MilestoneTypeRepository milestoneTypeRepository;

    private AddMilestoneViewModel mViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mViewModel = new AddMilestoneViewModel(milestoneRepository, milestoneTypeRepository, mWorkManager);
    }

    @Test
    public void saveMilestoneTest() {

        // Given that the UserDataSource returns a user
        MilestoneXType milestone = TestData.MILESTONE_2;
        when(milestoneRepository.insertOrUpdateMilestone(milestone.getMilestone())).thenReturn(Maybe.just(milestone.getId()));

        //When getting the user name
        mViewModel.saveMilestone(milestone.getTitle(), milestone.getDesc(),
                milestone.getMilestoneDate(), milestone.getType())
                .test()
                // The correct user name is emitted
                .assertValue(new Predicate<Long>() {
                    @Override
                    public boolean test(@NonNull Long id) {
                        // The emitted user is the expected one
                        return id == milestone.getId() &&
                                mViewModel.getInsertedMilestone() == milestone.getMilestone();
                    }
                });
    }

    @Test
    public void getAllTypes_whenAreEmpty() {
        // Given that the UserDataSource returns an empty list of users
        when(milestoneTypeRepository.getAllTypes()).thenReturn(Flowable.empty());

        //When getting the user name
        mViewModel.getAllTypes()
                .test()
                // The user name is empty
                .assertNoValues();
    }

    @Test
    public void getAllTypesTest() {

        when(milestoneTypeRepository.getAllTypes()).thenReturn(Flowable.just(Arrays.asList(
                TestData.MILESTONETYPE_1, TestData.MILESTONETYPE_2)));

        //When getting the user name
        mViewModel.getAllTypes()
                .test()
                // The user name is empty
                .assertValue(new Predicate<List<MilestoneType>>() {
                    @Override
                    public boolean test(@NonNull List<MilestoneType> milestoneTypes) {
                        // The emitted user is the expected one
                        return milestoneTypes.containsAll(Arrays.asList(
                                TestData.MILESTONETYPE_1, TestData.MILESTONETYPE_2));
                    }
                });
    }

    @Test
    public void getTimeToNotificationMessageTest() {

        Calendar calendar = TestData.getDate1();
        String result = "";
        String formattedHour =  (calendar.get(Calendar.HOUR) < 10)? "0" + calendar.get(Calendar.HOUR) : String.valueOf(calendar.get(Calendar.HOUR));
        String formattedMinute = (calendar.get(Calendar.MINUTE) < 10)? "0" + calendar.get(Calendar.MINUTE) :String.valueOf(calendar.get(Calendar.MINUTE));
        String correctResult = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR) + ", " + formattedHour + ":" + formattedMinute;

        try {
            // I'm use reflection to test private methods
            Method method = AddMilestoneViewModel.class.getDeclaredMethod(
                    "getTimeToNotificationMessage", Date.class);
            method.setAccessible(true);
            result = (String)method.invoke(mViewModel, calendar.getTime());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        assertEquals(result, correctResult);

    }

    @Test
    public void setAndGetTime(){

        Calendar calendar = TestData.getDate1();
        mViewModel.setTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        mViewModel.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        assertEquals(mViewModel.getDate(),calendar.getTime());
    }

    @Test
    public void propertiesAreSetMilestoneConstructor() {
        AddMilestoneViewModel addMilestoneViewModel = new AddMilestoneViewModel(milestoneRepository,
                milestoneTypeRepository, mWorkManager);

        assertSame(mWorkManager, addMilestoneViewModel.getWorkManager());
        assertSame(milestoneRepository, addMilestoneViewModel.getMilestoneRepository());
        assertSame(milestoneTypeRepository, addMilestoneViewModel.getMilestoneTypeRepository());
    }
}
