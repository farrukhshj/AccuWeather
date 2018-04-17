package shuja.com.accuweather.facade;

import java.io.IOException;

import shuja.com.accuweather.entity.weather5days.FiveDayWeatherEntity;
import shuja.com.accuweather.services.FiveDayWeatherService;
import shuja.com.accuweather.utility.Callback;
import shuja.com.accuweather.utility.NoNetworkException;
import shuja.com.accuweather.utility.Task;
import shuja.com.accuweather.utility.WebException;

public class FiveDayWeatherFacade {

    static public void getFiveDayWeatherDataFacade(final String city, final String temperatureUnits, final Callback<FiveDayWeatherEntity> callback) {
        new Task<>(new Task.RunInBackground<FiveDayWeatherEntity>() {
            @Override
            public FiveDayWeatherEntity runInBackground() throws NoNetworkException, WebException, IOException {
                return FiveDayWeatherService.getFiveDayWeatherDataService(city, temperatureUnits);
            }
        }, callback).execute();
    }
}
