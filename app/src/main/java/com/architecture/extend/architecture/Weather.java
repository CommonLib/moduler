package com.architecture.extend.architecture;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by byang059 on 11/21/17.
 */

@Entity
public class Weather {

    /**
     * date : 20171121
     * message : Success !
     * status : 200
     * city : 北京
     * count : 1
     * data : {"shidu":"38%","pm25":126,"pm10":159,"quality":"中度污染","wendu":"2","ganmao":"儿童、老年人及心脏、呼吸系统疾病患者人群应减少长时间或高强度户外锻炼，一般人群适量减少户外运动","yesterday":{"date":"20日星期一","sunrise":"07:03","high":"高温 8.0℃","low":"低温 -3.0℃","sunset":"16:55","aqi":92,"fx":"西南风","fl":"<3级","type":"晴","notice":"lovely sunshine，尽情享受阳光的温暖吧"},"forecast":[{"date":"21日星期二","sunrise":"07:04","high":"高温 8.0℃","low":"低温 -3.0℃","sunset":"16:55","aqi":244,"fx":"西南风","fl":"3-4级","type":"多云","notice":"今日多云，骑上单车去看看世界吧"},{"date":"22日星期三","sunrise":"07:06","high":"高温 7.0℃","low":"低温 -5.0℃","sunset":"16:54","aqi":57,"fx":"西北风","fl":"3-4级","type":"晴","notice":"天气干燥，请适当增加室内湿度"},{"date":"23日星期四","sunrise":"07:07","high":"高温 7.0℃","low":"低温 -4.0℃","sunset":"16:54","aqi":81,"fx":"南风","fl":"<3级","type":"晴","notice":"晴空万里，去沐浴阳光吧"},{"date":"24日星期五","sunrise":"07:08","high":"高温 7.0℃","low":"低温 -3.0℃","sunset":"16:53","aqi":119,"fx":"西南风","fl":"<3级","type":"晴","notice":"天气干燥，请适当增加室内湿度"},{"date":"25日星期六","sunrise":"07:09","high":"高温 8.0℃","low":"低温 -4.0℃","sunset":"16:53","aqi":145,"fx":"西北风","fl":"3-4级","type":"多云","notice":"绵绵的云朵，形状千变万化"}]}
     */
    @PrimaryKey
    private int id;
    private String date;
    private String message;
    private int status;
    private String city;
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class DataBean {
        /**
         * shidu : 38%
         * pm25 : 126.0
         * pm10 : 159.0
         * quality : 中度污染
         * wendu : 2
         * ganmao : 儿童、老年人及心脏、呼吸系统疾病患者人群应减少长时间或高强度户外锻炼，一般人群适量减少户外运动
         * yesterday : {"date":"20日星期一","sunrise":"07:03","high":"高温 8.0℃","low":"低温 -3.0℃","sunset":"16:55","aqi":92,"fx":"西南风","fl":"<3级","type":"晴","notice":"lovely sunshine，尽情享受阳光的温暖吧"}
         * forecast : [{"date":"21日星期二","sunrise":"07:04","high":"高温 8.0℃","low":"低温 -3.0℃","sunset":"16:55","aqi":244,"fx":"西南风","fl":"3-4级","type":"多云","notice":"今日多云，骑上单车去看看世界吧"},{"date":"22日星期三","sunrise":"07:06","high":"高温 7.0℃","low":"低温 -5.0℃","sunset":"16:54","aqi":57,"fx":"西北风","fl":"3-4级","type":"晴","notice":"天气干燥，请适当增加室内湿度"},{"date":"23日星期四","sunrise":"07:07","high":"高温 7.0℃","low":"低温 -4.0℃","sunset":"16:54","aqi":81,"fx":"南风","fl":"<3级","type":"晴","notice":"晴空万里，去沐浴阳光吧"},{"date":"24日星期五","sunrise":"07:08","high":"高温 7.0℃","low":"低温 -3.0℃","sunset":"16:53","aqi":119,"fx":"西南风","fl":"<3级","type":"晴","notice":"天气干燥，请适当增加室内湿度"},{"date":"25日星期六","sunrise":"07:09","high":"高温 8.0℃","low":"低温 -4.0℃","sunset":"16:53","aqi":145,"fx":"西北风","fl":"3-4级","type":"多云","notice":"绵绵的云朵，形状千变万化"}]
         */

        private String shidu;
        private double pm25;
        private double pm10;
        private String quality;
        private String wendu;
        private String ganmao;
        private YesterdayBean yesterday;
        private List<ForecastBean> forecast;

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

        public YesterdayBean getYesterday() {
            return yesterday;
        }

        public void setYesterday(YesterdayBean yesterday) {
            this.yesterday = yesterday;
        }

        public List<ForecastBean> getForecast() {
            return forecast;
        }

        public void setForecast(List<ForecastBean> forecast) {
            this.forecast = forecast;
        }

        public static class YesterdayBean {
            /**
             * date : 20日星期一
             * sunrise : 07:03
             * high : 高温 8.0℃
             * low : 低温 -3.0℃
             * sunset : 16:55
             * aqi : 92.0
             * fx : 西南风
             * fl : <3级
             * type : 晴
             * notice : lovely sunshine，尽情享受阳光的温暖吧
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
             * date : 21日星期二
             * sunrise : 07:04
             * high : 高温 8.0℃
             * low : 低温 -3.0℃
             * sunset : 16:55
             * aqi : 244.0
             * fx : 西南风
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
    }

    @Override
    public String toString() {
        return "Weather{" + "date='" + date + '\'' + ", message='" + message + '\'' + ", status="
                + status + ", city='" + city + '\'' + ", count=" + count + '}';
    }
}
