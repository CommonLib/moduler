package com.architecture.extend.architecture;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.architecture.extend.baselib.base.Bean;

/**
 * Created by byang059 on 11/21/17.
 */

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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public double getPm25() {
        return pm25;
    }

    public void setPm25(double pm25) {
        this.pm25 = pm25;
    }

    public double getPm10() {
        return pm10;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }


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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public double getAqi() {
            return aqi;
        }

        public void setAqi(double aqi) {
            this.aqi = aqi;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }
    }

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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public double getAqi() {
            return aqi;
        }

        public void setAqi(double aqi) {
            this.aqi = aqi;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }
    }
}
