package com.dushyant.worker.framework.dagger;

import com.dushyant.worker.framework.network.NetworkModule;
import com.dushyant.worker.framework.utils.ThreadManager;
import com.dushyant.worker.framework.viewmodelfactory.ServiceDomainProvider;
import com.dushyant.worker.framework.viewmodelfactory.ViewModelFactory;

import dagger.Component;

/*
 * Dagger component which is expected to provide network and database clients to viewModel factories.*/
@Component(modules = {NetworkModule.class, ThreadManager.class})
public interface ClientComponent {

    void doInjection(ViewModelFactory viewModelFactory);

    void doInjection(ServiceDomainProvider serviceDomainProvider);
}

