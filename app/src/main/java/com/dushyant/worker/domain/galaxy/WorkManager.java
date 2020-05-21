package com.dushyant.worker.domain.galaxy;

import android.graphics.Bitmap;
import android.util.Log;

import com.dushyant.worker.client.foregroundcomponents.imageComponent.Task;
import com.dushyant.worker.domain.DomainRequestObserver;
import com.dushyant.worker.domain.Worker;
import com.dushyant.worker.framework.utils.ThreadManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/*
 * If any view component wants to interact with data, then it could be possible by using repository of particular
 * type of unit. For example, Both of the views needs to have information about galaxy. They both need to use
 * Galaxy Repository.*/

public class WorkManager {
    private static final String TAG = "WorkManager";
    /*
     * I just exposed network to ui for our purpose*/
    private ThreadManager threadManager;
    private List<Disposable> workerList = new ArrayList<>();

    public WorkManager() {
        this.threadManager = ThreadManager.getInstance();
    }

    /*
     * In this method we just add the actual implementation of things which are commanded by ui.*/

    public void addTask(Task<Bitmap> task) {
        Log.i(TAG, "creating new worker");
        Worker<Bitmap> worker = new Worker<>(task);
        Log.i(TAG, "new worker created");
        Log.i(TAG, "starting worker");
        worker.subscribeOn(Schedulers.from(threadManager.getNetworkOperationThread()))
                .observeOn(Schedulers.from(threadManager.getMainThread()))
                .subscribe(new DomainRequestObserver<Bitmap>(task) {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        this.disposable = d;
                        workerList.add(d);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        disposable.dispose();
                    }
                });
    }
}
