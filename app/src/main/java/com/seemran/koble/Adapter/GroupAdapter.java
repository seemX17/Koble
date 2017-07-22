package com.seemran.koble.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seemran.koble.Models.GroupCard;
import com.seemran.koble.Login;
import com.seemran.koble.R;

import java.util.ArrayList;

/**
 * Created by infamous on 20/7/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
    ArrayList<GroupCard> groupcard;
    Context ctx;

    public GroupAdapter(Context ctx, ArrayList<GroupCard> groupcard) {
        this.ctx = ctx;
        this.groupcard = groupcard;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, time, grpmembers;
        public ImageView grpimage;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.groupname);
            grpimage = (ImageView) itemView.findViewById(R.id.groupimage);
            time = (TextView) itemView.findViewById(R.id.grouptime);
            grpmembers = (TextView) itemView.findViewById(R.id.groupmembers);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card,parent,false);
        return new GroupAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupCard grp = new GroupCard();
        grp =groupcard .get(position);
        holder.name.setText(grp.getName());
        holder.name.setTypeface(Login.customFont);
        holder.time.setText(grp.getTime());
        holder.time.setTypeface(Login.customFont);
        holder.grpmembers.setText(grp.getGrpmembers());
        holder.grpmembers.setTypeface(Login.customFont);

        Glide.with(ctx).load(grp.getGrpimage()).into(holder.grpimage);
    }

    @Override
    public int getItemCount() {
        return groupcard.size();
    }


}



