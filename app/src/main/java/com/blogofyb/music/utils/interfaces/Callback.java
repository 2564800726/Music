package com.blogofyb.music.utils.interfaces;

public interface Callback<T> {

    void onSuccess(T data);

    void onFailure(Exception e);

    void onCompleted();

    void onStart();

}
