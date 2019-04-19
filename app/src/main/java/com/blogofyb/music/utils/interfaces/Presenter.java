package com.blogofyb.music.utils.interfaces;

public interface Presenter<T> {

    void attached(View<T> view);

    void detached();

    boolean isAttached();

    void getData();

}
