package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 9/21/17.
 */

public interface ProduceAble<T> {
    T produce(LiveData<T> liveData);
}
