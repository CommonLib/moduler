package com.architecture.extend.baselib.mvvm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.architecture.extend.baselib.base.ShareDataViewModel;
import com.architecture.extend.baselib.util.GenericUtil;
import com.architecture.extend.baselib.util.PermissionAccessUtil;
import com.github.kayvannj.permission_utils.PermissionUtil;

import java.util.ArrayList;

/**
 * Created by byang059 on 12/19/16.
 */

public abstract class BaseActivity<VM extends BaseViewModel> extends AppCompatActivity
        implements ViewAble {

    private VM mViewModel;
    private boolean mIsForeground;
    private ViewForegroundSwitchListener mSwitchListener;
    private ArrayList<PermissionUtil.PermissionRequestObject> mPermissionRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Class<VM> viewModelClazz = GenericUtil.getGenericsSuperType(this, 0);
        mViewModel = ViewModelProviders.getInstance().get(viewModelClazz);
        mViewModel.onViewCreate();
        setForegroundSwitchCallBack(mViewModel);
        setContentView(getLayoutId());
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            handleIntent(intent);
        }
        if (savedInstanceState != null) {
            onRestoreInitData(savedInstanceState);
        }
        initData();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mIsForeground = true;
        mViewModel.onViewStart();
        mSwitchListener.onViewForeground();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onViewPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mViewModel.onViewRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsForeground = false;
        mViewModel.onViewStop();
        mSwitchListener.onViewBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onViewDestroy();
    }

    public VM getViewModel() {
        return mViewModel;
    }

    @Override
    public boolean isForeground() {
        return mIsForeground;
    }

    @Override
    public void setForegroundSwitchCallBack(ViewForegroundSwitchListener switchListener) {
        mSwitchListener = switchListener;
    }

    /**
     * for android verision above 23 to apply permission
     *
     * @param permission
     * @param callBack
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void usePermission(@RequiresPermission String permission,
                                 PermissionAccessUtil.PermissionCallBack callBack) {
        if (PermissionAccessUtil.hasPermission(this, permission)) {
            callBack.onGranted();
            return;
        }
        PermissionUtil.PermissionRequestObject permissionRequest = PermissionAccessUtil
                .requestPermission(this, permission, callBack);
        if (mPermissionRequests == null) {
            mPermissionRequests = new ArrayList<>();
        }
        mPermissionRequests.add(permissionRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (mPermissionRequests != null && mPermissionRequests.size() > 0) {
            for (PermissionUtil.PermissionRequestObject permissionRequest : mPermissionRequests) {
                permissionRequest
                        .onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            mPermissionRequests.clear();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected View inflate(@LayoutRes int id) {
        return inflate(id, null);
    }

    protected View inflate(@LayoutRes int id, @Nullable ViewGroup root) {
        return LayoutInflater.from(this).inflate(id, root);
    }

    protected Intent newIntent(Class<?> cls) {
        return new Intent(this, cls);
    }

    protected void startActivity(Class<?> cls) {
        startActivity(newIntent(cls));
    }

    protected void showToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    protected void showToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    protected DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    protected int getWidthPixels() {
        return getDisplayMetrics().widthPixels;
    }

    protected int getHeightPixels() {
        return getDisplayMetrics().heightPixels;
    }

    protected Object getSharedData(String key) {
        ShareDataViewModel shareDataViewModel = ViewModelProviders.getInstance()
                .get(ShareDataViewModel.class);
        return shareDataViewModel.take(key);
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected void handleIntent(@NonNull Intent intent) {
    }

    protected void onRestoreInitData(@NonNull Bundle savedInstanceState) {
    }
}
