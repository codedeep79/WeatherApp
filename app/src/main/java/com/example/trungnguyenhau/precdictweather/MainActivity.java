package com.example.trungnguyenhau.precdictweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button btnSearch, btnNextDay;
    private EditText edtSearch;
    private TextView txtCityName, txtCountryName,
            txtTemperature, txtStateTemperature, txtHumidity, txtCloud,
                txtWillmill, txtUpdateDay;

    private ImageView imgIcon;

    String searchData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnSearch           = (Button) findViewById(R.id.button_search);
        btnNextDay          = (Button) findViewById(R.id.button_nextday);
        edtSearch           = (EditText) findViewById(R.id.edittext_search);

        txtCityName         = (TextView) findViewById(R.id.textview_cityname);
        txtCountryName      = (TextView) findViewById(R.id.textview_countryname);
        txtTemperature      = (TextView) findViewById(R.id.textview_temperature);
        txtStateTemperature = (TextView) findViewById(R.id.textview_stateweather);
        txtHumidity         = (TextView) findViewById(R.id.textview_humidity);
        txtCloud            = (TextView) findViewById(R.id.textview_cloud);
        txtWillmill         = (TextView) findViewById(R.id.textview_willmill);
        txtUpdateDay        = (TextView) findViewById(R.id.textview_update_day);

        imgIcon = (ImageView) findViewById(R.id.imageview_icon);

    }

    private void addEvents() {
        getCurrentWeatherData("Sai Gon");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData = edtSearch.getText().toString();
                if (!searchData.equals(""))
                {
                    getCurrentWeatherData(searchData);
                }
                else
                {
                    searchData = "Sai Gon";
                    getCurrentWeatherData(searchData);
                }
            }
        });


        btnNextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchData = edtSearch.getText().toString();

                Intent intent = new Intent(MainActivity.this, NextDayActivity.class);
                intent.putExtra("searchCity", searchData);
                startActivity(intent);
            }
        });
    }

    private void getCurrentWeatherData(final String data){
        // Thực thi những yêu cầu mà chúng ta gửi đi
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=3c5656da04446f9e45f4b261c3543a52";

        // StringRequest(Phương thức đọc dữ liệu đường dẫn là gì,
        //                              đường dẫn đọc dữ liệu,
        //                                      Trả về giá trị đọc được hoặc trả về lỗi trong quá trifh đọc dữ liệu)
        StringRequest stringRequest = new StringRequest(StringRequest.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
        //              Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtCityName.setText("Tên Thành Phố: " + name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE  yyyy-dd-MM  HH:mm:ss");
                            String Day = simpleDateFormat.format(date);
                            txtUpdateDay.setText(Day);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");

                            // Lấy phần tử đầu tiên trong jsonArrayWeather
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(MainActivity.this)
                                    .load("http://openweathermap.org/img/w/" + icon + ".png").into(imgIcon);

                            txtStateTemperature.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temperature = jsonObjectMain.getString("temp");
                            String humidity = jsonObjectMain.getString("humidity");

                            Double temp = Double.valueOf(temperature);
                            String tempConvertString = String.valueOf(temp.intValue());

                            txtTemperature.setText("Nhiệt Độ: " + tempConvertString + " *C");
                            txtHumidity.setText("Độ Ẩm: " + humidity + " %");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String speedWind = jsonObjectWind.getString("speed");
                            txtWillmill.setText(speedWind + " m/s");

                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String cloudPercent = jsonObjectCloud.getString("all");
                            txtCloud.setText(cloudPercent + " %");

                            JSONObject jsonObjectCountry = jsonObject.getJSONObject("sys");
                            String countryName = jsonObjectCountry.getString("country");
                            txtCountryName.setText("Tên Đất Nước: " + countryName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi Trong Quá Trình Thực Thi Trên Server", Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(stringRequest);

    }

}
