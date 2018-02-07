package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.FragmentViewModel;
import com.architecture.extend.architecture.MainActivity;
import com.architecture.extend.architecture.MainFragment;
import com.architecture.extend.architecture.MainRepository;
import com.architecture.extend.architecture.MainViewModel;
import com.architecture.extend.architecture.SecondActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by byang059 on 1/31/18.
 */

@Module
public abstract class MainModule {

    @ContributesAndroidInjector
    abstract MainViewModel contributesMainViewModel();

    @ContributesAndroidInjector
    abstract FragmentViewModel contributesFragmentViewModel();

    @ContributesAndroidInjector
    abstract MainRepository contributesMainRepository();

    @ContributesAndroidInjector
    abstract SecondActivity contributesSecondActivity();

    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();

    @ContributesAndroidInjector
    abstract MainFragment contributesMainFragment();
}
