package com.dushyant.worker.domain;

import com.dushyant.worker.client.foregroundcomponents.imageComponent.Task;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/*
 * Generic observer observing DomainRequestObservable and delivers response from RxObservable to LiveData
 * which is being observed by ui.*/

public class DomainRequestObserver<ResultType> implements Observer<Task<ResultType>> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Task<ResultType> task) {
        task.onTaskComplete(task.getResult());
    }

    @Override
    public void onError(Throwable e) {

        DomainRequestError domainRequestError;

        try {

            domainRequestError = ((DomainRequestErrorException) e).getDomainRequestError();

            //task.onTaskError(DomainRequest.failed(domainRequestError, null));

        } catch (Exception ex) {

            domainRequestError = new DomainRequestError();
            domainRequestError.setErrorMessage(e.getMessage());

            //task.onTaskError(DomainRequest.failed(domainRequestError, null));
        }
    }

    @Override
    public void onComplete() {

    }
}
