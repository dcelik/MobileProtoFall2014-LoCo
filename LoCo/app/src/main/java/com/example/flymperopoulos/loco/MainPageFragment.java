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
import android.widget.EditText;
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
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by james on 10/9/14.
 */

public class MainPageFragment extends Fragment implements LocationListener{

    private LocationManager locationManager;
    private Context context;
    private TextView latituteField;
    private TextView longitudeField;
    UserAdapter contactInfoAdapter;
    Map<String, User> mutualHashmap;


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

        final ArrayList<User> list = new ArrayList<User>();
        final UserAdapter requestAdapter = new UserAdapter(getActivity(), R.layout.contact_item, list);

        requestListview.setAdapter(requestAdapter);
        fb.child(currentUser.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                list.addAll(dataSnapshot.getValue(User.class).getFlag());
                requestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        requestListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Send your location?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        final User sendUser = requestAdapter.getItem(i);
                        currentUser.removeFromFlag(sendUser);
                        fb.child(currentUser.getPhoneNumber()).setValue(currentUser);

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

        latituteField = (TextView) rootView.findViewById(R.id.lat);
        longitudeField = (TextView) rootView.findViewById(R.id.longi);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        final Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 0, 0 , this);

        if (location != null) {
            onLocationChanged(location);
        } else if(currentUser.getLongitude()==null && currentUser.getLatitude()==null){
            latituteField.setText("Location not available");
            longitudeField.setText("Location not available");
        } else {
            latituteField.setText(String.valueOf(currentUser.getLatitude()));
            longitudeField.setText(String.valueOf(currentUser.getLongitude()));
        }

        final TextView display = (TextView)rootView.findViewById(R.id.display);

        String latlng = "latlng=42.292657,-71.263114";

        String key = "AIzaSyBeknu4C9t4Dii04H8imC-ygXLvprFLfv4";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&" + latlng + "&key=" + key;
        RequestQueue queue = Volley.newRequestQueue(getActivity());

// Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stuff = response.getJSONArray("results").getJSONObject(1).get("formatted_address").toString();
//                            String address = response.get("result").toString();
                            //Log.d(stuff, "What is in the json\n\n\n\n");
                            String currentPosition = "Current Location " + stuff;
                            display.setText(currentPosition);

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
        //Log.d("mutual", mutualContacts.toString());
        contactInfoAdapter= new UserAdapter(getActivity(), R.layout.contact_item, new ArrayList<User>());
        ListView contactListview = (ListView)rootView.findViewById(R.id.contacts_list);
        contactListview.setAdapter(contactInfoAdapter);

        contactListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Send a Request?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        final User requestUser = contactInfoAdapter.getItem(i);
                        Log.d("current User", currentUser.getPhoneNumber().toString());
                        Log.d("request User", requestUser.getPhoneNumber().toString());
                        requestUser.addToFlag(currentUser);
//                        fb.child(requestUser.getPhoneNumber()).child("flag").
                        fb.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()){
                                    User grabbedUser = child.getValue(User.class);
                                    if (grabbedUser.getPhoneNumber().equals(requestUser.getPhoneNumber())) {
                                        fb.child(currentUser.getPhoneNumber()).setValue(currentUser);
                                    }
                                }
                                Toast.makeText(context, "Your request has been sent", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
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

        return rootView;
    }

    public ArrayList<User> getRequests(){
        return null;
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
                ArrayList<String> listUsers = new ArrayList<String>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    listUsers.add(child.getValue(User.class).getPhoneNumber());
                }
                for (User u : contacts) {
                    if (listUsers.contains(u.getPhoneNumber())) {
                        contactInfoAdapter.add(u);
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

    public String getAddress(){
        return null;
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
        latituteField.setText(String.valueOf(currentUser.getLatitude()));
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

    public String getUrl(String latlng, String key){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&" + latlng + "&key=" + key;
        return url;
    }

    public String getLatLng(User user){
        return user.getLatitude().toString() + user.getLongitude().toString();
    }


}
