package com.example.finalproject_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FlightTracker_DB";

    private static int VERSION_NUM = 2;

    private static String DEPARTURE_CODE;

    private static String STATUS;

    private static double HORIZONTAL_SPEED;

    private static double ALTITUDE;

    private static String LOCATION;



    private static int ID;

    SQLiteDatabase db;




    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" +  ID + " INTEGER PRIMARY KEY autoincrement, " +
        DEPARTURE_CODE + " TEXT, " + STATUS + " TEXT, " + HORIZONTAL_SPEED + " TEXT, " + ALTITUDE + " TEXT, " + LOCATION + " TEXT);" );

        Log.i("AirLine Data", "Create database");






    }





    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME + ";");
        onCreate(db);

        Log.i("Airline Data", "Recreate database");



    }
}
