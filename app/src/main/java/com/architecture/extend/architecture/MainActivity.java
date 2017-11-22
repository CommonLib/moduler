package com.architecture.extend.architecture;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.architecture.databinding.ShareLayoutBinding;
import com.architecture.extend.baselib.base.PermissionCallBack;
import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.mvvm.BaseDialog;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;
import com.architecture.extend.baselib.mvvm.Resource;
import com.architecture.extend.baselib.mvvm.ViewCreateCallBack;
import com.architecture.extend.baselib.util.AppUtil;
import com.architecture.extend.baselib.util.FragmentStack;
import com.architecture.extend.baselib.util.LogUtil;
import com.module.contract.router.RouterMaps;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainActivity extends BaseActivity<MainViewModel> {

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(ViewDataBinding binding) {
        findViewById(R.id.act_btn_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(RouterMaps.Page.PIC).navigation();
                //                startActivity(new Intent(MainActivity.this, Activity1.class));

            }
        });
        findViewById(R.id.act_btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseDialog.newInstance()
                        .setLayoutId(R.layout.share_layout, new ViewCreateCallBack() {
                            @Override
                            public void initView(ViewDataBinding dataBinding, BaseDialog dialog,
                                                 Bundle bundle) {
                                ShareLayoutBinding binding = (ShareLayoutBinding) dataBinding;
                                binding.wechat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(MainActivity.this, "wechat onclick",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setDimAmount(0.3f).setShowBottom(true).show(getSupportFragmentManager());
            }
        });
        findViewById(R.id.act_btn_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usePermission(new String[]{
                        Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE
                }, new PermissionCallBack() {
                    @Override
                    public void onGranted(String permission) {
                        LogUtil.d(permission + " onGranted");
                    }

                    @Override
                    public void onDenied(String permission) {
                        LogUtil.d(permission + " onDenied");
                    }
                });
                //                startActivity(new Intent(MainActivity.this, Activity1.class));
                getViewModel().getUserString().observe(MainActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtil.d("ui onChanged =>" + s);
                    }
                });

            }
        });

        FragmentStack fragmentStack = FragmentStack
                .create(getSupportFragmentManager(), R.id.act_fl_container);
        fragmentStack.push(new MainFragment(), null);

        LiveData<Resource<Weather>> resourceLiveData = getViewModel().getPullToRefresh();
        resourceLiveData.observe(this, weatherResource -> {
            if (weatherResource != null) {
                if (weatherResource.status == Resource.STATE_SUCCESS) {
                    getPullToRefreshView().refreshComplete();
                    LogUtil.d("ui STATE_SUCCESS");
                    if (weatherResource.data != null) {
                        TextView tv = findViewById(R.id.tv_hello_world);
                        tv.setText(weatherResource.data.toString());
                    }
                } else if (weatherResource.status == Resource.STATE_CACHE) {
                    LogUtil.d("ui STATE_CACHE");
                    if (weatherResource.data != null) {
                        TextView tv = findViewById(R.id.tv_hello_world);
                        tv.setText(weatherResource.data.toString());
                    }
                } else if (weatherResource.status == Resource.STATE_ERROR) {
                    LogUtil.d("ui STATE_ERROR");
                    Toast.makeText(MainActivity.this, "result error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public ConfigureInfo getConfigureInfo() {
        return new ConfigureInfo.Builder().asyncInflate(true).loadingState(true).pullToRefresh(true)
                .toolbar(true).build();
    }

    @Override
    public void onBackPressed() {
        AppUtil.startLauncherHome(this);
    }

    @Override
    protected void onPullRefreshBegin(PtrFrameLayout frame) {
        getViewModel().onPullToRefresh();
    }
}
