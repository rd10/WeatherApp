package com.example.stormy.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormy.R;
import com.example.stormy.databinding.ActivityMainBinding;
import com.example.stormy.weather.Current;
import com.example.stormy.weather.Forecast;
import com.example.stormy.weather.Hour;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Forecast forecast;

    private ImageView iconImageView;
    final String locationKey = "347625";
    private Current current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getForecast(locationKey);
        Log.d(TAG, "Main UI code is running, hurray!");

    }

    private void getForecast(String locationKey) {
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        TextView accuWeather = findViewById(R.id.accuWeatherAttribuition);
        accuWeather.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView accu = findViewById(R.id.accuWeatherImage);
        accu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.accuweather.com"));
                startActivity(intent);
            }
        });

        iconImageView = findViewById(R.id.iconImageView);

        String apiKey = "";
        String method = "hourly";
        String amount = "12hour";

        String hourlyForecastURL = "https://dataservice.accuweather.com/forecasts/v1/" + method +"/" + amount + "/"+ locationKey +"?apikey=" + apiKey;
        String currentForecastURL = "https://dataservice.accuweather.com/currentconditions/v1/"+ locationKey + "?apikey=" + apiKey + "&language=en-us&details=true";

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(currentForecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.v("FAILED", "test");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) {
                    try {
                        //Response response = call.execute();
                        String jsonData = Objects.requireNonNull(response.body()).string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            //forecast = parseForecastData(jsonData);

                            //Current current = forecast.getCurrent();
                            current = getCurrentDetails(jsonData);
                            Current displayWeather = new Current(
                                    current.getLocationLabel(),
                                    current.getIcon(),
                                    current.getTime(),
                                    current.getTemperature(),
                                    current.getIconPhrase(),
                                    current.getPrecipChance(),
                                    current.getSummary(),
                                    current.getTimeZone()
                            );

                            binding.setWeather(displayWeather);
                            Drawable drawable = getResources().getDrawable(displayWeather.getIconId());
                            iconImageView.setImageDrawable(drawable);

                            //comment code below: tutorial showed this way of doing it since icon image could not be updated
                            /*runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = getResources().getDrawable(displayWeather.getIconId());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });*/

                        } else {
                            alertUserAboutError(getString(R.string.error_message));
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
            // building second request to get hourly data from AccuWeather
            Request request2 = new Request.Builder()
                    .url(hourlyForecastURL)
                    .build();

            Call call2 = client.newCall(request2);
            call2.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.v("FAILED", "test");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String jsonHoursData = Objects.requireNonNull(response.body()).string();
                        Log.v(TAG, jsonHoursData);

                        if (response.isSuccessful()) {
                            forecast = parseForecastData(jsonHoursData);
                        } else {alertUserAboutError(getString(R.string.error_message));}
                     } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            /*Toast.makeText(this, R.string.network_unavailable_message,
                    Toast.LENGTH_LONG).show();*/
            alertUserAboutError(getString(R.string.network_unavailable_message));
        }
    }

    private Forecast parseForecastData(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(current);
        forecast.setHourlyForecast(getHourlyForecast(jsonData));

        return forecast;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONArray forecastArray = new JSONArray(jsonData);

        Hour[] hours = new Hour[forecastArray.length()];

        for(int i=0; i<forecastArray.length(); i++){
            JSONObject jsonHour = forecastArray.getJSONObject(i);
            JSONObject temperature = jsonHour.getJSONObject("Temperature");

            Hour hour = new Hour();
            hour.setIconPhrase(jsonHour.getString("IconPhrase"));
            hour.setIcon(jsonHour.getInt("WeatherIcon"));
            hour.setTemperature(temperature.getInt("Value"));
            hour.setTime(jsonHour.getLong("EpochDateTime"));
            hour.setTimeZone("America/Los_Angeles");

            hours[i] = hour;
        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONArray forecastArray = new JSONArray(jsonData);
        JSONObject forecast = forecastArray.getJSONObject(0);

        String time = forecast.getString("LocalObservationDateTime");
        Log.i(TAG, "From JSON: " + time);

        JSONObject Temperature = forecast.getJSONObject("Temperature");
        JSONObject Imperial = Temperature.getJSONObject("Imperial");

        Current current = new Current();

        current.setIconPhrase(forecast.getString("WeatherText"));
        current.setTime(forecast.getLong("EpochTime"));
        current.setIcon(forecast.getInt("WeatherIcon"));
        //current.setLocationLabel("Los Angeles, CA");
        current.setPrecipChance(forecast.getString("PrecipitationType"));
        current.setSummary(forecast.getString("MobileLink"));
        current.setTemperature(Imperial.getInt("Value"));
        current.setTimeZone("America/Los_Angeles");

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError(String message) {
        Bundle bundle = new Bundle();
        bundle.putString("message_key", message);

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "error_dialog");
    }

    public void refreshOnClick(View view){
        Toast.makeText(this,"Refreshing data", Toast.LENGTH_LONG).show();
        getForecast(locationKey);

    }

    public void hourlyOnClick(View view){
        List<Hour> hours = Arrays.asList(forecast.getHourlyForecast());
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra("HourlyList", (Serializable) hours);
        startActivity(intent);

    }
}