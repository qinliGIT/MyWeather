package com.ql.weather;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ql.weather.adapter.WeatherAdaptwe;
import com.ql.weather.https.HttpConst;
import com.ql.weather.https.Https;
import com.ql.weather.model.WeatherBean;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WeatherAdaptwe adapter;
    private TextView temp, mild, windNum, tempNum, dateNum,MainWea;
    private ListView listview;
    private RelativeLayout bar, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.list);
        title = (RelativeLayout) findViewById(R.id.title);
        bar = (RelativeLayout) findViewById(R.id.bar);
        temp = (TextView) findViewById(R.id.temp);
        mild = (TextView) findViewById(R.id.mild);
        windNum = (TextView) findViewById(R.id.windNum);
        tempNum = (TextView) findViewById(R.id.tempNum);
        dateNum = (TextView) findViewById(R.id.dateNum);
        MainWea = (TextView) findViewById(R.id.MainWea);
        setCurrentWeatherLayoutHight();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Https.getRequestForWeather(MainActivity.this, handler, "深圳");
            }
        }).start();
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
                    temp.setText(list.get(0).getTemperature() + "℃");
                    dateNum.setText(list.get(0).getWeek()+"/"+list.get(0).getDate());
                    tempNum.setText(list.get(0).getNhigh()+"℃~"+list.get(0).getHigh()+"℃");
                    windNum.setText(list.get(0).getWind());
                    mild.setText("湿度:"+list.get(0).getHumidity());
                    MainWea.setText(list.get(0).getWeather());
                    break;
            }
        }
    };


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
