package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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


import org.json.JSONException;
import org.json.JSONObject;

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
<<<<<<< HEAD
    ArrayList<User> listUsers;

    Firebase fb;
    HandlerDatabase db;

=======
    User currentUser;
>>>>>>> 836a790a38278abd777c80ff0ae700d984b8c933
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

        ArrayList<String> list = new ArrayList<String>();
        list.add("hi");
        list.add("stuff");
        ArrayAdapter<String> requestAdapter = new ArrayAdapter<String>(getActivity(), R.layout.request_my, R.id.row, list);
        requestListview.setAdapter(requestAdapter);
// Instantiate the RequestQueue.

        Button changetomap = (Button) rootView.findViewById(R.id.changtomap);
        changetomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivity activity = (MyActivity)getActivity();
                activity.changeToMap();
            }
        });

        loadDatabaselist();
//        Log.v(listUsers.get(1).toString(), "this");


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String latlng = "latlng=42.292657,-71.263114";

        String key = "AIzaSyBeknu4C9t4Dii04H8imC-ygXLvprFLfv4";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&" + latlng + "&key=" + key;

        latituteField = (TextView) rootView.findViewById(R.id.lat);
        longitudeField = (TextView) rootView.findViewById(R.id.longi);
<<<<<<< HEAD

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
=======
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
>>>>>>> 836a790a38278abd777c80ff0ae700d984b8c933
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

//        Button changeToContact = (Button) rootView.findViewById(R.id.displaycontact);
//        changeToContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MyActivity activity = (MyActivity)getActivity();
//                activity.changeToContact();
//            }
//        });

// Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String stuff = response.getJSONArray("results").getJSONObject(1).get("formatted_address").toString();
//                            String address = response.get("result").toString();
                            //Log.d(stuff, "What is in the json\n\n\n\n");
                            display.setText(stuff);

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

        ArrayList<PhoneContactInfo> contacts = readContacts();
        PhoneContactInfoAdapter contactInfoAdapter= new PhoneContactInfoAdapter(getActivity(), R.layout.contact_item, contacts);
        ListView contactListview = (ListView)rootView.findViewById(R.id.contacts_list);
        contactListview.setAdapter(contactInfoAdapter);

        return rootView;
    }

    public ArrayList<PhoneContactInfo> readContacts() {
        Log.d("START", "Getting all Contacts");
        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<PhoneContactInfo>();
        PhoneContactInfo phoneContactInfo=null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri,
                new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone._ID},
                null, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String contactNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));


            phoneContactInfo = new PhoneContactInfo();
            phoneContactInfo.setPhoneContactID(phoneContactID);
            phoneContactInfo.setContactName(contactName);
            phoneContactInfo.setContactNumber(contactNumber);
            if (phoneContactInfo != null)
            {
                arrContacts.add(phoneContactInfo);
            }
            phoneContactInfo = null;
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        Log.d("END","Got all Contacts");
        return arrContacts;
    }
    public void loadDatabaselist() {

        fb.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to Firebase
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Map<String, User> newPost = (Map<String, User>) snapshot.getValue();
                System.out.println(newPost);
                System.out.println(newPost.get("name"));
                System.out.println(newPost.get("phoneNumber"));
                System.out.println(newPost.get("latitude"));
                System.out.println(newPost.get("longitude"));

                String userName = newPost.get("name").toString();
                String userPhone = newPost.get("phoneNumber").toString();
//                double lat = newPost.get("latitude").getLatitude();
//                double longi = newPost.get("latitude").getLongitude();
                double lat = Double.parseDouble(newPost.get("latitude").toString());
                double longi = Double.parseDouble(newPost.get("latitude").toString());


                User newUser = new User(userName, userPhone,lat,longi);
                listUsers.add(newUser);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                String title = (String) snapshot.child("title").getValue();
                System.out.println("The updated post title is " + title);
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                String title = (String) snapshot.child("title").getValue();
                System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String f) {
                String title = (String) snapshot.child("title").getValue();
                System.out.println("The blog post titled " + title + " has been deleted");
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println(error.getMessage());
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
