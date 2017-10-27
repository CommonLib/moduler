package com.architecture.extend.baselib.mvvm;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by byang059 on 10/25/17.
 */

public abstract class ViewCreateCallBack implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ViewCreateCallBack() {
    }

    protected ViewCreateCallBack(Parcel in) {
    }

    public abstract void initView(ViewDataBinding dataBinding, BaseDialog dialog, Bundle bundle);

    public static final Creator<ViewCreateCallBack> CREATOR = new Creator<ViewCreateCallBack>() {
        @Override
        public ViewCreateCallBack createFromParcel(Parcel source) {
            return new ViewCreateCallBack(source) {
                @Override
                public void initView(ViewDataBinding dataBinding, BaseDialog dialog,
                                     Bundle bundle) {

                }
            };
        }

        @Override
        public ViewCreateCallBack[] newArray(int size) {
            return new ViewCreateCallBack[size];
        }
    };
}
