package com.architecture.extend.baselib.image;

import android.graphics.drawable.Drawable;

/**
 * Created by byang059 on 9/19/17.
 */

public interface ImageCallBack {
    void onBitmapLoaded(String url, Drawable drawable);
}
