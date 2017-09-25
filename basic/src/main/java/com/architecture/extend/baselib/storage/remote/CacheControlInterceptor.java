package com.architecture.extend.baselib.storage.remote;

import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.util.AppUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author:dongpo 创建时间: 9/1/2016
 * 描述: no matter
 * 修改:
 */
public class CacheControlInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        boolean hasInternet = AppUtil.isNetworkAvailable(BaseApplication.getInstance());
        if (!hasInternet) {
            //network is not work, we will use force cache
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);

        if (hasInternet) {
            //this is real response from server, use user configure time, otherwise use default
            String cacheControl = request.cacheControl().toString();
            response = response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            //this is force cache response
            response = response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=0")
                    .removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
