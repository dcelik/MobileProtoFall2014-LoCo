package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

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


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        db = ((MyActivity)getActivity()).db;
        fb = ((MyActivity)getActivity()).fb;


        final EditText userName = (EditText)rootView.findViewById(R.id.username);
        final EditText userPhone = (EditText)rootView.findViewById(R.id.phone);

        Button loginButton = (Button)rootView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                MyActivity activity = (MyActivity)getActivity();
                String username = userName.getText().toString();
                String userphone = userPhone.getText().toString();
                User  loginuser = new User(username,userphone,0.0,0.0);
                Map<String, User> newuser = new HashMap<String, User>();
                newuser.put(userphone, loginuser);
                fb.setValue(newuser);
                activity.changeToMainPage();
            }
        });

        return rootView;

    }
}
