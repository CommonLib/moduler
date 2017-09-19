package com.reference.apublic.web;

import android.os.Bundle;
import android.widget.Toast;

import com.architecture.extend.baselib.base.BaseActivity;
import com.architecture.extend.baselib.base.ViewModel;


/**
 * Created by byang059 on 9/15/17.
 */

@ViewModel(viewModel = WebViewModel.class)
public class WebActivity extends BaseActivity<WebContract> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity_main);
        String abc = (String) getSharedData("abc");
        Toast.makeText(this, abc, Toast.LENGTH_LONG).show();
    }
}
