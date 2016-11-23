package com.ql.weather.https;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ql.weather.model.WeatherBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 作者 :QinLi
 * @version 创建时间：2016-9-26 下午3:54:13 类说明
 */
public class Https {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public static final String APPKEY_WEATHER = "81fd3b9562af019c37458fdf03e1b7cd";

    /**
     * @param context
     * @param handler
     */
    public static void getRequestForWeather(Context context, Handler handler,
                                            String city) {
        final Message msg = new Message();
        msg.what = HttpConst.MSG_WHAT_WEATHER;
        List<WeatherBean> list = new ArrayList<WeatherBean>();
        WeatherBean bean;
        msg.obj = null;
        String result = null;
        String ResultStr = null;
        String url = "http://op.juhe.cn/onebox/weather/query";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("cityname", city);//要查询的城市，如：温州、上海、北京
        params.put("key", APPKEY_WEATHER);//应用APPKEY(应用详细页查询)
        params.put("dtype", "");//返回数据的格式,xml或json，默认json
        try {
            ResultStr = "success";
            result = net(url, params, "GET");
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                JSONObject secondObj = object.getJSONObject("result");
                JSONObject thirdObj = secondObj.getJSONObject("data");
                JSONArray firstArray = thirdObj.getJSONArray("weather");
               JSONObject oo = thirdObj.getJSONObject("realtime");
               JSONObject finalObj = oo.getJSONObject("weather");
                for (int i = 0; i < firstArray.length(); i++) {
                    bean = new WeatherBean();
                    JSONObject obj = (JSONObject) firstArray.get(i);
                    bean.setTime(oo.getString("time"));
                    bean.setTemperature(finalObj.getString("temperature"));
                    bean.setHumidity(finalObj.getString("humidity"));
                    bean.setInfo(finalObj.getString("info"));
                    bean.setDate(obj.getString("date"));
                    bean.setNongli(obj.getString("nongli"));
                    bean.setWeek("星期" + obj.getString("week"));
                    JSONObject info = obj.getJSONObject("info");
                    JSONArray secArray = info.getJSONArray("night");
                    bean.setNlow((String) secArray.get(0));
                    bean.setNhigh((String) secArray.get(2));
                    bean.setNweather((String) secArray.get(1));
                    bean.setNwind((String) secArray.get(3)+(String) secArray.get(4));
                    bean.setNriluo((String) secArray.get(5));
                    JSONArray secArray2 = info.getJSONArray("day");
                    bean.setHigh((String) secArray2.get(2));
                    bean.setLow((String) secArray2.get(0));
                    bean.setWeather((String) secArray2.get(1));
                    bean.setRichu((String) secArray2.get(5));
                    bean.setWind((String) secArray.get(3)+(String) secArray2.get(4));
                    list.add(bean);
                }
            } else {
                System.out.println(object.get("error_code") + ":"
                        + object.get("reason"));
            }
        } catch (Exception e) {
            ResultStr = null;
            e.printStackTrace();
        }
        if (ResultStr == null) {
            msg.obj = null;
            handler.sendMessage(msg);
        } else {
            msg.obj = list;
            handler.sendMessage(msg);
        }

    }

    /**
     * --------------------------------以下为共用方法------------------------------
     * <p>
     * /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params, String method)
            throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if (method == null || method.equals("GET")) {
                strUrl = strUrl + "?" + urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params != null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(
                            conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    // 将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=")
                        .append(URLEncoder.encode(i.getValue() + "", "UTF-8"))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
