package com.dushyant.worker.framework.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThreadManager {
    private static ThreadManager threadManager;
    private Executor networkOperationThread;
    private Executor mainThread;

    /*
     * methods to dispatch tasks on demanded thread.*/

    private ThreadManager() {
        this.networkOperationThread = Executors.newSingleThreadExecutor();
        this.mainThread = new MainThreadExecutor();
    }

    public static ThreadManager getInstance() {
        if (threadManager == null) {
            threadManager = new ThreadManager();
        }
        return threadManager;
    }

    public Executor getNetworkOperationThread() {
        return networkOperationThread;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}

