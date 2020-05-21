package com.dushyant.worker.framework.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dushyant.worker.domain.image.datasources.ImageNetworkDao;
import com.dushyant.worker.framework.application.WorkerApplication;
import com.dushyant.worker.framework.utils.ThreadManager;

import javax.inject.Inject;


public class ViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    ImageNetworkDao imageNetworkDao;
    @Inject
    ThreadManager threadManager;


    /*
     * Here we prepare repositories of all of the units for example galaxy is unit, in future there can be another unit which could be used by one
     * or many ui components. This is personal preference. There are other ways in which each ui has it's own repository.
     *
     * There should not be any other way we be instanciating repositories other than this class. If there is service which needs to have repository then it also
     * must have some class which mimics viewModel for service. */
    public ViewModelFactory() {

        WorkerApplication.getClientComponent().doInjection(this);


        //  this.workManager = new WorkManager(imageNetworkDao, threadManager);

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass) {
        /*
         * Here we can create various view Models.*/

        return null;
    }
}