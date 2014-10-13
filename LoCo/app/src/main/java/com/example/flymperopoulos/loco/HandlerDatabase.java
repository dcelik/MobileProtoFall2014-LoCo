package com.example.flymperopoulos.loco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


/**
 * Created by dcelik on 10/10/14.
 */
public class HandlerDatabase {

    //Database Model
    private ModelDatabase model;

    //Database
    private SQLiteDatabase database;

    //All Fields
    private String[] allColumns = {
            ModelDatabase.USER_NAME,
            ModelDatabase.USER_NUM,
            ModelDatabase.USER_LAT,
            ModelDatabase.USER_LONG,
    };

    //Public Constructor - create connection to Database
    public HandlerDatabase(Context context){
        model = new ModelDatabase(context);
    }

    /**
     * Add
     */
    public void addUserToDatabase(User user){
        ContentValues values = new ContentValues();
        values.put(ModelDatabase.USER_NAME, user.getName());
        values.put(ModelDatabase.USER_NUM, user.getPhoneNumber());
        values.put(ModelDatabase.USER_LAT, user.getLatitude());
        values.put(ModelDatabase.USER_LONG, user.getLongitude());
        database.insertWithOnConflict(ModelDatabase.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }
    public void updateUser(User user){
        ContentValues values = new ContentValues();
        values.put(ModelDatabase.USER_NAME, user.getName());
        values.put(ModelDatabase.USER_NUM, user.getPhoneNumber());
        values.put(ModelDatabase.USER_LAT, user.getLatitude());
        values.put(ModelDatabase.USER_LONG, user.getLongitude());
        database.update(ModelDatabase.TABLE_NAME, values, ModelDatabase.USER_NUM + " = " + user.getPhoneNumber(), null);
    }

    /**
     * Get
     */
    public ArrayList<User> getAllUsers(){
        return sweepCursor(database.query(ModelDatabase.TABLE_NAME, allColumns,null, null, null, null,null));
    }

    public void deleteAllChats(){
        ArrayList<User> users = getAllUsers();
        for(User u:users)
        {
            database.delete(
                    ModelDatabase.TABLE_NAME,
                    ModelDatabase.USER_NUM + " like '%" + u.getPhoneNumber() + "%'",
                    null
            );
        }
    }

    public User getUserByPhoneNumber(String num){
        ArrayList<User> list = sweepCursor(database.query(
                ModelDatabase.TABLE_NAME,
                allColumns,
                ModelDatabase.USER_NUM + " like '%" + num + "%'",
                null, null, null,null
        ));
        if(list.size()==0){
            return null;
        }
        return list.get(0);
    }

    /**
     * Delete
     */
    public void deleteUserByPhoneNumber(String num){
        database.delete(
                ModelDatabase.TABLE_NAME,
                ModelDatabase.USER_NUM + " like '%" + num + "%'",
                null
        );
    }

    /**
     * Additional Helpers
     */
    //Sweep Through Cursor and return a List of Chats
    private ArrayList<User> sweepCursor(Cursor cursor){
        ArrayList<User> myUsers = new ArrayList<User>();

        //Get to the beginning of the cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            User user = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getDouble(3)
            );
            myUsers.add(user);
            cursor.moveToNext();
        }
        return myUsers;
    }

    //Get Writable Database - open the database
    public void open(){
        database = model.getWritableDatabase();
    }
}
