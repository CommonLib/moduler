package com.architecture.extend.architecture.dagger;

import com.architecture.extend.architecture.FragmentViewModel;
import com.architecture.extend.architecture.MainActivity;
import com.architecture.extend.architecture.MainRepository;
import com.architecture.extend.architecture.MainViewModel;
import com.architecture.extend.architecture.SecondActivity;
import com.architecture.extend.baselib.dagger.ObjectInjectionModule;
import com.architecture.extend.baselib.dagger.ObjectKey;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by byang059 on 1/31/18.
 */

@Module(subcomponents = {
        MainModule.MainViewModelSubComponent.class, MainModule.FragmentViewModelSubComponent.class, MainModule.MainRepositorySubComponent.class
})
public abstract class MainModule {

    @Binds
    @IntoMap
    @ObjectKey(MainViewModel.class)
    abstract AndroidInjector.Factory<? extends Object> bindMainViewModelInjectorFactory(
            MainViewModelSubComponent.Builder builder);

    @Subcomponent(modules = {ObjectInjectionModule.class})
    interface MainViewModelSubComponent extends AndroidInjector<MainViewModel> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<MainViewModel> {}
    }


    @Binds
    @IntoMap
    @ObjectKey(FragmentViewModel.class)
    abstract AndroidInjector.Factory<? extends Object> bindFragmentViewModelInjectorFactory(
            FragmentViewModelSubComponent.Builder builder);

    @Subcomponent(modules = {ObjectInjectionModule.class})
    interface FragmentViewModelSubComponent extends AndroidInjector<FragmentViewModel> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<FragmentViewModel> {}
    }


    @Binds
    @IntoMap
    @ObjectKey(MainRepository.class)
    abstract AndroidInjector.Factory<? extends Object> bindMainRepositoryInjectorFactory(
            MainRepositorySubComponent.Builder builder);

    @Subcomponent(modules = {ObjectInjectionModule.class})
    interface MainRepositorySubComponent extends AndroidInjector<MainRepository> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<MainRepository> {}
    }


    @ContributesAndroidInjector
    abstract SecondActivity contributesSecondActivity();

    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();
}
