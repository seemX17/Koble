package com.seemran.koble.Fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seemran.koble.R;
import com.seemran.koble.Models.User;
import com.seemran.koble.Adapter.UserAdapter;

import java.util.ArrayList;

public class RecentFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter mAdapter;
    ArrayList<User> users;
    Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        // THis is same as set content view. But for fragment. Fragment  is like a window
        // It takes a layout and puts it in that window.
        // So, that is called inflating. It took the recentfrag layout and fit it in the window. GOt it? yes
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
        user.message = "I am WonderWoman.";
        user.time = "16:41";
        user.image = "https://s-media-cache-ak0.pinimg.com/736x/d0/61/de/d061de87c4ae9016f3f4b13c6e0766d8--hamza-retro-illustration.jpg";
        users.add(user);
        user = new User();
        user.name = "Jude Silveira";
        user.message = " Contact me for Photography.";
        user.time = "21:16";
        user.image = "https://s-media-cache-ak0.pinimg.com/originals/2a/bc/e5/2abce5b2f8cd52849d1098a9ac8da057.png";
        users.add(user);
        user = new User();
        user.name = "Shubham Modi";
        user.message = "Woah! There the Spiderman!";
        user.time = "11:30";
        user.image = "https://s-media-cache-ak0.pinimg.com/originals/35/20/13/352013cd60bf82b0eb538ba3ef876d6e.jpg";
        users.add(user);
        user = new User();
        user.name = "Amrit Almeida";
        user.message = "I am Lord, Bless you!";
        user.time = "07:18";
        user.image = "https://s-media-cache-ak0.pinimg.com/originals/87/6b/a3/876ba342d0098c78f67ef0aaff45f02d.png";
        users.add(user);
        user = new User();
        user.name = "Edrick";
        user.message = "hey";
        user.time="01.17";
        user.image = "https://pbs.twimg.com/profile_images/482948029129838592/WwzZK2aI_400x400.jpeg";
        users.add(user);
        user = new User();
        user.name = "Cedric periera";
        user.message ="Tommorw i have to go to church.";
        user.time = "02.45";
        user.image ="https://s-media-cache-ak0.pinimg.com/236x/ed/11/2d/ed112db5d8dc69ac75f4139ed31efec6.jpg";
        users.add(user);
        user = new User();

        user.name ="Adrain almeida";
        user.message = "Superhooman";
        user.time ="yesterday";
        user.image ="https://s-media-cache-ak0.pinimg.com/avatars/3obaz_1429685795_280.jpg";
        users.add(user);
    }
}
