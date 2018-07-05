package com.architecture.extend.architecture.dagger;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.architecture.extend.architecture.FragmentViewModel;
import com.architecture.extend.architecture.MainActivity;
import com.architecture.extend.architecture.MainFragment;
import com.architecture.extend.architecture.MainRepository;
import com.architecture.extend.architecture.MainViewModel;
import com.architecture.extend.architecture.SecondActivity;
import com.architecture.extend.architecture.SecondRepository;
import com.architecture.extend.architecture.SecondViewModel;
import com.architecture.extend.baselib.dagger.ActivityScope;
import com.architecture.extend.baselib.dagger.Injector;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by byang059 on 2018/6/20.
 */

@Module(subcomponents = {
        SubComponentModule.SecondActivitySubcomponent.class,
        SubComponentModule.MainActivitySubcomponent.class,
        SubComponentModule.MainFragmentSubcomponent.class
})
public abstract class SubComponentModule {

    @Binds
    @IntoMap
    @ActivityKey(SecondActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindSecondActivityInjectorFactory(
            SecondActivitySubcomponent.Builder builder);

    @Subcomponent(modules = SecondActivityModule.class)
    @ActivityScope
    public interface SecondActivitySubcomponent
            extends Injector<SecondActivity, SecondViewModel, SecondRepository> {

        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<SecondActivity> {}
    }

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(
            SubComponentModule.MainActivitySubcomponent.Builder builder);

    @Subcomponent
    @ActivityScope
    public interface MainActivitySubcomponent
            extends Injector<MainActivity, MainViewModel, MainRepository> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
    }

    @Binds
    @IntoMap
    @FragmentKey(MainFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMainFragmentInjectorFactory(
            SubComponentModule.MainFragmentSubcomponent.Builder builder);

    @Subcomponent
    @ActivityScope
    public interface MainFragmentSubcomponent
            extends Injector<MainFragment, FragmentViewModel, Object> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<MainFragment> {}
    }
}
