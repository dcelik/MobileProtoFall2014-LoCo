package com.example.flymperopoulos.loco;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by james on 10/10/14.
 */
public class MapFragment extends Fragment {

    GoogleMap map;
    MapView mapView;

    public MapFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mapfragment_my, container, false);

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
        LatLng olin = new LatLng(42.29311, -71.262547);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(olin, 0));

        Marker college = map.addMarker(new MarkerOptions()
                .title("Olin College")
                .snippet("The School")
                .position(olin));
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