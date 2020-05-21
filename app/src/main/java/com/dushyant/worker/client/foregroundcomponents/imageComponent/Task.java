package com.dushyant.worker.client.foregroundcomponents.imageComponent;

public interface Task<T> {

    T onExecuteTask();

    void onTaskComplete(T result);

    String getTaskName();

    void setTaskName(String taskName);

    T getResult();
}
