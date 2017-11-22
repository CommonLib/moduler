package com.architecture.extend.architecture;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by byang059 on 11/21/17.
 */

@Dao
public interface WeatherDao {
    @Insert(onConflict = REPLACE)
    void save(Weather weather);
    @Query("SELECT * FROM weather WHERE city = :city")
    LiveData<Weather> load(String city);
}
