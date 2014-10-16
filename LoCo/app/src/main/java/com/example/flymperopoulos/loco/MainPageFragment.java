package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by james on 10/9/14.
 */

public class MainPageFragment extends Fragment implements LocationListener{

    private LocationManager locationManager;
    private Context context;
    private TextView latitudeField;
    private TextView longitudeField;
    UserAdapter contactInfoAdapter;
    ArrayList<User> mutualContacts;
    ArrayList<User> requestContacts;
    ArrayList<User> contactLocations;
    String key = "AIzaSyBeknu4C9t4Dii04H8imC-ygXLvprFLfv4";
    RequestQueue queue;

    Firebase fb;
    HandlerDatabase db;
    User currentUser;
    public MainPageFragment(){
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mainpagefragment_my, container, false);
        currentUser = ((MyActivity)getActivity()).currentUser;

        ListView requestListview = (ListView)rootView.findViewById(R.id.requests);
        db = ((MyActivity)getActivity()).db;
        fb = ((MyActivity)getActivity()).fb;
        mutualContacts = ((MyActivity)getActivity()).mutualContacts;
        requestContacts = ((MyActivity)getActivity()).requestContacts;
        contactLocations = ((MyActivity)getActivity()).contactLocations;
        final UserAdapter requestAdapter = new UserAdapter(getActivity(), R.layout.contact_item, requestContacts);

        ListView contactListview = (ListView)rootView.findViewById(R.id.contacts_list);

        queue = Volley.newRequestQueue(getActivity());

        requestListview.setAdapter(requestAdapter);
        requestListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Send your location?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        final String sendUser = requestAdapter.getItem(i).getPhoneNumber();
                        currentUser.removeFromFlag(sendUser);
                        for(User u: mutualContacts){
                            if(currentUser.getPhoneNumber().equals(u.getPhoneNumber())){
                                u.removeFromFlag(sendUser);
                            }
                            contactInfoAdapter.notifyDataSetChanged();
                        }
                        //

                        fb.child(currentUser.getPhoneNumber()).setValue(currentUser);

                        Log.d("debug",fb.child(sendUser).child("requestConfirmed").toString());
                        ArrayList<String> temp = new ArrayList<String>();
                        temp.add(currentUser.getPhoneNumber());

                        fb.child(sendUser).child("requestConfirmed").setValue(temp);
//                        fb.child(sendUser).child("requestConfirmed").push().setValue(currentUser.getPhoneNumber());
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        Button changetomap = (Button) rootView.findViewById(R.id.changtomap);
        changetomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivity activity = (MyActivity)getActivity();
                activity.changeToMap();
            }
        });

        latitudeField = (TextView) rootView.findViewById(R.id.lat);
        longitudeField = (TextView) rootView.findViewById(R.id.longi);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        final Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 0, 0 , this);

        if (location != null) {
            onLocationChanged(location);
        } else if(currentUser.getLongitude()==null && currentUser.getLatitude()==null){
            latitudeField.setText("Location not available");
            longitudeField.setText("Location not available");
        } else {
            latitudeField.setText(String.valueOf(currentUser.getLatitude()));
            longitudeField.setText(String.valueOf(currentUser.getLongitude()));
        }

        final TextView display = (TextView)rootView.findViewById(R.id.display);

        String latlng = "latlng=42.292657,-71.263114";


        String url = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&" + latlng + "&key=" + key;

//                      https://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=42.29306196,-71.26257942&key=AIzaSyBeknu4C9t4Dii04H8imC-ygXLvprFLfv4
//        String url1 = "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyBeknu4C9t4Dii04H8imC-ygXLvprFLfv4";
        String url2 = "https://maps.googleapis.com/maps/api/geocode/json?latlng=42.29306196,-71.26257942&key=AIzaSyBeknu4C9t4Dii04H8imC-ygXLvprFLfv4";


// Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String address = response.getJSONArray("results").getJSONObject(1).get("formatted_address").toString();
                            display.setText(address);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonRequest);

