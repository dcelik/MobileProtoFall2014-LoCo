package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by james on 10/9/14.
 */

public class MainPageFragment extends Fragment implements LocationListener{

    private LocationManager locationManager;
    private Context context;
    private TextView latituteField;
    private TextView longitudeField;
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
        ListView contactListview = (ListView)rootView.findViewById(R.id.contacts_list);
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String latlng = "latlng=42.292657,-71.263114";

        String key = "AIzaSyBeknu4C9t4Dii04H8imC-ygXLvprFLfv4";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&" + latlng + "&key=" + key;

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

        Button changeToContact = (Button) rootView.findViewById(R.id.displaycontact);
        changeToContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivity activity = (MyActivity)getActivity();
                activity.changeToContact();
            }
        });

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


        return rootView;
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


}
