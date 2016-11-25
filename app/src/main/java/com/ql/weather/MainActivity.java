package com.ql.weather;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ql.weather.adapter.WeatherAdaptwe;
import com.ql.weather.city.GetProviceActivity;
import com.ql.weather.https.HttpConst;
import com.ql.weather.https.Https;
import com.ql.weather.location.CheckPermissionsActivity;
import com.ql.weather.model.WeatherBean;
import com.ql.weather.utils.PreferenceUtils;
import java.util.List;

public class MainActivity extends CheckPermissionsActivity implements View.OnClickListener {
    private static final int BEGIN_CODE = 0x01;
    private boolean isLoca = true;
    //声明一个AlertDialog构造器
    private AlertDialog.Builder builder;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    private WeatherAdaptwe adapter;
    private TextView temp, mild, windNum, tempNum, dateNum, MainWea,  location;
    private ListView listview;
    private RelativeLayout bar, title;
    private ImageView about, share, wea_img;

    private String locationStr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化定位
        initLocation();


        listview = (ListView) findViewById(R.id.list);
        title = (RelativeLayout) findViewById(R.id.title);
        bar = (RelativeLayout) findViewById(R.id.bar);
        temp = (TextView) findViewById(R.id.temp);
        mild = (TextView) findViewById(R.id.mild);
        windNum = (TextView) findViewById(R.id.windNum);
        tempNum = (TextView) findViewById(R.id.tempNum);
        dateNum = (TextView) findViewById(R.id.dateNum);
        MainWea = (TextView) findViewById(R.id.MainWea);
        location = (TextView) findViewById(R.id.location);
        about = (ImageView) findViewById(R.id.about);
        share = (ImageView) findViewById(R.id.share);
        wea_img = (ImageView) findViewById(R.id.wea_img);
        about.setOnClickListener(this);
        share.setOnClickListener(this);
        location.setOnClickListener(this);
        /**
         *  设置布局高度
         */
        setCurrentWeatherLayoutHight();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpConst.MSG_WHAT_WEATHER:
                    List<WeatherBean> list = (List<WeatherBean>) msg.obj;
                    adapter = new WeatherAdaptwe(MainActivity.this, list);
                    listview.setAdapter(adapter);
                    if (list.size() == 0) {
                        return;
                    }
                    String temperature = list.get(0).getTemperature() + "℃";
                    String date = list.get(0).getWeek() + "/" + list.get(0).getDate();
                    String temp_num = list.get(0).getNhigh() + "℃~" + list.get(0).getHigh() + "℃";
                    String shidu = "湿度:" + list.get(0).getHumidity();
                    String wea = list.get(0).getWeather();
                    String wind = list.get(0).getWind();
                    temp.setText(temperature);
                    dateNum.setText(date);
                    tempNum.setText(temp_num);
                    windNum.setText(wind);
                    mild.setText(shidu);
                    MainWea.setText(wea);
                    if(!isLoca) {
                        location.setText(list.get(0).getCity());
                    }
                    if (wea.contains("小雨")) {
                        wea_img.setImageResource(R.mipmap.xyu);
                    }
                    if (wea.contains("大雨")) {
                        wea_img.setImageResource(R.mipmap.dyu);
                    }
                    if (wea.contains("中雨")) {
                        wea_img.setImageResource(R.mipmap.zhongyu);
                    }
                    if (wea.contains("暴雨")) {
                        wea_img.setImageResource(R.mipmap.byu);
                    }
                    if (wea.contains("多云")) {
                        wea_img.setImageResource(R.mipmap.dyun);
                    }
                    if (wea.contains("阴")) {
                        wea_img.setImageResource(R.mipmap.y);
                    }
                    if (wea.contains("晴")) {
                        wea_img.setImageResource(R.mipmap.qin);
                    }
                    if (wea.contains("小雪")) {
                        wea_img.setImageResource(R.mipmap.xxue);
                    }
                    PreferenceUtils.getInstance(MainActivity.this).setDate(date);
                    PreferenceUtils.getInstance(MainActivity.this).setShidu(shidu);
                    PreferenceUtils.getInstance(MainActivity.this).setTemp_NUM(temp_num);
                    PreferenceUtils.getInstance(MainActivity.this).setTEMPERATURE(temperature);
                    PreferenceUtils.getInstance(MainActivity.this).setWea(wea);
                    PreferenceUtils.getInstance(MainActivity.this).setWind(wind);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        temp.setText(PreferenceUtils.getInstance(MainActivity.this).getTEMPERATURE());
        dateNum.setText(PreferenceUtils.getInstance(MainActivity.this).getDate());
        tempNum.setText(PreferenceUtils.getInstance(MainActivity.this).getTEMP_NUM());
        windNum.setText(PreferenceUtils.getInstance(MainActivity.this).getWind());
        mild.setText(PreferenceUtils.getInstance(MainActivity.this).getShidu());
        MainWea.setText(PreferenceUtils.getInstance(MainActivity.this).getWea());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PreferenceUtils.getInstance(MainActivity.this).getLocation().equalsIgnoreCase("")) {
            isLoca = true;
            startLocation();
        } else {
            locationStr = PreferenceUtils.getInstance(MainActivity.this).getCity();
            isLoca = false;
            GetData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    /**
     * 设置布局的高度（铺满屏幕）
     */
    private void setCurrentWeatherLayoutHight() {
        // 通知栏高度
        int statusBarHeight = 0;
        try {
            statusBarHeight = getResources().getDimensionPixelSize(
                    Integer.parseInt(Class
                            .forName("com.android.internal.R$dimen")
                            .getField("status_bar_height")
                            .get(Class.forName("com.android.internal.R$dimen")
                                    .newInstance()).toString()));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // 屏幕高度
        int displayHeight = ((WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getHeight();
        // title bar LinearLayout高度，标题栏高度
        bar.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int BarHeight = bar.getMeasuredHeight();
        // 剩下的即为要得到的布局高度
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) title
                .getLayoutParams();
        linearParams.height = displayHeight - statusBarHeight - BarHeight;
        title.setLayoutParams(linearParams);
    }

    private void GetData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Https.getRequestForWeather(MainActivity.this, handler, locationStr);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about:
                showSimpleDialog(view);
                break;
            case R.id.share:

                break;
            case R.id.location:
                Intent intent = new Intent(MainActivity.this,
                        GetProviceActivity.class);
                PreferenceUtils.getInstance(MainActivity.this).setLocation("second");
                startActivityForResult(intent, BEGIN_CODE);
                break;
        }
    }

    //显示基本Dialog
    private void showSimpleDialog(View view) {
        builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("关于");
        builder.setMessage("Author:覃力\n\n联系方式：QQ：2824648353，邮箱：2824648353@qq.com\n\nAll rights reserved");

        //监听下方button点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 获取传会的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BEGIN_CODE:
                Bundle bundle = data.getExtras();
                String provice = bundle.getString("provice");
                String cityName = bundle.getString("cityName");
                // 出发地
                location.setText(cityName);
                locationStr = cityName;
                PreferenceUtils.getInstance(MainActivity.this).setCity(cityName);
//                stopLocation();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                String result = loc.getAddress();
                String[] str = result.split("市");
                result = str[1];
                location.setText(result);
                locationStr = loc.getCity();
                PreferenceUtils.getInstance(MainActivity.this).setCity(locationStr);
                if (!locationStr.equalsIgnoreCase("")) {
                    GetData();
                }
            } else {
                location.setText("定位失败，loc is null");
            }
            stopLocation();
        }
    };

    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
