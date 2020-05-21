package com.dushyant.worker.framework.application;

import android.app.Application;

import com.dushyant.worker.framework.dagger.ClientComponent;
import com.dushyant.worker.framework.dagger.DaggerClientComponent;
import com.dushyant.worker.framework.network.NetworkModule;


public class WorkerApplication extends Application {

    protected static ClientComponent clientComponent;


    public static ClientComponent getClientComponent() {
        return clientComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setDataLayerComponent();
    }

    protected void setDataLayerComponent() {
        clientComponent = DaggerClientComponent.builder()
                .networkModule(new NetworkModule(this))
                .build();
    }

}
