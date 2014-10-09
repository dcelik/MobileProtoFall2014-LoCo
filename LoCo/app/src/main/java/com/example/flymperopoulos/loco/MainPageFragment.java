package com.example.flymperopoulos.loco;

import android.app.Fragment;
import android.content.Context;
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
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by james on 10/9/14.
 */
public class MainPageFragment extends Fragment {
    private Context context;
    public MainPageFragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mainpagefragment_my, container, false);

        ListView requestListview = (ListView)rootView.findViewById(R.id.requests);
        ListView contactListview = (ListView)rootView.findViewById(R.id.contacts_list);
        ArrayList<String> list = new ArrayList<String>();
        list.add("hi");
        list.add("stuff");
        ArrayAdapter<String> requestAdapter = new ArrayAdapter<String>(getActivity(), R.layout.request_my, R.id.row, list);
        requestListview.setAdapter(requestAdapter);
// Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url = "https://maps.googleapis.com/maps/api/geocode/";
        String url1 = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=40.714224,-73.961452&key=AIzaSyClQMzD6V2eLi6CkqoSQzxCtarjlbcgSE4";

        String key = "AIzaSyClQMzD6V2eLi6CkqoSQzxCtarjlbcgSE4";
        String latlng= "40.714224,-73.961452";

//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final TextView display = (TextView)rootView.findViewById(R.id.display);


// Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(response.toString(), "What is in the json\n\n\n\n");
                        try {
                            String address = response.getJSONArray("results").get(0).toString();


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



        return rootView;

    }
}
