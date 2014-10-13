package com.example.flymperopoulos.loco;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by james on 10/9/14.
 */
public class LoginFragment extends Fragment {

    private Context context;
    public LoginFragment(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);


        EditText userName = (EditText)rootView.findViewById(R.id.username);
        Button loginButton = (Button)rootView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
//                userName = ;
                MyActivity activity = (MyActivity)getActivity();
                activity.changeToMainPage();
            }
        });

        return rootView;

    }
}
