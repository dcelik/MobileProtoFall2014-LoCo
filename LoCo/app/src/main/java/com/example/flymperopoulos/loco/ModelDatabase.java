package com.example.flymperopoulos.loco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by dcelik on 10/11/14.
 */
public class ModelDatabase extends SQLiteOpenHelper {
    //Table Name
    public static final String TABLE_NAME = "UserData";

    //Table Fields
    public static final String USER_NAME = "username";
    public static final String USER_NUM = "phonenumber";
    public static final String USER_LAT = "lattitude";
    public static final String USER_LONG = "longitude";

    //Database Info
    private static final String DATABASE_NAME = "ChatAppDatabase";
    private static final int DATABASE_VERSION = 1;

    // ModelDatabase creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + USER_NAME + " TEXT NOT NULL, "
            + USER_NUM + " TEXT NOT NULL UNIQUE, "
            + USER_LAT + " FLOAT, "
            + USER_LONG + " FLOAT );";

    //Default Constructor
    public ModelDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //OnCreate Method - creates the ModelDatabase
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);

    }
    @Override
    //OnUpgrade Method - upgrades ModelDatabase if applicable
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(ModelDatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}