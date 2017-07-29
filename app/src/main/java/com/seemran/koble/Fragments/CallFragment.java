package com.seemran.koble.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.seemran.koble.Models.Contact;
import com.seemran.koble.Adapter.CallAdapter;
import com.seemran.koble.R;

import java.util.ArrayList;

public class CallFragment extends Fragment {
    RecyclerView recyclerView;
    CallAdapter mAdapter;
    Context ctx;
    public  static final int RequestPermissionCode  = 1 ;
    Cursor cursor ;
    String name, phonenumber ;
    ArrayList<Contact> contactList;
    public ImageView newchat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        ctx = getActivity();
        contactList = new ArrayList<>();// This is compulsory for any type of arraylist. It initializes or creates buffer.
        GetContactsIntoArrayList();


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclercontacts);
        mAdapter = new CallAdapter( contactList,ctx);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        newchat = (ImageView) view.findViewById(R.id.chat);
        return view;

    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);

        //get the button view

//        newchat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), ChatActivity.class);
//                getActivity().startActivity(i);
//            }
//        });
//    }
    public void GetContactsIntoArrayList(){
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null, null, null);
        // See here we accessed the phones directory and saved all contacts in the cursor.
        // Now cursor has many contacts.
        // we access them one by one using that loop below
        // cursor.movetonext will stop when the cursor is empty. it will return false. and stop the loop ok

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Contact contact = new Contact();
            // This is one instance of call class we just created.
            // It represents one call.
            // Now this is a loop. Everytime we get a call we add it to the arraylist we created!
             // Cursor is an object that allows us to save result aquired from a query execution.
            //  SQLite database we execute a query. The result is stored in the cursor.
            // Which allows us to implement read write operations on the data.
            // cursor var contains a table
            // Which has many columns. Like name, number, email and stuff.
            // So in order to suppose get name
            // We write cursor.getString(cursor.getColumnIndex(ContactsContract.Commonda.....Phone.Datayouwant))
            // Now name is not directly accessible.
            // So first we find which column name is present in.
            // Different brands of phones have different types of ways to save contacts. hence diffrent indexes
           // Now we made call var but its empty.
            // fill it
            contact.setName(name);
            contact.setNumber(phonenumber);
            contactList.add(contact);
        }

        cursor.close();

    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(),"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getContext(),"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
