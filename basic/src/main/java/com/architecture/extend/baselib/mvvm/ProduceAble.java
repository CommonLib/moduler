package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 9/21/17.
 */

public interface ProduceAble<T> {
    void produce(LiveData<T> liveData);
}
