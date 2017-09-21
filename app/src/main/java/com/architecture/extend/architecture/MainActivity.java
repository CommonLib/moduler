package com.architecture.extend.architecture;

import android.view.View;
import android.widget.Toast;

import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.mvvm.UiCallBack;
import com.architecture.extend.baselib.router.Router;
import com.architecture.extend.baselib.util.LogUtil;
import com.module.contract.web.IWeb;

public class MainActivity extends BaseActivity<MainViewModel> {

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
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
                //                IPic service = (IPic) Router.getInstance().service(IPic.class);
                //                service.playPic(MainActivity.this);
                getViewModel().getUserString()
                        .observe(MainActivity.this, new UiCallBack<String>() {
                            @Override
                            public void onDataReady(String s) {
                                LogUtil.d("ui onDataReady =>" + s);
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


}
