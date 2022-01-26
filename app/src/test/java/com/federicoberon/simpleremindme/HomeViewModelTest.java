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

package com.federicoberon.simpleremindme;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.work.WorkManager;

import com.federicoberon.simpleremindme.model.MilestoneXType;
import com.federicoberon.simpleremindme.repositories.MilestoneRepository;
import com.federicoberon.simpleremindme.ui.home.HomeViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;

/**
 * Unit test for {@link HomeViewModel}
 */
public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private WorkManager mWorkManager;

    @Mock
    private MilestoneRepository milestoneRepository;

    private HomeViewModel mViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mViewModel = new HomeViewModel(milestoneRepository);
    }

    @Test
    public void getAllMilestones_whenNoMilestonesSaved() {
        // Given that the UserDataSource returns an empty list of users
        when(milestoneRepository.getAllMilestone()).thenReturn(Flowable.empty());

        //When getting the user name
        mViewModel.getMilestones("")
                .test()
                // The user name is empty
                .assertNoValues();
    }

    @Test
    public void getMilestone_whenMilestoneSaved() {
        // Given that the UserDataSource returns a user

        when(milestoneRepository.getAllMilestone()).thenReturn(Flowable.just(Collections.singletonList(TestData.MILESTONE_1)));

        //When getting the user name
        mViewModel.getMilestones("")
                .test()
                .assertValue(Collections.singletonList(TestData.MILESTONE_1));

    }

    @Test
    public void getMilestoneById() {
        // Given that the UserDataSource returns a user
        long id = TestData.MILESTONE_2.getId();
        when(milestoneRepository.getMilestoneById(id)).thenReturn(Flowable.just(TestData.MILESTONE_2));

        //When getting the user name
        mViewModel.getMilestoneById(id)
                .test()
                // The correct user name is emitted
                .assertValue(new Predicate<MilestoneXType>() {
                    @Override
                    public boolean test(@NonNull MilestoneXType milestoneXType) {
                        // The emitted user is the expected one
                        return milestoneXType.equals(TestData.MILESTONE_2) &&
                                mViewModel.getCurrentMilestoneId() == TestData.MILESTONE_2.getId();
                    }
                });

    }

    @Test
    public void deleteMilestone(){

        long testId = 1;
        // Given that the UserDataSource returns an empty list of users
        when(milestoneRepository.deleteMilestone(testId)).thenReturn(Completable.complete());

        mViewModel.deleteMilestone();

        verify(milestoneRepository).deleteMilestone(-1);
    }

    @Test
    public void propertiesAreSetMilestoneConstructor() {
        HomeViewModel homeViewModel = new HomeViewModel(milestoneRepository,
                mWorkManager);

        assertSame(mWorkManager, homeViewModel.getWorkManager());
        assertSame(milestoneRepository, homeViewModel.getMilestoneRepository());
    }
}
