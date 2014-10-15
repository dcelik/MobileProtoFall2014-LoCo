package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 10/9/14.
 */
public class LoginFragment extends Fragment {

    private Context context;
    public LoginFragment(){}
    Firebase fb;
    HandlerDatabase db;
    User currentUser;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        db = ((MyActivity)getActivity()).db;
        fb = ((MyActivity)getActivity()).fb;
        currentUser = ((MyActivity)getActivity()).currentUser;

        final EditText userName = (EditText)rootView.findViewById(R.id.username);
        final EditText userPhone = (EditText)rootView.findViewById(R.id.phone);

        Button loginButton = (Button)rootView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                MyActivity activity = (MyActivity)getActivity();
                final String username = userName.getText().toString();
                final String phonenumber = userPhone.getText().toString();
                currentUser.setName(username);
                currentUser.setPhoneNumber(phonenumber);
                Query userquery = fb;
                userquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<User> users = new ArrayList<User>();
                        Boolean found = false;
                        for (DataSnapshot child : dataSnapshot.getChildren()){
                            if(!found) {
                                User grabbedUser = child.getValue(User.class);
                                users.add(grabbedUser);
                                if (grabbedUser.getPhoneNumber().equals(phonenumber)) {
                                    found = true;
                                    currentUser.setLongitude(grabbedUser.getLongitude());
                                    currentUser.setLatitude(grabbedUser.getLatitude());
                                    currentUser.setName(grabbedUser.getName());
                                }
                            }
                        }
                        if(!found){
                            fb.child(phonenumber).setValue(currentUser);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });

                activity.changeToMainPage();
            }
        });

        return rootView;

    }
}
