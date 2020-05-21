package com.dushyant.worker.client.foregroundcomponents.imageComponent;

import androidx.lifecycle.ViewModel;

import com.dushyant.worker.domain.galaxy.WorkManager;

public class ImageViewModel extends ViewModel {
    WorkManager workManager;

    public ImageViewModel(WorkManager workManager) {
        this.workManager = workManager;
    }


}