//      GETTING THE CONTACTS
        compareDatabaselist(readContacts());
        contactInfoAdapter= new UserAdapter(getActivity(), R.layout.contact_item, mutualContacts);
        contactListview.setAdapter(contactInfoAdapter);
        contactListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Send a Request?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        User requestUser = contactInfoAdapter.getItem(i);
                        requestUser.addToFlag(currentUser.getPhoneNumber());
                        fb.child(requestUser.getPhoneNumber()).setValue(requestUser);

                        Toast.makeText(context, "Your request has been sent", Toast.LENGTH_SHORT).show();
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestContacts.clear();
                if(dataSnapshot!=null) {
                    Log.d("does it call", "value Listener?");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if(child.getName().equals(currentUser.getPhoneNumber())) {
                            ArrayList<String> flags = child.getValue(User.class).getFlag();
                            for(String s: flags){
                                requestContacts.add(dataSnapshot.child(s).getValue(User.class));
                            }
                            requestAdapter.notifyDataSetChanged();
                        }
                        Log.d("does it call", "end of the first for loop");
                        for(User u: mutualContacts){
                            Log.d("does it call", "start of the second for loop");
                            Log.d("Number", child.getValue(User.class).getPhoneNumber());
                            if(child.getValue(User.class).getRequestConfirmed().contains(u.getPhoneNumber())){
                                contactLocations.add(u);
                                child.getValue(User.class).getRequestConfirmed().remove(u.getPhoneNumber());
                                Toast.makeText(context, u.getName()+ " has responded", Toast.LENGTH_LONG).show();
                                fb.child(currentUser.getPhoneNumber()).child("requestConfirmed").setValue(null);
                                String url = getUrl(getLatLng(u), key);
                                Log.d("address", url);
                                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    Log.d("address", "printing1");
                                                    String address = response.getJSONArray("results").getJSONObject(1).get("formatted_address").toString();
                                                    display.setText(address);
                                                    Log.d("address", address);
                                                    Log.d("address", "printing1a");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                });
                                queue.add(jsonRequest);
                                Log.d("address", "printing2");

//                                a way to remove the value from the confirmed list without doing the setValue thing

                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }

    public ArrayList<User> readContacts() {
        Log.d("START", "Getting all Contacts");
        ArrayList<User> arrContacts = new ArrayList<User>();
        User user =null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri,
                new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone._ID},null, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String contactNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNumber = contactNumber.replaceAll("[^[0-9]*$]", "");
            if(contactNumber.length()>10){
                contactNumber = contactNumber.substring(1);
            }
            user = new User();
            user.setName(contactName);
            user.setPhoneNumber(contactNumber);
            if (user != null)
            {
                arrContacts.add(user);
            }
            user = null;
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        Log.d("END","Got all Contacts");
        return arrContacts;
    }
//    comparing sets instead of Users
    public void compareDatabaselist(final ArrayList<User> contacts) {
        fb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mutualContacts.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    for (User u : contacts) {
                        if (child.getValue(User.class).getPhoneNumber().equals(u.getPhoneNumber())) {
                            if(!mutualContacts.contains(child.getValue(User.class))){
                                mutualContacts.add(child.getValue(User.class));
                            }
                        }
                    }
                }
                contactInfoAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        currentUser.setLatitude(lat);
        currentUser.setLongitude((lng));
        latitudeField.setText(String.valueOf(currentUser.getLatitude()));
        longitudeField.setText(String.valueOf(currentUser.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String s) {
//        Toast.makeText(this, "Enable"+ provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
    }
//    String url = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&" + latlng + "&key=" + key;
//    String latlng = "latlng=42.292657,-71.263114";

    public String getUrl(String latlng, String key){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?" + latlng + "&key=" + key;
        return url;
    }

    public String getLatLng(User user){
        Log.d("actual address", "latlng="+ user.getLatitude().toString() +","+ user.getLongitude().toString());
        return "latlng="+ user.getLatitude().toString() +","+ user.getLongitude().toString();
    }


}
