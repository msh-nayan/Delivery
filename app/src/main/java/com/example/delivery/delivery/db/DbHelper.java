package com.example.delivery.delivery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.delivery.delivery.entity.Delivery;
import com.example.delivery.delivery.entity.DeliveryLocation;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private String TAG = DbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_DELIVERY = "DB_DELIVERY";
    private static final String TABLE_DELIVERY = "TABLE_DELIVERY";

    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_LATITUDE = "lat";
    private static final String KEY_LONGITUDE = "lng";
    private static final String KEY_ADDRESS = "address";

    public DbHelper(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASE_DELIVERY, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_DELIVERY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DESCRIPTION + " TEXT," + KEY_IMAGE_URL
                + " TEXT," + KEY_LATITUDE + " REAL," + KEY_LONGITUDE + " REAL," + KEY_ADDRESS + " TEXT" + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY);
        onCreate(db);
    }

    public void insertDelivery(Delivery delivery) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlQuery = "SELECT * FROM " + TABLE_DELIVERY + " WHERE " + KEY_ID + " = " + "\"" + delivery.getId() + "\"";
        Cursor c = db.rawQuery(sqlQuery, null);

        if (c != null && c.getCount() != 0) {
            return;
        }

        if (isExist(delivery.getId())) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, delivery.getId());
        contentValues.put(KEY_DESCRIPTION, delivery.getDescription());
        contentValues.put(KEY_IMAGE_URL, delivery.getImageUrl());
        contentValues.put(KEY_LATITUDE, delivery.getLocation().getLat());
        contentValues.put(KEY_LONGITUDE, delivery.getLocation().getLng());
        contentValues.put(KEY_ADDRESS, delivery.getLocation().getAddress());

        db.insert(TABLE_DELIVERY, null, contentValues);
        db.close();
    }

    public List<Delivery> getDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_DELIVERY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Delivery delivery = new Delivery();
                delivery.setId(Integer.parseInt(cursor.getString(0)));
                delivery.setDescription(cursor.getString(1));
                delivery.setImageUrl(cursor.getString(2));

                DeliveryLocation location = new DeliveryLocation();
                location.setLat(cursor.getDouble(3));
                location.setLng(cursor.getDouble(4));
                location.setAddress(cursor.getString(5));
                delivery.setLocation(location);

                deliveries.add(delivery);
            } while (cursor.moveToNext());
        }

        return deliveries;
    }

    public boolean isExist(int id) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_DELIVERY + " where " + KEY_ID + " = " + id;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
