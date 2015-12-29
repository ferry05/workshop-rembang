package org.meruvian.restsecurity.service;

/**
 * Created by merv on 6/6/15.
 */
public interface TaskService<R> {
    void onExecute(int code);

    void onSuccess(int code, R result);

    void onCancel(int code, String message);

    void onError(int code, String message);

}
