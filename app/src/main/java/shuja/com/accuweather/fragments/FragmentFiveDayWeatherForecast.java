package shuja.com.accuweather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shuja.com.accuweather.R;
import shuja.com.accuweather.adapters.AdapterFiveDayWeather;
import shuja.com.accuweather.entity.weather5days.FiveDayWeatherEntity;
import shuja.com.accuweather.facade.FiveDayWeatherFacade;
import shuja.com.accuweather.utility.Callback;

public class FragmentFiveDayWeatherForecast extends Fragment {

    private String city;
    private String unit;
    private FiveDayWeatherEntity fiveDayWeatherEntity;
    private AdapterFiveDayWeather adapterFiveDayWeather;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragment_five_day_weather_forecast, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
       Bundle bundle = getArguments();
       if(bundle != null) {
           city = bundle.getString("city");
           unit = bundle.getString("unit");
       }

       fetchDataFomWebService(city);
    }

    private void fetchDataFomWebService(String city) {
        FiveDayWeatherFacade.getFiveDayWeatherDataFacade(city, unit, new Callback<FiveDayWeatherEntity>() {
            @Override
            public void onCallback(@Nullable FiveDayWeatherEntity data, @Nullable Exception exception) {
                if(data!=null && exception == null && isAlive()){
                   fiveDayWeatherEntity = data;
                   bindViewsWithData();
                }
            }
        });
    }

    private void bindViewsWithData() {
        View view = getView();
        RecyclerView rv_five_day_weather = view.findViewById(R.id.rv_five_day_weather);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_five_day_weather.setLayoutManager(layoutManager);

        adapterFiveDayWeather = new AdapterFiveDayWeather(fiveDayWeatherEntity);
        rv_five_day_weather.setAdapter(adapterFiveDayWeather);
        rv_five_day_weather.setItemAnimator(new DefaultItemAnimator());
        rv_five_day_weather.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    public boolean isAlive() {
        boolean returnValue = (!isDetached() && isAdded() && getActivity() != null);
        return returnValue;
    }
}
