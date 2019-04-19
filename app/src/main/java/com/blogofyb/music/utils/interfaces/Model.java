package com.blogofyb.music.utils.interfaces;

public interface Model<T> {

    void requestData(View<?> view, Callback<T> callback);

}
