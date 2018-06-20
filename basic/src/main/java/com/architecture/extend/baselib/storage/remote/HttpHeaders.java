package com.architecture.extend.baselib.storage.remote;

import com.architecture.extend.baselib.dagger.ApplicationScope;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by byang059 on 9/22/17.
 */

@ApplicationScope
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
