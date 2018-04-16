package shuja.com.accuweather.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import shuja.com.accuweather.utility.DatabaseHelper;
import shuja.com.accuweather.R;
import shuja.com.accuweather.fragments.FragmentListOfCities;

public class HomeActivity extends AppCompatActivity {
    private DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(findViewById(R.id.fl_fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }
            FragmentListOfCities fragmentListOfCities = new FragmentListOfCities();
            fragmentListOfCities.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, fragmentListOfCities,  fragmentListOfCities.getTag()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add:
                return false;
        }
        return false;
    }
}
