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
import android.widget.Toast;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

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
                if (userName.getText().toString().isEmpty() && userPhone.getText().toString().isEmpty()){
                    Toast.makeText(context, "Enter a valid username and phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPhone.getText().toString().length()<10 || userName.getText().toString().isEmpty() ){
                    Toast.makeText(context, "Enter a 10-digit number with no spaces or dashes", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Welcome, " + userName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                final MyActivity activity = (MyActivity)getActivity();
                final String username = userName.getText().toString();
                final String phonenumber = userPhone.getText().toString();
                currentUser.setName(username);
                currentUser.setPhoneNumber(phonenumber);
//                Query userquery = fb;
//                Log.i("DebugDebug", phonenumber);
                fb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i("DebugDebug", "Data Changed");
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                            Log.i("DebugDebug", child.getName());
                            if (child.getName().equals(phonenumber)) {
                                User grabbedUser = child.getValue(User.class);
                                currentUser.setLongitude(grabbedUser.getLongitude());
                                currentUser.setLatitude(grabbedUser.getLatitude());
                                currentUser.setName(grabbedUser.getName());

                                activity.changeToMainPage();

                                return;
                            }

                        }
                        fb.child(phonenumber).setValue(currentUser);
                        activity.changeToMainPage();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.i("DebugDebug", "The read failed: " + firebaseError.getMessage());
                    }
                });

            }
        });
        return rootView;
    }
}
