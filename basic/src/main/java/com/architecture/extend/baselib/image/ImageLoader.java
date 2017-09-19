package com.architecture.extend.baselib.image;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

/**
 * Created by byang059 on 9/19/17.
 */

public class ImageLoader {

    public static void fromUrl(Context context, final String url, final ImageCallBack callBack) {
        Glide.with(context).load(url).into(new SimpleTarget<GlideDrawable>() {

            @Override
            public void onResourceReady(GlideDrawable resource,
                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                callBack.onBitmapLoaded(url, resource);
            }
        });
    }

    public static void fromUrl(Context context, final String url, final ImageCallBack callBack,
                               @IdRes int placeHolder, @IdRes int errorHolder) {
        Glide.with(context).load(url).placeholder(placeHolder).crossFade().error(errorHolder)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        callBack.onBitmapLoaded(url, resource);
                    }
                });
    }

    public static void fromUrl(Context context, final String url, final ImageCallBack callBack,
                               @IdRes int placeHolder, @IdRes int errorHolder, float thumbnail) {
        Glide.with(context).load(url).placeholder(placeHolder).crossFade().error(errorHolder)
                .thumbnail(thumbnail).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource,
                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                callBack.onBitmapLoaded(url, resource);
            }
        });
    }

    public static void fromFile(Context context, final File file, ImageView imageView) {
        Glide.with(context).load(file).into(imageView);
    }

    public static void fromFile(Context context, @IdRes int resourceId, ImageView imageView) {
        Glide.with(context).load(resourceId).into(imageView);
    }

    public static void fromUri(Context context, Uri uri, ImageView imageView) {
        Glide.with(context).load(uri).into(imageView);
    }
}
