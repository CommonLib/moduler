package com.architecture.extend.baselib.mvvm;

/**
 * Created by byang059 on 9/25/17.
 */

public class ConfigureInfo {
    private boolean asyncInflate;
    private Boolean toolbar;
    private boolean pullToRefresh;
    private boolean loadingState;

    private ConfigureInfo(Builder builder) {
        this.asyncInflate = builder.asyncInflate;
        this.toolbar = builder.toolbar;
        this.pullToRefresh = builder.pullToRefresh;
        this.loadingState = builder.loadingState;
    }

    public boolean isAsyncInflate() {
        return asyncInflate;
    }

    public Boolean isEnableToolbar() {
        return toolbar;
    }

    public boolean isPullToRefresh() {
        return pullToRefresh;
    }

    public boolean isLoadingState() {
        return loadingState;
    }

    public static ConfigureInfo defaultConfigure(){
        return new Builder().build();
    }

    public static class Builder {
        private boolean asyncInflate;
        private Boolean toolbar;
        private boolean pullToRefresh;
        private boolean loadingState;

        public Builder asyncInflate(boolean asyncInflate) {
            this.asyncInflate = asyncInflate;
            return this;
        }

        public Builder toolbar(boolean toolbar) {
            this.toolbar = toolbar;
            return this;
        }

        public Builder pullToRefresh(boolean pullToRefresh) {
            this.pullToRefresh = pullToRefresh;
            return this;
        }

        public Builder loadingState(boolean loadingState) {
            this.loadingState = loadingState;
            return this;
        }

        public ConfigureInfo build() {
            return new ConfigureInfo(this);
        }
    }
}
