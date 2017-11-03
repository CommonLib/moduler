package com.architecture.extend.architecture;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.architecture.extend.architecture.databinding.ShareLayoutBinding;
import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.mvvm.BaseDialog;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;
import com.architecture.extend.baselib.mvvm.ViewCreateCallBack;
import com.architecture.extend.baselib.util.AppUtil;
import com.architecture.extend.baselib.util.FragmentStack;
import com.module.contract.router.RouterMaps;

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
                /*getViewModel().getUserString()
                        .subscribe(MainActivity.this, new LiveCallBack<String>() {

                            @Override
                            public void onStart() {
                                super.onStart();
                                LogUtil.d("ui onStart =>");
                            }

                            @Override
                            public void onProgressUpdate(int progress) {
                                super.onProgressUpdate(progress);
                                LogUtil.d("ui onProgressUpdate =>" + progress);
                            }

                            @Override
                            public void onCacheReturn(String s) {
                                super.onCacheReturn(s);
                                LogUtil.d("ui onCacheReturn =>" + s);
                            }

                            @Override
                            public void onError(Throwable t) {
                                super.onError(t);
                                LogUtil.d("ui onError => " + t);
                            }

                            @Override
                            public void onComplete(String s) {
                                LogUtil.d("ui onComplete =>" + s);
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                            }
                        });
                usePermission(Manifest.permission.READ_CONTACTS, new PermissionCallBack() {
                    @Override
                    public void onGranted(String permission) {
                        LogUtil.d("onGranted");
                    }

                    @Override
                    public void onDenied(String permission) {
                        LogUtil.d("onDenied");
                    }
                });*/
                startActivity(new Intent(MainActivity.this, Activity1.class));
            }
        });

        FragmentStack fragmentStack = FragmentStack
                .create(getSupportFragmentManager(), R.id.act_fl_container);
        fragmentStack.push(new MainFragment(), null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public ConfigureInfo getConfigureInfo() {
        return new ConfigureInfo.Builder().asyncInflate(true).loadingState(true)
                .pullToRefresh(false).toolbar(true).build();
    }

    @Override
    public void onBackPressed() {
        AppUtil.startLauncherHome(this);
    }
}
