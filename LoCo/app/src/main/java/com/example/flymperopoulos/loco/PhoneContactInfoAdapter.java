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
public class PhoneContactInfoAdapter extends ArrayAdapter<PhoneContactInfo> {
    private List<PhoneContactInfo> phoneContactInfos = new ArrayList<PhoneContactInfo>();
    private int resource;
    private Context context;


    public PhoneContactInfoAdapter(Context context, int resource, List<PhoneContactInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.phoneContactInfos = objects;

    }

    private class PhoneContactInfoHolder {
        TextView name, body;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        PhoneContactInfoHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(resource, parent, false);
        holder = new PhoneContactInfoHolder();

        //TextViews
        holder.name = (TextView) view.findViewById(R.id.profile_name);
        holder.body = (TextView) view.findViewById(R.id.contact_number);

        fillViews(holder, phoneContactInfos.get(position));
        return view;
    }
    @Override
    public int getCount(){
        return this.phoneContactInfos.size();
    }

    @Override
    public PhoneContactInfo getItem(int position) {
        return this.phoneContactInfos.get(position);
    }

    private void fillViews(PhoneContactInfoHolder holder, PhoneContactInfo phoneContactInfo){
        holder.name.setText(phoneContactInfo.name);
        holder.body.setText(phoneContactInfo.number);
    }


    public void addPhoneContactInfo(PhoneContactInfo PhoneContactInfo){
        this.phoneContactInfos.add(PhoneContactInfo);
        notifyDataSetChanged();
    }
}

