package com.architecture.extend.architecture;

import com.architecture.extend.baselib.mvvm.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by byang059 on 11/21/17.
 */

public interface MainApiService{
    @GET("open/api/weather/json.shtml")
    Call<ApiResponse<Weather>> getWeather(@Query("city")String city);
}
