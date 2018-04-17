package shuja.com.accuweather.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shuja.com.accuweather.entity.currentweather.CurrentWeatherEntity;
import shuja.com.accuweather.services.CurrentWeatherService;
import shuja.com.accuweather.utility.Callback;
import shuja.com.accuweather.utility.NoNetworkException;
import shuja.com.accuweather.utility.Task;
import shuja.com.accuweather.utility.WebException;

public class CurrentWeatherFacade {
    private static List<CurrentWeatherEntity> listOfCurrentWeather = new ArrayList<>();

    static public void getCurrentWeatherDataFacade(final List<String> listOfCity, final String temperatureUnits, final Callback<List<CurrentWeatherEntity>> callback) {
        new Task<>(new Task.RunInBackground<List<CurrentWeatherEntity>>() {
            @Override
            public List<CurrentWeatherEntity> runInBackground() throws NoNetworkException, WebException, IOException {
                for (String city : listOfCity) {
                    listOfCurrentWeather.add(CurrentWeatherService.getCurrentWeatherDataService(city, temperatureUnits));
                }
                return listOfCurrentWeather;
            }
        }, callback).execute();
    }

    static public void checkValidCity(final String city, final Callback<String> callback) {
        new Task<>(new Task.RunInBackground<String>() {
            @Override
            public String runInBackground() throws NoNetworkException, WebException, IOException {
                String responseCode = CurrentWeatherService.checkValidCityService(city);
                return responseCode;
            }
        }, callback).execute();
    }
}
