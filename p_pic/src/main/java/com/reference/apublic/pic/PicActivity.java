package com.reference.apublic.pic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.module.contract.router.RouterConstant;

/**
 * Created by byang059 on 9/15/17.
 */

@Route(path = RouterConstant.Pic.PAGE_PIC)
public class PicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic);
    }
}
