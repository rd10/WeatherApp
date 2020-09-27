package com.example.stormy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stormy.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;

    private ImageView iconImageView;
    final String locationKey = "";

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
        String amount = "1hour";

        String forecastURL = "https://dataservice.accuweather.com/forecasts/v1/" + method +"/" + amount + "/"+ locationKey +"?apikey=" + apiKey;

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forecastURL)
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
                            currentWeather = getCurrentDetails(jsonData);

                            CurrentWeather displayWeather = new CurrentWeather(
                                    currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getIconPhrase(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimeZone()
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
        }
        else {
            /*Toast.makeText(this, R.string.network_unavailable_message,
                    Toast.LENGTH_LONG).show();*/
            alertUserAboutError(getString(R.string.network_unavailable_message));
        }
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONArray forecastArray = new JSONArray(jsonData);
        JSONObject forecast = forecastArray.getJSONObject(0);

        String time = forecast.getString("DateTime");
        Log.i(TAG, "From JSON: " + time);

        JSONObject Temperature = forecast.getJSONObject("Temperature");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setIconPhrase(forecast.getString("IconPhrase"));
        currentWeather.setTime(forecast.getLong("EpochDateTime"));
        currentWeather.setIcon(forecast.getInt("WeatherIcon"));
        currentWeather.setLocationLabel("Arvin, CA");
        currentWeather.setPrecipChance(forecast.getDouble("PrecipitationProbability"));
        currentWeather.setSummary(forecast.getString("MobileLink"));
        currentWeather.setTemperature(Temperature.getInt("Value"));
        currentWeather.setTimeZone("America/Los_Angeles");

        Log.d(TAG, currentWeather.getFormattedTime());

        return currentWeather;
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
}