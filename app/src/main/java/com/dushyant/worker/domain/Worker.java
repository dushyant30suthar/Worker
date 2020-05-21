package com.dushyant.worker.domain;


import android.util.Log;

import com.dushyant.worker.client.foregroundcomponents.imageComponent.Task;
import com.dushyant.worker.framework.utils.ThreadManager;

import java.util.concurrent.Executor;

/*
 * We control our data flow here. We decide from where data should be fetched depending upon certain conditions.*/

public class Worker<ResultType> {

    private String TAG;

    private ThreadManager threadManager = ThreadManager.getInstance();

    private Executor networkOperationThread;

    public Worker(String workerName) {
        networkOperationThread = threadManager.getNetworkOperationThread(true);
        TAG = workerName;
        Log.i(TAG, "worker initialized");
    }

    synchronized private void doWork(String taskName, Task<ResultType> task) {
        Log.i(TAG, "worker executing " + taskName);

        ResultType dataFromNetwork = task.onExecuteTask();

        if (dataFromNetwork != null) {

            Log.d(TAG, "Request Success " + taskName);

            threadManager.getMainThread().execute(() -> task.onTaskComplete(dataFromNetwork));

        } else {

            Log.e(TAG, "Request Failure " + taskName);

            //onFetchFailed(new Throwable("No Data Received"));

        }

        Log.i(TAG, "worker completed task " + taskName);

    }

    public void addTask(String taskName, Task<ResultType> task) {
        networkOperationThread.execute(() -> doWork(taskName, task));
    }
}
