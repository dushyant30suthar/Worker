package com.dushyant.worker.domain;


import android.util.Log;

import com.dushyant.worker.client.foregroundcomponents.imageComponent.Task;

import io.reactivex.Observable;
import io.reactivex.Observer;

/*
 * We control our data flow here. We decide from where data should be fetched depending upon certain conditions.*/

public class Worker<ResultType> extends Observable<DomainRequest<ResultType>> {


    private String TAG = "Worker";

    private Observer<? super DomainRequest<ResultType>> requestObserver;

    private Task<ResultType> task;

    public Worker(Task<ResultType> task) {
        TAG = task.getWorkerName();
        this.task = task;
    }

    /*
     * Here is the flow implemented. This series of statements decide the final data returned to ui.*/
    @Override
    protected void subscribeActual(Observer<? super DomainRequest<ResultType>> observer) {

        Log.i(TAG, "worker started");

        requestObserver = observer;

        fetchFromNetwork();

        requestObserver.onComplete();

        Log.i(TAG, "worker completed task");
    }


    private void fetchFromNetwork() {

        Log.i(TAG, "worker executing task on background");

        ResultType dataFromNetwork = task.onExecuteTask();

        if (dataFromNetwork != null) {

            Log.d(TAG, "Request Success ");

            requestObserver.onNext(DomainRequest.succeed(dataFromNetwork, 200));

        } else {

            Log.e(TAG, "Request Failure ");

            onFetchFailed(new Throwable("No Data Received"));

        }

    }

    private void onFetchFailed(Throwable throwable) {
        requestObserver.onError(throwable);
    }
}
