package com.architecture.extend.architecture;

import android.Manifest;
import android.view.View;
import android.widget.Toast;

import com.architecture.extend.baselib.base.PermissionCallBack;
import com.architecture.extend.baselib.mvvm.BaseActivity;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;
import com.architecture.extend.baselib.mvvm.LiveCallBack;
import com.architecture.extend.baselib.router.Router;
import com.architecture.extend.baselib.util.AppUtil;
import com.architecture.extend.baselib.util.FragmentStack;
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
                getViewModel().getUserString().subscribe(MainActivity.this, new LiveCallBack<String>() {

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
                });
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
        return new ConfigureInfo.Builder().asyncInflate(true).loadingState(true).pullToRefresh
                (false).toolbar(true).build();
    }

    @Override
    public void onBackPressed() {
        AppUtil.startLauncherHome(this);
    }
}
