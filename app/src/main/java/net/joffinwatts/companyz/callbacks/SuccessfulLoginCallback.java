package net.joffinwatts.companyz.callbacks;

public interface SuccessfulLoginCallback<T> {
    void callback(T data);
}
