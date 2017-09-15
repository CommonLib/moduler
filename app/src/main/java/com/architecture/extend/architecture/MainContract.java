package com.architecture.extend.architecture;

import android.arch.lifecycle.LiveData;

/**
 * Created by byang059 on 5/27/17.
 */

public interface MainContract {
    interface View{

    }

    interface ViewModel{
        LiveData<String> getUserString();
    }

    interface Model{

        LiveData<String> readDatabase(String a, String b);
    }
}
