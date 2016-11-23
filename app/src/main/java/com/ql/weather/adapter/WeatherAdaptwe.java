package com.ql.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ql.weather.R;
import com.ql.weather.model.WeatherBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class WeatherAdaptwe extends BaseAdapter {
    private Context c;
    private List<WeatherBean> list = new ArrayList<WeatherBean>();

    public WeatherAdaptwe(Context c, List<WeatherBean> list) {
        this.c = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.weather_list_item, null);
            holder = new ViewHolder();
            holder.temp = (TextView) convertView.findViewById(R.id.temp);
            holder.weather = (TextView) convertView.findViewById(R.id.weather);
            holder.wind = (TextView) convertView.findViewById(R.id.wind);
            holder.week = (TextView) convertView.findViewById(R.id.week);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.temp.setText(list.get(position).getNhigh() + "℃~" + list.get(position).getHigh()+"℃");
        holder.weather.setText(list.get(position).getWeather());
        holder.wind.setText(list.get(position).getWind());
        holder.week.setText(list.get(position).getWeek());
        return convertView;
    }

    private class ViewHolder {
        TextView  wind, weather,temp,week;
    }
}
