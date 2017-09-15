package com.architecture.extend.architecture;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.architecture.extend.baselib.base.BaseActivity;
import com.architecture.extend.baselib.router.Router;
import com.module.contract.pic.IPic;
import com.module.contract.web.IWeb;

public class MainActivity extends BaseActivity<MainViewModel, MainContract.ViewModel>
        implements MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViewModel().getUserString().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        findViewById(R.id.act_btn_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IWeb service = (IWeb) Router.getInstance().service(IWeb.class);
                service.openWeb(MainActivity.this);
            }
        });
        findViewById(R.id.act_btn_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPic service = (IPic) Router.getInstance().service(IPic.class);
                service.playPic(MainActivity.this);
            }
        });
    }
}
