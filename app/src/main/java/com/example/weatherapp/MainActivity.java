package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRl;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    //private RecyclerView weatherRV;
    private TextInputEditText cityEdit;
    private ImageView backIV, iconIV, searchIV;
    //private ArrayList<WeatherModelRV> weatherModelRVArrayList;
    //private WeatherAdapterRV weatherAdapterRV;

    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;

    private String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        homeRl = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        //weatherRV = findViewById(R.id.idRVWeather);
        cityEdit = findViewById(R.id.idEditCity);
        backIV = findViewById(R.id.idIVBlack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        /*weatherModelRVArrayList = new ArrayList<>();
        weatherAdapterRV = new WeatherAdapterRV(this, weatherModelRVArrayList);
        weatherRV.setAdapter(weatherAdapterRV);*/

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            cityName = getCityName(location.getLongitude(), location.getLatitude());
            getWeatherInfo(cityName);
        } else {
            cityName = "London";
            getWeatherInfo(cityName);
        }

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityEdit.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter city", Toast.LENGTH_SHORT).show();
                } else {
                    cityNameTV.setText(cityName);
                    getWeatherInfo(city);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "City not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for (Address ad : addresses) {
                if (ad != null) {
                    String city = ad.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                        Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo(String cityName) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=a42cb708f8a7493685f83844221605&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        homeRl.setVisibility(View.VISIBLE);
                        //weatherModelRVArrayList.clear();

                        try {
                            String temperature = response.getJSONObject("current").getString("temp_c");
                            temperatureTV.setText(temperature + "°C");
                            int isDay = response.getJSONObject("current").getInt("is_day");
                            String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                            conditionTV.setText(condition);
                            if (isDay == 1) {
                                Picasso.get().load("https://images.unsplash.com/photo-1517639493569-5666a7b2f494?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80").into(backIV);
                            } else {
                                Picasso.get().load("https://images.unsplash.com/photo-1507400492013-162706c8c05e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1559&q=80").into(backIV);
                            }

                            /*JSONObject forecastObj = response.getJSONObject("forecast");
                            JSONObject forecastDayObj = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray hourArray = forecastDayObj.getJSONArray("hour");

                            for (int i = 0; i < hourArray.length(); i++) {
                                JSONObject hourObj = hourArray.getJSONObject(i);
                                String time = hourObj.getString("time");
                                String temp = hourObj.getString("temp_c");
                                String img = hourObj.getJSONObject("condition").getString("icon");
                                String humidity = hourObj.getString("humidity");
                                weatherModelRVArrayList.add(new WeatherModelRV(time, temp, img, humidity));
                            }
                            weatherAdapterRV.notifyDataSetChanged();*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObject);
    }
}