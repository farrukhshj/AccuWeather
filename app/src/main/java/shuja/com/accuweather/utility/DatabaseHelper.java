package shuja.com.accuweather.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import shuja.com.accuweather.entity.currentweather.CurrentWeatherEntity;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "accu_weather_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CurrentWeatherEntity.CREATE_TABLE);
        Log.d("DatabaseHelper:", " onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CurrentWeatherEntity.TABLE_NAME);
        onCreate(sqLiteDatabase);
        Log.d("DatabaseHelper:", " onUpgrade");
    }

    public void insertCityInDB(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CurrentWeatherEntity.COLUMN_CITY_NAME, city);
        long id = db.insert(CurrentWeatherEntity.TABLE_NAME, null, values);
        Log.d("DatabaseHelper:", "Inserting to database returned status ="+ String.valueOf(id));
        db.close();

    }

    public List<String> getCitiesFromDB() {
        List<String> cities = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + CurrentWeatherEntity.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                cities.add(cursor.getString(cursor.getColumnIndex(CurrentWeatherEntity.COLUMN_CITY_NAME)));
            } while (cursor.moveToNext());
        }

        db.close();
        return cities;
    }

    public int getCityCount() {

        String countQuery = "SELECT  * FROM " + CurrentWeatherEntity.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void deleteCity(String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(CurrentWeatherEntity.TABLE_NAME, CurrentWeatherEntity.COLUMN_CITY_NAME + "=?", new String[]{city});
        Log.d("DatabaseHelper:", "Deleting from database returned status ="+ String.valueOf(status));
        db.close();
    }
}
