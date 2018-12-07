package com.architecture.extend.baselib.mvvm;

import lombok.Builder;
import lombok.Data;

/**
 * Created by byang059 on 9/25/17.
 */

@Data
@Builder
public class ConfigureInfo {
    private boolean asyncInflate;
    private boolean toolbar;
    private Boolean isToolbarShow;
    private boolean pullToRefresh;
    private boolean loadingState;

    public static ConfigureInfo defaultConfigure(){
        return ConfigureInfo.builder().asyncInflate(true).build();
    }
}
