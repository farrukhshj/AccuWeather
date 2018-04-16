package shuja.com.accuweather.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shuja.com.accuweather.R;

public class AdapterFiveDayWeather extends RecyclerView.Adapter<AdapterFiveDayWeather.ViewHolder> {


    @NonNull
    @Override
    public AdapterFiveDayWeather.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_five_day_weather, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFiveDayWeather.ViewHolder holder, int position) {
        holder.tv_date.setText("12-12-2018");
        holder.tv_summary.setText("Set Text");
        holder.tv_temperature.setText("12 C");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public AdapterFiveDayWeather(){

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date;
        TextView tv_temperature;
        TextView tv_summary;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            tv_temperature = itemView.findViewById(R.id.tv_temperature);
            tv_summary = itemView.findViewById(R.id.tv_summary);

        }
    }
}
