package shuja.com.accuweather.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import shuja.com.accuweather.R;
import shuja.com.accuweather.entity.currentweather.CurrentWeatherEntity;


public class AdapterForListOfCities extends RecyclerView.Adapter<AdapterForListOfCities.ViewHolder> {
    private List<CurrentWeatherEntity> mCurrentWeatherEntity;
    private RecyclerViewClickListener mListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ConstraintLayout viewBackground, viewForeground;
        private RecyclerViewClickListener mRecyclerViewClickListener;
        TextView tv_city_name;
        TextView tv_current_temperature;
        TextView tv_zipcode;


        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mRecyclerViewClickListener = listener;
            itemView.setOnClickListener(this);
            tv_city_name = itemView.findViewById(R.id.tv_city_name);
            tv_current_temperature = itemView.findViewById(R.id.tv_current_temperature);
            tv_zipcode = itemView.findViewById(R.id.tv_zipcode);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            viewForeground = itemView.findViewById(R.id.viewForeground);
        }

        @Override
        public void onClick(View view) {
            mRecyclerViewClickListener.onClick(view,getAdapterPosition());
        }
    }

    public AdapterForListOfCities(List<CurrentWeatherEntity> currentWeatherEntity, RecyclerViewClickListener listener) {
        this.mCurrentWeatherEntity = currentWeatherEntity;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AdapterForListOfCities.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_of_cities_row, parent, false);
        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForListOfCities.ViewHolder holder, int position) {
        holder.tv_city_name.setText(mCurrentWeatherEntity.get(position).getName());
        holder.tv_current_temperature.setText(String.valueOf(Math.floor(mCurrentWeatherEntity.get(position).getMain().getTemp())));
        holder.tv_zipcode.setText(String.valueOf(mCurrentWeatherEntity.get(position).getWeather().get(0).getMain()));
    }

    @Override
    public int getItemCount() {
        return mCurrentWeatherEntity.size();
    }

    public String removeItem(int position) {
        String cityName = mCurrentWeatherEntity.get(position).getName();
        mCurrentWeatherEntity.remove(position);
        notifyItemRemoved(position);
        return cityName;
    }

}
