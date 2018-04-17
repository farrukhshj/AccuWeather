package shuja.com.accuweather.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import shuja.com.accuweather.R;
import shuja.com.accuweather.adapters.AdapterForListOfCities;
import shuja.com.accuweather.adapters.RecyclerViewClickListener;
import shuja.com.accuweather.entity.currentweather.CurrentWeatherEntity;
import shuja.com.accuweather.facade.CurrentWeatherFacade;
import shuja.com.accuweather.utility.Callback;
import shuja.com.accuweather.utility.DatabaseHelper;
import shuja.com.accuweather.utility.RecyclerItemTouchHelper;


public class FragmentListOfCities extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private AdapterForListOfCities adapterForListOfCities;
    private AlertDialog alertDialog;
    private List<CurrentWeatherEntity> currentWeatherEntity = new ArrayList<>();
    private List<String> listOfCities = new ArrayList<>();
    private DatabaseHelper db;
    private String tempUnit;
    private boolean isValidCity;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragment_list_of_cities, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        final Button btn_celsius = view.findViewById(R.id.btn_celsius);
        final Button btn_farenheit = view.findViewById(R.id.btn_farenheit);
        tempUnit = "metric";
        btn_celsius.setTextColor(getResources().getColor(R.color.colorAccent));
        btn_celsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempUnit = "metric";
                btn_celsius.setTextColor(getResources().getColor(R.color.colorAccent));
                btn_farenheit.setTextColor(getResources().getColor(R.color.backgroundDefault));

                if (adapterForListOfCities.mCurrentWeatherEntity != null && listOfCities != null) {
                    adapterForListOfCities.mCurrentWeatherEntity.clear();
                    listOfCities.clear();
                }
                listOfCities.addAll(db.getCitiesFromDB());
                fetchWeatherData(listOfCities);
            }
        });
        btn_farenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempUnit = "imperial";
                btn_celsius.setTextColor(getResources().getColor(R.color.backgroundDefault));
                btn_farenheit.setTextColor(getResources().getColor(R.color.colorAccent));

                if (adapterForListOfCities.mCurrentWeatherEntity != null && listOfCities != null) {
                    adapterForListOfCities.mCurrentWeatherEntity.clear();
                    listOfCities.clear();
                }

                listOfCities.addAll(db.getCitiesFromDB());
                fetchWeatherData(listOfCities);
            }
        });

        db = new DatabaseHelper(getActivity());
        listOfCities.addAll(db.getCitiesFromDB());
        fetchWeatherData(listOfCities);
    }

    private void fetchWeatherData(List<String> listOfCities) {
        CurrentWeatherFacade.getCurrentWeatherDataFacade(listOfCities, tempUnit, new Callback<List<CurrentWeatherEntity>>() {
            @Override
            public void onCallback(@Nullable List<CurrentWeatherEntity> data, @Nullable Exception exception) {
                if (data != null && isAlive() && exception == null) {
                    currentWeatherEntity = data;
                    bindViewsWithData();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    private void bindViewsWithData() {
        View view = getView();

        final RecyclerView rv_list_of_cities = view.findViewById(R.id.rv_list_of_cities);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list_of_cities.setLayoutManager(layoutManager);

        RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (getActivity() != null) {

                    FragmentFiveDayWeatherForecast fragmentFiveDayWeatherForecast = new FragmentFiveDayWeatherForecast();
                    TextView tvCityName = view.findViewById(R.id.tv_city_name);
                    Bundle bundle = new Bundle();
                    bundle.putString("city", tvCityName.getText().toString());
                    bundle.putString("unit", tempUnit);
                    fragmentFiveDayWeatherForecast.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("FragmentListOfCities").replace(R.id.fl_fragment_container, fragmentFiveDayWeatherForecast, fragmentFiveDayWeatherForecast.getTag()).commit();

                    listOfCities.clear();
                    adapterForListOfCities.mCurrentWeatherEntity.clear();
                }
            }
        };

        adapterForListOfCities = new AdapterForListOfCities(currentWeatherEntity, recyclerViewClickListener);
        rv_list_of_cities.setAdapter(adapterForListOfCities);
        rv_list_of_cities.setItemAnimator(new DefaultItemAnimator());
        rv_list_of_cities.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_list_of_cities);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                final AlertDialog.Builder mAlertDailogBuilder = new AlertDialog.Builder(getContext());
                final View view = getLayoutInflater().inflate(R.layout.add_location_prompt, null);
                final EditText etLocation = view.findViewById(R.id.et_location);

                etLocation.setOnClickListener(new View.OnClickListener() {
                    public boolean alreadyInList = false;

                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < listOfCities.size(); i++) {
                            if (listOfCities.get(i).equalsIgnoreCase(etLocation.getText().toString())) {
                                Toast.makeText(getContext(), etLocation.getText().toString() + " already exists in the list", Toast.LENGTH_SHORT).show();
                                alreadyInList = true;
                                break;
                            }
                        }
                        if (!alreadyInList) {
                            CurrentWeatherFacade.checkValidCity(etLocation.getText().toString(), new Callback<String>() {
                                @Override
                                public void onCallback(@Nullable String data, @Nullable Exception exception) {
                                    if (data != null && isAlive() && exception == null) {
                                        listOfCities.clear();
                                        listOfCities.add(etLocation.getText().toString());
                                        fetchWeatherData(listOfCities);
                                        if(!TextUtils.isDigitsOnly(etLocation.getText().toString())) {
                                            db.insertCityInDB(etLocation.getText().toString().toLowerCase());
                                        }
                                    } else {
                                        Toast.makeText(getContext(), " Oops! I could not find that city", Toast.LENGTH_LONG).show();
                                        listOfCities.clear();
                                        listOfCities.addAll(db.getCitiesFromDB());
                                        adapterForListOfCities.mCurrentWeatherEntity.clear();
                                        fetchWeatherData(listOfCities);
                                    }
                                }
                            });
                        }
                        alertDialog.cancel();
                    }
                });
                mAlertDailogBuilder.setView(view);
                alertDialog = mAlertDailogBuilder.create();
                alertDialog.show();

                break;
        }
        return true;
    }

    public boolean isAlive() {
        boolean returnValue = (!isDetached() && isAdded() && getActivity() != null);
        return returnValue;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AdapterForListOfCities.ViewHolder) {
            String cityToBeRemovedFromDB = adapterForListOfCities.removeItem(viewHolder.getAdapterPosition());
            for (int i = 0; i < listOfCities.size(); i++) {
                if (listOfCities.get(i).equalsIgnoreCase(cityToBeRemovedFromDB)) {
                    listOfCities.remove(i);
                }
            }
            //db.deleteAllCities();
            db.deleteCity(cityToBeRemovedFromDB);
            Log.d("DatabaseHelper:", " current row count " + String.valueOf(db.getCityCount()));
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        adapterForListOfCities.mCurrentWeatherEntity.clear();
    }
}
