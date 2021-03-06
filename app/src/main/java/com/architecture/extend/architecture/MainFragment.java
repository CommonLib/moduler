package com.architecture.extend.architecture;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;

import com.architecture.extend.architecture.databinding.FragmentMainBinding;
import com.architecture.extend.architecture.databinding.ItemLoadMoreBinding;
import com.architecture.extend.baselib.BaseApplication;
import com.architecture.extend.baselib.base.BaseRecycleAdapter;
import com.architecture.extend.baselib.base.ViewHolder;
import com.architecture.extend.baselib.mvvm.BaseFragment;
import com.architecture.extend.baselib.mvvm.ConfigureInfo;
import com.architecture.extend.baselib.util.LogUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by byang059 on 9/27/17.
 */

public class MainFragment extends BaseFragment<FragmentViewModel> {

    private BaseRecycleAdapter mAdapter;
    private List<String> mStrs;

    @Inject
    ConfigureInfo mConfigureInfo;

    @Override
    public void initData() {
        mConfigureInfo.toString();
    }

    @Override
    public void initView(ViewDataBinding binding) {
        FragmentMainBinding bind = (FragmentMainBinding) binding;
        mStrs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mStrs.add(i + " item");
        }
        mAdapter = new BaseRecycleAdapter(mStrs, R.layout.item_load_more) {
            @Override
            protected void onBindViewData(ViewHolder holder, int userPosition, int viewType) {
                if (viewType == R.layout.item_load_more) {
                    List<String> data = getData(R.layout.item_load_more);
                    String value = data.get(userPosition);
                    ItemLoadMoreBinding bind = (ItemLoadMoreBinding) holder.getBinding();
                    bind.itemLoadText.setText(value);
                }
            }
        };


        final LinearLayoutManager manager = new LinearLayoutManager(getBindActivity(),
                LinearLayoutManager.VERTICAL, false);
        ViewDataBinding loadBinding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.view_load_more, null, false);
        bind.viewScrollContent.setAdapter(mAdapter);
        bind.viewScrollContent.setLayoutManager(manager);
        bind.viewScrollContent.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                LogUtil.d("onLoadMore");
                BaseApplication.getInstance().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> str = new ArrayList<String>();
                        for (int i = 0; i < 10; i++) {
                            str.add("load more item" + i);
                        }
                        mAdapter.addData(str, R.layout.item_load_more);
                    }
                }, 3000);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public ConfigureInfo getConfigureInfo() {
        return ConfigureInfo.builder().asyncInflate(true).loadingState(true)
                .pullToRefresh(false).toolbar(true).isToolbarShow(true).build();
    }

    @Override
    public Class<FragmentViewModel> getViewModelClass() {
        return FragmentViewModel.class;
    }
}
