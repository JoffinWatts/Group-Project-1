package net.joffinwatts.companyz.callbacks;

public interface TodosPulledFromFirebaseCallback<T> {
    void callback(T data);
}
