package com.architecture.extend.architecture;

import android.os.SystemClock;

import com.architecture.extend.baselib.mvvm.BaseModel;
import com.architecture.extend.baselib.mvvm.LiveData;

/**
 * Created by byang059 on 5/27/17.
 */

public class MainModel extends BaseModel<MainViewModel> {

    public LiveData<String> readDatabase(final String a, final String b) {

        final LiveData<String> liveData = new LiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                liveData.setValue(a + b);
            }
        }).start();
        return liveData;
    }
}
