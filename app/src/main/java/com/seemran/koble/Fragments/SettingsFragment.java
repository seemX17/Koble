package com.seemran.koble.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seemran.koble.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public TextView userlocation;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        userlocation=(TextView)view.findViewById(R.id.UserLocation);
//        Intent iin= getActivity().getIntent();
//        Bundle b = iin.getExtras();
//
//        if(b!=null)
//        {
//            String j =(String) b.get("TextValue");
//            userlocation.setText(j);
//        }
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
