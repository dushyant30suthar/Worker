package com.dushyant.worker.framework.application;

import com.dushyant.worker.framework.viewmodelfactory.ServiceDomainProvider;
import com.dushyant.worker.framework.viewmodelfactory.ViewModelFactory;

import dagger.Module;
import dagger.Provides;

/*
 * This module provides factory class of viewmodel to create view model out of it as per screen.*/
@Module
public class ApplicationModule {

    @Provides
    public ViewModelFactory getViewModelFactory() {
        return new ViewModelFactory();
    }

    @Provides
    public ServiceDomainProvider getServiceModelFactory() {
        return new ServiceDomainProvider();
    }


}