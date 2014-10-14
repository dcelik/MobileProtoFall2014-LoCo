package com.example.flymperopoulos.loco;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by flymperopoulos on 10/13/2014.
 */
public class ContactsFragment extends Fragment{
    PhoneContactInfoAdapter list;
    Context context;

    public ContactsFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_list_view, container, false);

        Button backBtn = (Button)rootView.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivity activity = (MyActivity)getActivity();
                activity.changeToMainFragment();
            }
        });

        ArrayList<PhoneContactInfo> contacts = readContacts();
        list= new PhoneContactInfoAdapter(getActivity(), R.layout.contact_item, contacts);
        ListView lv=(ListView) rootView.findViewById(R.id.list);
        lv.setAdapter(list);

        return rootView;

    }
    public ArrayList<PhoneContactInfo> readContacts() {
        Log.d("START", "Getting all Contacts");
        ArrayList<PhoneContactInfo> arrContacts = new ArrayList<PhoneContactInfo>();
        PhoneContactInfo phoneContactInfo=null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri,
                new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone._ID},
                null, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String contactNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));


            phoneContactInfo = new PhoneContactInfo();
            phoneContactInfo.setPhoneContactID(phoneContactID);
            phoneContactInfo.setContactName(contactName);
            phoneContactInfo.setContactNumber(contactNumber);
            if (phoneContactInfo != null)
            {
                arrContacts.add(phoneContactInfo);
            }
            phoneContactInfo = null;
            cursor.moveToNext();
        }
        cursor.close();
        cursor = null;
        Log.d("END","Got all Contacts");
        return arrContacts;
    }
}

