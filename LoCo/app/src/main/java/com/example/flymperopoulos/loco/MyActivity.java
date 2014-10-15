package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;



public class MyActivity extends Activity {

    HandlerDatabase db;
    Firebase fb;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
        db = new HandlerDatabase(this);
        fb = new Firebase("https://scorching-fire-1825.firebaseio.com/Users");
        currentUser = new User(null,null,0.0,0.0);
        db.open();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeToMainPage(){
        MainPageFragment fragment = new MainPageFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void changeToMap(){
        MapFragment fragment = new MapFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
    public void changeToContact(){
        ContactsFragment fragment = new ContactsFragment ();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
    public void goBack()
    {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }
    public void changeToMainFragment(){
        MainPageFragment fragment = new MainPageFragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


}
