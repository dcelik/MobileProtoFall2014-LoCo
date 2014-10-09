package com.example.flymperopoulos.loco;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by james on 10/9/14.
 */
public class MainPageFragment extends Fragment {
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

        return rootView;

    }
}
