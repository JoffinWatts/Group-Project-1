package net.joffinwatts.companyz.callbacks;

public interface AccountCreatedCallback<T> {
    void callback(T data);
}
