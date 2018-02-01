package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.FragmentViewModel;
import com.architecture.extend.architecture.MainActivity;
import com.architecture.extend.architecture.MainViewModel;
import com.architecture.extend.architecture.SecondActivity;
import com.architecture.extend.baselib.dagger.ViewModelKey;
import com.architecture.extend.baselib.mvvm.BaseViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by byang059 on 1/31/18.
 */

@Module(subcomponents = {MainViewModelSubComponent.class,FragmentViewModelComponent.class
})
public abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract AndroidInjector.Factory<? extends BaseViewModel> bindMainViewModelInjectorFactory(
            MainViewModelSubComponent.Builder builder);

    @Binds
    @IntoMap
    @ViewModelKey(FragmentViewModel.class)
    abstract AndroidInjector.Factory<? extends BaseViewModel> bindFragmentViewModelInjectorFactory(
            FragmentViewModelComponent.Builder builder);

    @ContributesAndroidInjector
    abstract SecondActivity contributesSecondActivity();

    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();
}
