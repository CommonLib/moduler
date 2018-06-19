package com.architecture.extend.architecture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by byang059 on 10/24/17.
 */
public class SecondActivity extends AppCompatActivity {

    @Inject
    View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        AndroidInjection.inject(this);

        if(mView != null){
            LogUtil.d("SecondActivity inject success" + this);
            LogUtil.d("SecondActivity inject success" + mView);
        }
    }
}
