package com.seemran.koble.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seemran.koble.Adapter.GroupAdapter;
import com.seemran.koble.Models.GroupCard;
import com.seemran.koble.R;
import com.seemran.koble.Models.User;
import com.seemran.koble.Adapter.UserAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Groupfragment extends Fragment {
    RecyclerView recyclerView;
    GroupAdapter mAdapter;
    ArrayList<GroupCard> groupcard;
    Context ctx;

    public Groupfragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);//
        ctx = getActivity();
        groupcard = new ArrayList<>();
        createGrp();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewgroup);
        mAdapter = new GroupAdapter(  ctx,groupcard);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2 );
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        return view ;

    }

    private void createGrp() {

        GroupCard groupCard=new GroupCard();
        groupCard.name="College";
        groupCard.time="10 days ago";
        groupCard.grpmembers="7 members";
        groupCard.grpimage="https://s-media-cache-ak0.pinimg.com/originals/35/20/13/352013cd60bf82b0eb538ba3ef876d6e.jpg";
        groupcard.add(groupCard);

        groupCard.name="Hangout Friends";
        groupCard.time="Today";
        groupCard.grpmembers="3 members";
        groupCard.grpimage="https://s-media-cache-ak0.pinimg.com/originals/35/20/13/352013cd60bf82b0eb538ba3ef876d6e.jpg";
        groupcard.add(groupCard);

        groupCard.name="Cat Lovers";
        groupCard.time="yesterday";
        groupCard.grpmembers="10 members";
        groupCard.grpimage="https://s-media-cache-ak0.pinimg.com/originals/35/20/13/352013cd60bf82b0eb538ba3ef876d6e.jpg";
        groupcard.add(groupCard);

    }
}


