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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


public class FragmentListOfCities extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    private AdapterForListOfCities adapterForListOfCities;
    private AlertDialog alertDialog;
    private List<CurrentWeatherEntity> currentWeatherEntity = new ArrayList<>();
    private List<String> listOfCities = new ArrayList<>();
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate(R.layout.fragment_list_of_cities, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        listOfCities.add("Richmond");
        db = new DatabaseHelper(getActivity());
        listOfCities.addAll(db.getCitiesFromDB());
        fetchWeatherData(listOfCities);
    }

    private void fetchWeatherData(List<String> listOfCities) {
        CurrentWeatherFacade.getCurrentWeatherDataFacade(listOfCities, "metric", new Callback<List<CurrentWeatherEntity>>() {
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

        RecyclerView rv_list_of_cities = view.findViewById(R.id.rv_list_of_cities);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list_of_cities.setLayoutManager(layoutManager);

        RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(), "Clicked" + position , Toast.LENGTH_SHORT).show();
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
                View view = getLayoutInflater().inflate(R.layout.add_location_prompt, null);
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
                            listOfCities.clear();
                            db.insertCityInDB(etLocation.getText().toString());
                            listOfCities.add(etLocation.getText().toString());
                            fetchWeatherData(listOfCities);
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
            for(int i =0; i < listOfCities.size(); i++){
                if(listOfCities.get(i).equalsIgnoreCase(cityToBeRemovedFromDB)){
                    listOfCities.remove(i);
                }
            }
            db.deleteCity(cityToBeRemovedFromDB);
            Log.d("DatabaseHelper:"," current row count "+String.valueOf(db.getCityCount()));
        }

    }
}
