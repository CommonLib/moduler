package com.architecture.extend.architecture;

import io.reactivex.Flowable;

/**
 * Created by byang059 on 5/27/17.
 */

public interface MainContract {
    interface View {

    }

    interface ViewModel {
        Flowable<String> getUserString();
    }

    interface Model {
        String readDatabase(String a, String b);
    }
}
