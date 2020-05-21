package com.dushyant.worker.domain;


import android.util.Log;

import com.dushyant.worker.client.foregroundcomponents.imageComponent.Task;

import io.reactivex.Observable;
import io.reactivex.Observer;

/*
 * We control our data flow here. We decide from where data should be fetched depending upon certain conditions.*/

public class Worker<ResultType> extends Observable<Task<ResultType>> {


    private String TAG = "Worker";

    private Observer<? super Task<ResultType>> requestObserver;

    /*
     * Here is the flow implemented. This series of statements decide the final data returned to ui.*/
    @Override
    protected void subscribeActual(Observer<? super Task<ResultType>> observer) {

        Log.i(TAG, "worker started");

        requestObserver = observer;

        //requestObserver.onComplete();
    }

    private void doWork(Task<ResultType> task) {

        Log.i(TAG, "worker executing " + task.getTaskName());

        ResultType dataFromNetwork = task.onExecuteTask();

        if (dataFromNetwork != null) {

            Log.d(TAG, "Request Success ");

            requestObserver.onNext(task);

        } else {

            Log.e(TAG, "Request Failure ");

            onFetchFailed(new Throwable("No Data Received"));

        }

        Log.i(TAG, "worker completed task");

    }

    public void addTask(Task<ResultType> task) {
        doWork(task);
    }

    private void onFetchFailed(Throwable throwable) {
        requestObserver.onError(throwable);
    }
}
