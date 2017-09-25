package com.architecture.extend.baselib.storage.remote;


import android.support.annotation.NonNull;

import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.util.AppUtil;
import com.architecture.extend.baselib.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author:dongpo 创建时间: 9/1/2016
 * 描述:
 * 修改:
 */
public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        boolean networkAvailable = AppUtil.isNetworkAvailable(BaseApplication.getInstance());
        if (!networkAvailable) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        } else {
            builder.addHeader("Accept", "application/json");
            HashMap<String, String> headers = RetrofitHelper.getInstance().getHeaders();
            if (headers != null) {
                for (String key : headers.keySet()) {
                    builder.addHeader(key, headers.get(key));
                }
            }
        }
        Request newRequest = builder.build();
        printRequestInfo(newRequest);
        //获取请求头中的缓存时间
        String cacheControl = request.cacheControl().toString();
        Response response = chain.proceed(newRequest);

        if (networkAvailable) {
            response.newBuilder().header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            response.newBuilder().header("Cache-Control", cacheControl).removeHeader("Pragma")
                    .build();
        }
        printResponseInfo(response);
        return response;
    }

    private void printRequestInfo(Request request) {
        if (LogUtil.DEBUG) {
            try {
                LogUtil.d("---------------------------Request Start---------------------------");
                LogUtil.d("method = " + request.method());
                LogUtil.d("url = " + request.url().toString());
                Headers headers = request.headers();
                for (int i = 0; i < headers.size(); i++) {
                    String name = headers.name(i);
                    String value = headers.get(name);
                    LogUtil.d("header: " + name + ", " + value);
                }
                if (request.body() != null) {
                    if (request.body().contentLength() > 102400) {
                        LogUtil.d("body: body is too long to print, ignore..");
                    } else {
                        LogUtil.d("body:" + request.body().toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.d("IOException:" + e.getMessage());
            }
            LogUtil.d("---------------------------Request End-----------------------------");
        }
    }

    private void printResponseInfo(Response response) {

        if (LogUtil.DEBUG) {
            try {
                LogUtil.d("---------------------------Response start---------------------------");
                LogUtil.d("code = " + response.code());
                LogUtil.d("url = " + response.request().url().toString());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    String name = headers.name(i);
                    String value = headers.get(name);
                    LogUtil.d("header: " + name + ", " + value);
                }
                if (response.body() != null) {
                    if (response.body().contentLength() > 102400) {
                        LogUtil.d("body: body is too long to print, ignore..");
                    } else {
                        LogUtil.d("body:" + response.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.d("IOException:" + e.getMessage());
            }
            LogUtil.d("---------------------------Response end-----------------------------");
        }
    }
}
