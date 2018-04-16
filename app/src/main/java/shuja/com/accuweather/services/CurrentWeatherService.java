package shuja.com.accuweather.services;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import shuja.com.accuweather.entity.currentweather.CurrentWeatherEntity;
import shuja.com.accuweather.utility.NoNetworkException;
import shuja.com.accuweather.utility.OkHttpManager;
import shuja.com.accuweather.utility.WebException;

public class CurrentWeatherService {

    private static String baseURL = "https://api.openweathermap.org/data/2.5/weather";
    private static String API_KEY = "37a69371df31abe40928104ab8cec132";

    public static CurrentWeatherEntity getCurrentWeatherDataService(String city, String temperatureUnit) throws NoNetworkException, WebException, IOException {

        HttpUrl.Builder httpBuilder = HttpUrl.parse(baseURL).newBuilder();
        if (!city.isEmpty()) {
            httpBuilder.addQueryParameter("q", city);
            httpBuilder.addQueryParameter("appid", API_KEY);
            httpBuilder.addQueryParameter("units", temperatureUnit);
        }
        Request.Builder request = new Request.Builder();
        request.url(httpBuilder.build()).build();
        request.get();

        Response response = OkHttpManager.performRequest(request.build());
        String responseString = response.body().string();
        Log.d("CurrentWeatherService:"," JSON Response = " +responseString);
        return new Gson().fromJson(responseString, CurrentWeatherEntity.class);
    }

}
