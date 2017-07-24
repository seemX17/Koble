package com.seemran.koble.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seemran.koble.Activity.LoginActivity;
import com.seemran.koble.R;
import com.seemran.koble.Models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Seemran on 7/11/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private ArrayList<User> users; // This var was made to get data passed in contructor
    private Context context; // Now that highligthed contructor gets data. but we need to save data
    //so we create vars to save


    public UserAdapter(Context ctx, ArrayList<User> users){ // THis is a constructor.
        this.users = users; // We are taking the array above and saving all users passed in that constuctor here
        this.context = ctx;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, msg, time;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.userName);
            msg = (TextView) view.findViewById(R.id.userMsg);
            time = (TextView) view.findViewById(R.id.userTime);
            image = (ImageView)view.findViewById(R.id.image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.contact_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getName());
        holder.name.setTypeface(LoginActivity.customFont);
        holder.msg.setText(user.getMessage());
        holder.msg.setTypeface(LoginActivity.customFont);
        holder.time.setText(user.getTime());
        holder.time.setTypeface(LoginActivity.customFont);

        Log.d("Operations", user.getName());
        Picasso.with(context).load(user.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
