package com.architecture.extend.architecture;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.architecture.extend.architecture.databinding.ShareLayoutBinding;
import com.architecture.extend.baselib.aop.DebugLog;
import com.architecture.extend.baselib.aop.NeedPermission;
import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.mvvm.BaseDialog;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;
import com.architecture.extend.baselib.mvvm.Resource;
import com.architecture.extend.baselib.mvvm.ViewCreateCallBack;
import com.architecture.extend.baselib.util.FragmentStack;
import com.architecture.extend.baselib.util.LogUtil;

import javax.inject.Inject;
import javax.inject.Named;

import in.srain.cube.views.ptr.PtrFrameLayout;

public class MainActivity extends BaseActivity<MainViewModel> {

    @Inject
    MainRepository mMainRepository;

    @Inject
    Handler mHandler;

    @Inject
    @Named("launcher")
    Intent mLauncher;

    @Override
    protected void initData() {
        if (mHandler != null) {
            LogUtil.d("MainActivity inject success" + mHandler);
        }
        LogUtil.d("mMainRepository inject success" + mMainRepository.mMainApiService);
    }

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @DebugLog
    @Override
    protected void initView(ViewDataBinding binding) {
        findViewById(R.id.act_btn_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                ARouter.getInstance().build(RouterConstants.Pic.PAGE_PIC).navigation();
                startActivity(new Intent(MainActivity.this, SecondActivity.class));

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
                onClickPicBtn(v);
            }
        });

        FragmentStack fragmentStack = FragmentStack
                .create(getSupportFragmentManager(), R.id.act_fl_container);
        fragmentStack.push(new MainFragment(), null);

        LiveData<Resource<Weather>> resourceLiveData = getViewModel().getPullToRefresh();
        resourceLiveData.observe(this, new Observer<Resource<Weather>>() {
            @Override
            public void onChanged(@Nullable Resource<Weather> weatherResource) {
                if (weatherResource != null) {
                    if (weatherResource.isSuccess()) {
                        getPullToRefreshView().refreshComplete();
                        LogUtil.d("ui STATE_SUCCESS");
                        if (weatherResource.data != null) {
                            TextView tv = findViewById(R.id.tv_hello_world);
                            tv.setText(weatherResource.data.toString());
                        }
                    } else if (weatherResource.isCache()) {
                        LogUtil.d("ui STATE_CACHE");
                        if (weatherResource.data != null) {
                            TextView tv = findViewById(R.id.tv_hello_world);
                            tv.setText(weatherResource.data.toString());
                        }
                    } else if (weatherResource.isError()) {
                        getPullToRefreshView().refreshComplete();
                        LogUtil.d("ui STATE_ERROR");
                        Toast.makeText(MainActivity.this, weatherResource.message,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @NeedPermission(permission = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE})
    private void onClickPicBtn(View v) {
        LogUtil.d("onGranted");
        //startActivity(new Intent(MainActivity.this, SecondActivity.class));
        getViewModel().getUserString("abc","bcd").observe(MainActivity.this, new
                Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtil.d("ui onChanged =>" + s);
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
        startActivity(mLauncher);
    }

    @Override
    protected void onPullRefreshBegin(PtrFrameLayout frame) {
        getViewModel().onPullToRefresh();
    }
}
