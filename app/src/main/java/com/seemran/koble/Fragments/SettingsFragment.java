package com.seemran.koble.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seemran.koble.Models.ProfileData;
import com.seemran.koble.R;

public class SettingsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private EditText username, phone, name;
    private Button saveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        username = (EditText) view.findViewById(R.id.userid);
        name=(EditText)view.findViewById(R.id.fullname);
        phone=(EditText)view.findViewById(R.id.phonenumber);
        saveData=(Button)view.findViewById(R.id.save);
        saveData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveDataClicked();
                    }
                }
        );
        return view;
    }

    public void saveDataClicked(){
        String userId = mDatabase.push().getKey();
        ProfileData profileData = new ProfileData();
        profileData.usernametxt = username.getText().toString();
        profileData.numbertxt = phone.getText().toString();
        profileData.nametxt = name.getText().toString();
        mDatabase.child(userId).setValue(profileData);
        Log.d("Operations", "Data Saved!");
    }
}
