package com.architecture.extend.baselib.image;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by byang059 on 9/19/17.
 */

public class ImageLoader {

    public static void fromUrl(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    public static void fromUrl(Context context, String url, ImageView imageView,
                               @IdRes int placeHolder, @IdRes int errorHolder) {
        Glide.with(context).load(url).placeholder(placeHolder).crossFade().error(errorHolder)
                .into(imageView);
    }

    public static void fromUrl(Context context, String url, ImageView imageView,
                               @IdRes int placeHolder, @IdRes int errorHolder, float thumbnail) {
        Glide.with(context).load(url).placeholder(placeHolder).crossFade().error(errorHolder)
                .thumbnail(thumbnail).into(imageView);
    }

    public static void fromFile(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file).into(imageView);
    }

    public static void fromFile(Context context, @IdRes int resourceId, ImageView imageView) {
        Glide.with(context).load(resourceId).into(imageView);
    }

    public static void fromUri(Context context, Uri uri, ImageView imageView) {
        Glide.with(context).load(uri).into(imageView);
    }
}
