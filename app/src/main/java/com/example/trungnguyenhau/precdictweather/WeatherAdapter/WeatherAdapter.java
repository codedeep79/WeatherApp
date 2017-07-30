package com.example.trungnguyenhau.precdictweather.WeatherAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trungnguyenhau.precdictweather.MainActivity;
import com.example.trungnguyenhau.precdictweather.Model.Weather;
import com.example.trungnguyenhau.precdictweather.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class WeatherAdapter extends BaseAdapter{
    Context context;
    ArrayList<Weather> listWeather;

    public WeatherAdapter(Context context, ArrayList<Weather> listWeather) {
        this.context = context;
        this.listWeather = listWeather;
    }

    @Override
    public int getCount() {
        return listWeather.size();
    }

    @Override
    public Object getItem(int position) {
        return listWeather.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_list_weather, null);

        Weather weather     = listWeather.get(position);

        TextView txtDay     = (TextView) view.findViewById(R.id.textview_ngaythang_custom);
        TextView txtState   = (TextView) view.findViewById(R.id.textview_trangthai_custom);
        TextView txtMaxTemp = (TextView) view.findViewById(R.id.textview_maxtemp_custom);
        TextView txtMinTemp = (TextView) view.findViewById(R.id.textview_mintemp_custom);
        ImageView imgState  = (ImageView) view.findViewById(R.id.imageview_trangthai_custom);

        txtDay.setText(weather.getDay());
        txtState.setText(weather.getStatus());
        txtMaxTemp.setText(weather.getMaxTemp());
        txtMinTemp.setText(weather.getMinTemp());

        Picasso.with(context)
                .load("http://openweathermap.org/img/w/" + weather.getImage() + ".png").into(imgState);

        return view;
    }
}
