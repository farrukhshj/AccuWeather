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
                for(String city : listOfCity) {
                    listOfCurrentWeather.add(CurrentWeatherService.getCurrentWeatherDataService(city, temperatureUnits));
                }
             return listOfCurrentWeather;
            }
        }, callback).execute();
    }
}
