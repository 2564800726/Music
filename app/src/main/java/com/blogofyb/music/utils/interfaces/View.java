package com.blogofyb.music.utils.interfaces;

public interface View<T> {

    void onSuccess(T data);

    void onFailure();

}
