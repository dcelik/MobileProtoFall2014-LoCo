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
import android.widget.Toast;

/**
 * Created by james on 10/9/14.
 */
public class LoginFragment extends Fragment {

    private Context context;
    public LoginFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my, container, false);


        final EditText userName = (EditText)rootView.findViewById(R.id.username);
        final EditText userPhone = (EditText)rootView.findViewById(R.id.phone);

        Button loginButton = (Button)rootView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().isEmpty() && userPhone.getText().toString().isEmpty()){
                    Toast.makeText(context, "Enter a valid username and phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPhone.getText().toString().length()<10 || userName.getText().toString().isEmpty() ){
                    Toast.makeText(context, "Enter a 10-digit number with no spaces or dashes", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Welcome, " + userName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                MyActivity activity = (MyActivity)getActivity();
                activity.changeToMainPage();
            }
        });
        return rootView;
    }
}
