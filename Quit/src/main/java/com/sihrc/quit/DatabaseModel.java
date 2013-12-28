package com.sihrc.quit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by chris on 12/27/13.
 */
public class DatabaseModel extends SQLiteOpenHelper {
    //Table Name
    public static final String TABLE_NAME = "GIFs";

    //Table Fields
    public static final String GIF_URL = "url";
    public static final String GIF_INDEX = "index";
    public static final String GIF_IMAGE = "image";

    //Database Info
    private static final String DATABASE_NAME = "QuitDatabase";
    private static final int DATABASE_VERSION = 1;

    // DatabaseModel creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + GIF_URL + " TEXT NOT NULL, "
            + GIF_INDEX + " TEXT NOT NULL, "
            + GIF_IMAGE + " BLOB );";

    //Default Constructor
    public DatabaseModel(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //OnCreate Method - creates the DatabaseModel
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);

    }
    @Override
    //OnUpgrade Method - upgrades DatabaseModel if applicable
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(DatabaseModel.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}