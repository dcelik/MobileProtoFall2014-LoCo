package com.example.flymperopoulos.loco;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by james on 10/10/14.
 */
public class MapFragment extends Fragment {

    GoogleMap map;
    MapView mapView;
    ArrayList<User> contactLocations;
    User currentUser;

    public MapFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mapfragment_my, container, false);

        contactLocations = ((MyActivity)getActivity()).contactLocations;
        currentUser = ((MyActivity)getActivity()).currentUser;

        Button backBtn = (Button)rootView.findViewById(R.id.backMap);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivity activity = (MyActivity) getActivity();
                activity.changeToMainFragment();
            }
        });

        mapView = ((MapView) rootView.findViewById(R.id.map));
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(getActivity());

        map = mapView.getMap();
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                setupMap();
            }
        });

        return rootView;
    }

    private void setupMap() {
        map.setMyLocationEnabled(true);
        LatLng userLocation = new LatLng(currentUser.getLatitude(), currentUser.getLongitude());

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 0));

        addMarker(contactLocations);

    }

    public void addMarker(ArrayList<User> userList){
        for(User u: userList) {
            LatLng latLng = new LatLng(u.getLatitude(), u.getLongitude());

            Marker marker = map.addMarker(new MarkerOptions()
                    .title(u.getName())
                    .position(latLng));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


}