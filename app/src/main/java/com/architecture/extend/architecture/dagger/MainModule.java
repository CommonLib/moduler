package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.ActivityModule;
import com.architecture.extend.architecture.FragmentViewModel;
import com.architecture.extend.architecture.MainActivity;
import com.architecture.extend.architecture.MainFragment;
import com.architecture.extend.architecture.MainRepository;
import com.architecture.extend.architecture.MainViewModel;
import com.architecture.extend.architecture.SecondActivity;
import com.architecture.extend.architecture.ViewModel1;
import com.architecture.extend.baselib.dagger.ActicityScope;
import com.architecture.extend.baselib.dagger.BaseApplicationModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by byang059 on 1/31/18.
 */

@Module(includes = BaseApplicationModule.class)
public abstract class MainModule {

    @ContributesAndroidInjector
    abstract MainViewModel contributesMainViewModel();

    @ActicityScope
    @ContributesAndroidInjector
    abstract FragmentViewModel contributesFragmentViewModel();

    @ActicityScope
    @ContributesAndroidInjector
    abstract MainRepository contributesMainRepository();

    @ActicityScope
    @ContributesAndroidInjector(modules = ActivityModule.class)
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
