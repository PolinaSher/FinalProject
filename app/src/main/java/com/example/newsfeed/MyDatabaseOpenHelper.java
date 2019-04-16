package com.example.newsfeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "News";
    public static final String COL_ID = "_id";
    public static final String COL_MESSAGE = "MESSAGE";
    public static final String COL_IS_SEND = "TITLE";
    public static final String COL_URL = "URL";


    public MyDatabaseOpenHelper(Context ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MESSAGE + " TEXT, " + COL_URL + " TEXT, "+ COL_IS_SEND + " TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public long insertNewsFeed(String msg, String link, String title) {
        SQLiteDatabase db = getWritableDatabase();
        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string name in the NAME column:
        newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, msg);
        newRowValues.put(MyDatabaseOpenHelper.COL_IS_SEND, title);
        newRowValues.put(MyDatabaseOpenHelper.COL_URL, link);
        //insert in the database:
        return db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
    }

}
