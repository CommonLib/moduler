package com.architecture.extend.architecture;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.architecture.extend.baselib.base.Bean;

import lombok.Data;

/**
 * Created by byang059 on 11/21/17.
 */

@Data
@Entity
public class Weather extends Bean{

    @PrimaryKey
    private int id;

    /**
     * shidu : 21%
     * pm25 : 11.0
     * pm10 : 91.0
     * quality : 良
     * wendu : 5
     * ganmao : 极少数敏感人群应减少户外活动
     * yesterday : {"date":"21日星期二","sunrise":"07:04","high":"高温 8.0℃","low":"低温 -2.0℃","sunset":"16:55","aqi":207,"fx":"西北风","fl":"3-4级","type":"多云","notice":"今日多云，骑上单车去看看世界吧"}
     * forecast : [{"date":"22日星期三","sunrise":"07:06","high":"高温 7.0℃","low":"低温 -5.0℃","sunset":"16:54","aqi":44,"fx":"西北风","fl":"3-4级","type":"多云","notice":"绵绵的云朵，形状千变万化"},{"date":"23日星期四","sunrise":"07:07","high":"高温 7.0℃","low":"低温 -4.0℃","sunset":"16:54","aqi":77,"fx":"西南风","fl":"<3级","type":"多云","notice":"今日多云，骑上单车去看看世界吧"},{"date":"24日星期五","sunrise":"07:08","high":"高温 7.0℃","low":"低温 -3.0℃","sunset":"16:53","aqi":66,"fx":"西风","fl":"<3级","type":"多云","notice":"今日多云，骑上单车去看看世界吧"},{"date":"25日星期六","sunrise":"07:09","high":"高温 8.0℃","low":"低温 -3.0℃","sunset":"16:53","aqi":95,"fx":"西北风","fl":"<3级","type":"多云","notice":"悠悠的云里有淡淡的诗"},{"date":"26日星期日","sunrise":"07:10","high":"高温 4.0℃","low":"低温 -4.0℃","sunset":"16:52","aqi":64,"fx":"东风","fl":"<3级","type":"晴","notice":"天气干燥，请适当增加室内湿度"}]
     */

    private String shidu;
    private double pm25;
    private double pm10;
    private String quality;
    private String wendu;
    private String ganmao;
//    private YesterdayBean yesterday;
//    private List<ForecastBean> forecast;

    @Data
    public static class YesterdayBean {
        /**
         * date : 21日星期二
         * sunrise : 07:04
         * high : 高温 8.0℃
         * low : 低温 -2.0℃
         * sunset : 16:55
         * aqi : 207.0
         * fx : 西北风
         * fl : 3-4级
         * type : 多云
         * notice : 今日多云，骑上单车去看看世界吧
         */

        private String date;
        private String sunrise;
        private String high;
        private String low;
        private String sunset;
        private double aqi;
        private String fx;
        private String fl;
        private String type;
        private String notice;
    }

    @Data
    public static class ForecastBean {
        /**
         * date : 22日星期三
         * sunrise : 07:06
         * high : 高温 7.0℃
         * low : 低温 -5.0℃
         * sunset : 16:54
         * aqi : 44.0
         * fx : 西北风
         * fl : 3-4级
         * type : 多云
         * notice : 绵绵的云朵，形状千变万化
         */

        private String date;
        private String sunrise;
        private String high;
        private String low;
        private String sunset;
        private double aqi;
        private String fx;
        private String fl;
        private String type;
        private String notice;
    }
}
