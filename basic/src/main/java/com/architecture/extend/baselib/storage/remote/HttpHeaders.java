package com.architecture.extend.baselib.storage.remote;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by byang059 on 9/22/17.
 */

@Singleton
public class HttpHeaders {

    private HashMap<String, String> mHeaders;

    @Inject
    HttpHeaders() {
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void addHeader(String headerName, String headerValue) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }
        mHeaders.put(headerName, headerValue);
    }
}
