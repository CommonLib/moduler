package com.architecture.extend.architecture;

import android.view.View;
import android.widget.Toast;

import com.architecture.extend.baselib.base.BaseActivity;
import com.architecture.extend.baselib.base.Configuration;
import com.architecture.extend.baselib.router.Router;
import com.module.contract.web.IWeb;

import io.reactivex.functions.Consumer;

@Configuration(layout = R.layout.activity_main, viewModel = MainViewModel.class)
public class MainActivity extends BaseActivity<MainContract.ViewModel>
        implements MainContract.View {

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
                getViewModel().getUserString().subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
