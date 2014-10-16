package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * Created by james on 10/9/14.
 */
public class LoginFragment extends Fragment{

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
        final RelativeLayout relativeLayout = (RelativeLayout)rootView.findViewById(R.id.rellayout);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v==relativeLayout){
                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(userPhone.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        Button loginButton = (Button)rootView.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().isEmpty() || userPhone.getText().toString().isEmpty()){
                    Toast.makeText(context, "Enter a valid username or phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPhone.getText().toString().length()<10){
                    Toast.makeText(context, "Enter a 10-digit number with no spaces or dashes", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, "Welcome, " + userName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                final MyActivity activity = (MyActivity)getActivity();
                final String username = userName.getText().toString();
                final String phonenumber = userPhone.getText().toString();
                currentUser.setName(username);
                currentUser.setPhoneNumber(phonenumber);
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, false);
                final Location location = locationManager.getLastKnownLocation(provider);
                currentUser.setLatitude(location.getLatitude());
                currentUser.setLongitude(location.getLongitude());
                fb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                if (child.getName().equals(phonenumber)) {
                                    User grabbedUser = child.getValue(User.class);
                                    currentUser.setFlag(grabbedUser.getFlag());
                                    fb.child(phonenumber).setValue(currentUser);
                                    activity.changeToMainPage();
                                    ((MyActivity) getActivity()).notificationStart();
                                    return;
                                }
                            }
                            fb.child(phonenumber).setValue(currentUser);
                            ((MyActivity) getActivity()).notificationStart();
                            activity.changeToMainPage();
                        }
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
