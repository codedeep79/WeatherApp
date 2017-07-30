package com.example.trungnguyenhau.precdictweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trungnguyenhau.precdictweather.Model.Weather;
import com.example.trungnguyenhau.precdictweather.WeatherAdapter.WeatherAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NextDayActivity extends AppCompatActivity {
    ImageView imgBack;
    TextView txtCityName;
    ListView lstWeather;
    String cityName;

    WeatherAdapter weatherAdapter;
    ArrayList<Weather> listWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_day);
        cityName = getIntent().getStringExtra("searchCity");
        if (!cityName.equalsIgnoreCase(""))
        {
            getSevenDayData(cityName);
        }
        else
        {
            getSevenDayData("sai gon");
        }

        addControls();
        addEvents();
    }

    private void addControls() {
        imgBack = (ImageView) findViewById(R.id.imageview_back);
        txtCityName = (TextView) findViewById(R.id.textview_cityname);
        lstWeather = (ListView) findViewById(R.id.listview_weather);

        listWeather = new ArrayList<Weather>();
        weatherAdapter = new WeatherAdapter(NextDayActivity.this, listWeather);
        lstWeather.setAdapter(weatherAdapter);

    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private void getSevenDayData(String cityName) {
        String linkData = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+cityName+"&units=metric&cnt=7&appid=3c5656da04446f9e45f4b261c3543a52";
        RequestQueue requestQueue = Volley.newRequestQueue(NextDayActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, linkData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String cityName = jsonObjectCity.getString("name");
                            txtCityName.setText(cityName);


                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0;i < jsonArray.length(); ++i)
                            {
                                JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                                String ngayThang = jsonObjectList.getString("dt");
                                long l = Long.valueOf(ngayThang);

                                // Chuyển Long sang mili giây *1000L
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE  yyyy-dd-MM  HH:mm:ss");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String maxTemp = jsonObjectTemp.getString("max");
                                String minTemp = jsonObjectTemp.getString("min");

                                Double maxTempConvertDouble = Double.valueOf(maxTemp);
                                Double minTempConvertDouble = Double.valueOf(minTemp);
                                String nhietDoLonNhat = String.valueOf(maxTempConvertDouble.intValue());
                                String nhietDoNhoNhat = String.valueOf(minTempConvertDouble.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");
                                listWeather.add(new Weather(Day, status, icon, "Nhiệt Độ Lớn Nhất: " + nhietDoLonNhat, "Nhiệt Độ Nhỏ Nhất: " + nhietDoNhoNhat));

                            }
                            weatherAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
        });
        requestQueue.add(stringRequest);
    }


}
