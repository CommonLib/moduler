package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.FragmentViewModel;
import com.architecture.extend.architecture.MainActivity;
import com.architecture.extend.architecture.MainFragment;
import com.architecture.extend.architecture.MainRepository;
import com.architecture.extend.architecture.MainViewModel;
import com.architecture.extend.architecture.SecondActivity;
import com.architecture.extend.architecture.SecondActivityModule;
import com.architecture.extend.architecture.ViewModel1;
import com.architecture.extend.baselib.dagger.ActicityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by byang059 on 2018/6/20.
 */

@Module
public abstract class SubComponentModule {

    @ContributesAndroidInjector
    abstract MainViewModel contributesMainViewModel();

    @ActicityScope
    @ContributesAndroidInjector
    abstract FragmentViewModel contributesFragmentViewModel();

    @ActicityScope
    @ContributesAndroidInjector
    abstract MainRepository contributesMainRepository();

    @ActicityScope
    @ContributesAndroidInjector(modules = SecondActivityModule.class)
    abstract SecondActivity contributesSecondActivity();

    @ActicityScope
    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();

    @ActicityScope
    @ContributesAndroidInjector
    abstract MainFragment contributesMainFragment();

    @ActicityScope
    @ContributesAndroidInjector
    abstract ViewModel1 contributesViewModel1();
}
