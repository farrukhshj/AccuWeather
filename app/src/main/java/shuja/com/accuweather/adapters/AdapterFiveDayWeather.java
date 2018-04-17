package shuja.com.accuweather.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import shuja.com.accuweather.R;
import shuja.com.accuweather.entity.weather5days.FiveDayWeatherEntity;

public class AdapterFiveDayWeather extends RecyclerView.Adapter<AdapterFiveDayWeather.ViewHolder> {

    private FiveDayWeatherEntity mData;
    private java.util.List<String> temps = new ArrayList<>();

    @NonNull
    @Override
    public AdapterFiveDayWeather.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_five_day_weather, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFiveDayWeather.ViewHolder holder, int position) {
        removeRedundantData(holder, position);
    }

    private void removeRedundantData(ViewHolder holder, int position) {
        String date = mData.getList().get(position).getDtTxt().split(" ")[0];
        String temp = String.valueOf(Math.floor(mData.getList().get(position).getMain().getTemp()));
        String weatherSummary = mData.getList().get(position).getWeather().get(0).getMain();
        String humidity = String.valueOf(mData.getList().get(position).getMain().getHumidity());
        String clouds = String.valueOf(mData.getList().get(position).getClouds().getAll());
        String winds = String.valueOf(mData.getList().get(position).getWind().getSpeed());

        if (position != 0) {
            if (!date.equalsIgnoreCase(temps.get(temps.size() - 1))) {
                temps.add(date);
                holder.tv_date.setText(date);
                holder.tv_summary.setText(weatherSummary);
                holder.tv_temperature.setText(temp);
                holder.tv_humidity.setText(humidity + "% humid");
                holder.tv_clouds.setText(clouds + "% Cloudiness");
                holder.tv_winds.setText("Wind speed "+ winds);

            } else {
                holder.tv_temperature.setVisibility(View.GONE);
                holder.tv_date.setVisibility(View.GONE);
                holder.tv_summary.setVisibility(View.GONE);
                holder.tv_humidity.setVisibility(View.GONE);
                holder.tv_clouds.setVisibility(View.GONE);
                holder.tv_winds.setVisibility(View.GONE);
            }
        } else {
            temps.add(date);
            holder.tv_date.setText(date);
            holder.tv_summary.setText(weatherSummary);
            holder.tv_temperature.setText(temp);
            holder.tv_humidity.setText(humidity + "% humid");
            holder.tv_clouds.setText(clouds + "% Cloudiness");
            holder.tv_winds.setText("Wind speed "+ winds);
        }
    }

    @Override
    public int getItemCount() {
        return mData.getList().size();
    }

    public AdapterFiveDayWeather(FiveDayWeatherEntity fiveDayWeatherEntity) {
        this.mData = fiveDayWeatherEntity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date;
        TextView tv_temperature;
        TextView tv_summary;
        TextView tv_humidity;
        TextView tv_clouds;
        TextView tv_winds;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_temperature = itemView.findViewById(R.id.tv_temperature);
            tv_summary = itemView.findViewById(R.id.tv_summary);
            tv_humidity = itemView.findViewById(R.id.tv_humidity);
            tv_clouds = itemView.findViewById(R.id.tv_clouds);
            tv_winds = itemView.findViewById(R.id.tv_winds);
        }
    }
}
