package com.architecture.extend.baselib.storage.remote;


import android.support.annotation.NonNull;

import com.architecture.extend.baselib.dagger.ApplicationScope;
import com.blankj.utilcode.util.NetworkUtils;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author:dongpo 创建时间: 9/1/2016
 * 描述:
 * 修改:
 */
@ApplicationScope
public class HeaderInterceptor implements Interceptor {

    HttpHeaders mHeaders;

    @Inject
    HeaderInterceptor(HttpHeaders headers) {
        mHeaders = headers;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        boolean networkAvailable = NetworkUtils.isConnected();
        if (!networkAvailable) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        } else {
            builder.addHeader("Accept", "application/json");
            HashMap<String, String> headers = mHeaders.getHeaders();
            if (headers != null) {
                for (String key : headers.keySet()) {
                    builder.addHeader(key, headers.get(key));
                }
            }
        }

        Object tag = request.tag();
        RequestBody body = request.body();
        if (tag instanceof RetrofitCallBack && body != null) {
            String method = request.method();
            builder.method(method, new ProgressRequestBody(body, (RetrofitCallBack) tag));
        }

        Request newRequest = builder.build();
        //获取请求头中的缓存时间
        String cacheControl = request.cacheControl().toString();
        Response response = chain.proceed(newRequest);
        Response.Builder responseBuilder = response.newBuilder();

        if (tag instanceof RetrofitCallBack) {
            responseBuilder.body(new ProgressResponseBody(response.body(), (RetrofitCallBack) tag));
        }

        if (networkAvailable) {
            response = responseBuilder.header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            response = responseBuilder.header("Cache-Control", cacheControl).removeHeader("Pragma")
                    .build();
        }
        return response;
    }
}
