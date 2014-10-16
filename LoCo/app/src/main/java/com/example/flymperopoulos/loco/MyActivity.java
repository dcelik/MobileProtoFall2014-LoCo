package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


public class MyActivity extends Activity {

    NotificationManager NM;
    HandlerDatabase db;
    Firebase fb;
    User currentUser;
    ArrayList<User> mutualContacts;
    ArrayList<User> contactLocations;
    int notificatoinid;

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
        mutualContacts = new ArrayList<User>();
        contactLocations = new ArrayList<User>();
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

    @SuppressWarnings("deprecation")
    public void popMessage(String username){
        String title = "New request!";
        String subject = username + " requests your location!";
        String body = "";
        NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification(R.drawable.iclogo,title,System.currentTimeMillis());
        PendingIntent pending= PendingIntent.getActivity(
                getApplicationContext(), 0, new Intent(), 0);
        notify.setLatestEventInfo(getApplicationContext(), subject, body, pending);
        NM.notify(notificatoinid,notify);
        notificatoinid++;
    }

    public void notificationStart(){
        fb.child(currentUser.getPhoneNumber()).child("flag").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    fb.child(dataSnapshot.getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            popMessage(dataSnapshot.getValue(User.class).getName());
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
