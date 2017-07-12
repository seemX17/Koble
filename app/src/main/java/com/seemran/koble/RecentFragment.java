package com.seemran.koble;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecentFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter mAdapter;
    ArrayList<User> users;
    Context ctx;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ctx = getActivity();
        users = new ArrayList<>();
        createUsers();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new UserAdapter(ctx, users);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        return view;
    }
    public void createUsers(){
        User user = new User();


        user.name = "Aditya Bhosle";
        user.message = "WonderWoman";
        user.time = "16:41";
        user.image = "https://lh3.googleusercontent.com/-jrW4bMl3sCo/AAAAAAAAAAI/AAAAAAAAB0M/SOvt53SIH5Q/s90-p-rw-no/photo.jpg";
        users.add(user);
        user = new User();
        user.name = "Jude Silveira";
        user.message = "Photographer";
        user.time = "21:16";
        user.image = "https://lh3.googleusercontent.com/-jrW4bMl3sCo/AAAAAAAAAAI/AAAAAAAAB0M/SOvt53SIH5Q/s90-p-rw-no/photo.jpg";
        users.add(user);
        user = new User();
        user.name = "Shubham Modi";
        user.message = "Spiderman";
        user.time = "11:30";
        user.image = "https://lh3.googleusercontent.com/-jrW4bMl3sCo/AAAAAAAAAAI/AAAAAAAAB0M/SOvt53SIH5Q/s90-p-rw-no/photo.jpg";
        users.add(user);
        user = new User();
        user.name = "Amrit Almeida";
        user.message = "Superman";
        user.time = "07:18";
        user.image = "https://lh3.googleusercontent.com/-jrW4bMl3sCo/AAAAAAAAAAI/AAAAAAAAB0M/SOvt53SIH5Q/s90-p-rw-no/photo.jpg";
        users.add(user);
    }
}
