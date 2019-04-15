package com.example.finalproject_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.media.MediaFormat.KEY_HEIGHT;
import static java.security.AccessController.getContext;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private SQLiteDatabase database;
    Cursor cursor;
    public static final String DATABASE_NAME = "Articles_DB";
    public static final int VERSION_NUM = 4;
    public static final String TABLE_NAME = "Articles";

    public static final String COL_ID = "ID";
    public static final String COL_SNIPPET = "Snippet";
    private static final String[] COLUMNS = { COL_ID, COL_SNIPPET};

    //To access to db instantiate subclass with context
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

   //create a db using SQL helper
    //Class DBHelper overrides four methods (must be at list three)
   @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER 	PRIMARY KEY AUTOINCREMENT, " +
                COL_SNIPPET + " TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        database = db;
    }

    public Integer deleteOne(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] { id });
       // db.close();
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Articles getArticle (int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,COLUMNS, "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if(cursor != null)
            cursor.moveToFirst();
        Articles articles = new Articles();
        articles.setId(Integer.parseInt(cursor.getString(0)));
        articles.setArticles(cursor.getString(1));

        return articles;
    }

//    public List<Article> allArticles(){
//        List<Article> articles=new LinkedList<Article>();
//        String query = "SELECT * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        Articles article=null;
//
//        if(cursor.moveToFirst()){
//            do {
//                article = new Articles();
//                article.setId(Integer.parseInt(cursor.getString(0)));
//                article.setArticles(cursor.getString(1));
//            } while(cursor.moveToNext());
//        }
//        return articles;//list
//    }




    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT*FROM " + TABLE_NAME, null);
        return res;
    }



    //put info into database
    public void addData(String snip) {
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_SNIPPET, snip);

        database.insert(TABLE_NAME, null, values);
        Log.i("MMMMMMMMMMMMMMMMMM", snip);
        Log.d(TAG, "addData: Adding: " + snip+" into"+ TABLE_NAME );
        //long newRowId  = db.insert(TABLE_NAME, null, values);//insert method will return the ID for the newly created row


        Log.i("DATABASE INSERT","SUCCESSFULLY");
    }

    SQLiteDatabase db = this.getReadableDatabase();

    public Cursor getMessages(){
        List articleID = new ArrayList<>();

        while (cursor.moveToNext()) {
            long itemID = cursor.getLong(
                    cursor.getColumnIndexOrThrow(COL_ID));
            articleID.add(itemID);
        }
        cursor.close();
        return database.query(false, TABLE_NAME,null,null,null,null,null,null,null) ;
    }
}
