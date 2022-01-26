package com.federicoberon.simpleremindme;

import static org.junit.Assert.assertEquals;

import android.app.Application;

import androidx.test.core.app.ApplicationProvider;
import androidx.work.Data;
import androidx.work.ListenableWorker.Result;
import androidx.work.testing.TestWorkerBuilder;

import com.federicoberon.simpleremindme.workmanager.NotificationsWorker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class NotificationsWorkerTest {

    private Application context;
    private Executor executor;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        executor = Executors.newSingleThreadExecutor();
    }

    @Test
    public void testNotificationsWorker() {

        Data inputData = new Data.Builder()
                .putString("title", "title")
                .putString("message", "message")
                .putString("idFirebase", "idFirebase")
                .build();

        NotificationsWorker worker =
                (NotificationsWorker) TestWorkerBuilder.from(context,
                        NotificationsWorker.class,
                        executor)
                        .setInputData(inputData)
                        .build();

        Result result = worker.doWork();
        assertEquals(result,Result.success());
    }
}