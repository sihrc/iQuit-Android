package com.sihrc.quit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by chris on 12/27/13.
 */
public class DBHandler {
    //Database Model
    private DatabaseModel model;

    //Database
    private SQLiteDatabase database;

    //All Fields
    private String[] allColumns = {
            DatabaseModel.GIF_URL,
            DatabaseModel.GIF_INDEX,
            DatabaseModel.GIF_IMAGE
    };

    //Public Constructor - create connection to Database
    public DBHandler(Context context){
        model = new DatabaseModel(context);
    }

    /**
     * Add
     */
    public void addGIFtoDatabase(GIF gif){
        ContentValues values = new ContentValues();
        values.put(DatabaseModel.GIF_URL, gif.url);
        for (int i = 0; i < gif.images.size(); i++){
            values.put(DatabaseModel.GIF_IMAGE, gif.images.get(i));
            values.put(DatabaseModel.GIF_INDEX, i);
            database.insert(DatabaseModel.TABLE_NAME, null, values);
        }
    }

    /**
     * Get
     */
    public ArrayList<byte[]> getImagesForGIF(String url){
        return sweepCursor(database.query(
                DatabaseModel.TABLE_NAME,
                allColumns,
                DatabaseModel.GIF_URL + " like '" + url + "'",
                null, null, null,
                DatabaseModel.GIF_INDEX));
    }

    /**
     * Delete
     */
    public void deleteGIFbyURL(String url){
        database.delete(
                DatabaseModel.TABLE_NAME,
                DatabaseModel.GIF_URL + " like '%" + url + "%'",
                null
        );
    }

    /**
     * Additional Helpers
     */
    //Sweep Through Cursor and return a List of Kitties
    private ArrayList<byte[]> sweepCursor(Cursor cursor){
        ArrayList<byte[]> images = new ArrayList<byte[]>();

        //Get to the beginning of the cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            images.add(cursor.getBlob(1));
            //Go on to the next GIF
            cursor.moveToNext();
        }
        return images;
    }

    //Get Writable Database - open the database
    public void open(){
        database = model.getWritableDatabase();
    }
}