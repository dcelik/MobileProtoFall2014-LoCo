package com.example.flymperopoulos.loco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 10/14/14.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private List<User> Users = new ArrayList<User>();
    private int resource;
    private Context context;


    public UserAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.Users = objects;

    }

    private class UserHolder {
        TextView name, body;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        UserHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        holder = new UserHolder();

        //TextViews
        holder.name = (TextView) view.findViewById(R.id.profile_name);
//        holder.body = (TextView) view.findViewById(R.id.contact_number);

        fillViews(holder, Users.get(position));
        return view;
    }
    @Override
    public int getCount(){
        return this.Users.size();
    }

    @Override
    public User getItem(int position) {
        return this.Users.get(position);
    }

    private void fillViews(UserHolder holder, User user){
        holder.name.setText(user.getName());
//        holder.body.setText(user.getPhoneNumber());
    }


    public void addUser(User user){
        this.Users.add(user);
        notifyDataSetChanged();
    }
}

