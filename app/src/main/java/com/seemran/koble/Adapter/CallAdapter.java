package com.seemran.koble.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seemran.koble.Activity.LoginActivity;
import com.seemran.koble.Activity.ChatActivity;
import com.seemran.koble.Activity.OnCallActivity;
import com.seemran.koble.Models.Contact;
import com.seemran.koble.R;

import java.util.AbstractList;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Seemran on 7/16/2017.
 */

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.MyViewHolder>
{
    ArrayList<Contact> namecontact;
    Context ctx;
    String phnumber;

    public CallAdapter(ArrayList<Contact> namecontact, Context context )
    {
        this.ctx = context;
        this.namecontact=namecontact;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView namecontact;
        public CircleImageView userimage;
        public  LinearLayout ContactLayout;
        public ImageView chatimage,callimage;


        public MyViewHolder(View itemView) {
            super(itemView);

            userimage = (CircleImageView) itemView.findViewById(R.id.userimage);
            namecontact=(TextView) itemView.findViewById(R.id.namecontact);
            ContactLayout=(LinearLayout)itemView.findViewById(R.id.calllayout);
            chatimage=(ImageView) itemView.findViewById(R.id.chat);
            callimage=(ImageView)itemView.findViewById(R.id.call);
        }

    }
    //implement
    @Override
    public CallAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call, parent, false);

        return new CallAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallAdapter.MyViewHolder holder, int position) {
        Contact contact = new Contact();
        contact = namecontact.get(position);
        holder.namecontact.setText(contact.getName());
        holder.namecontact.setTypeface(LoginActivity.customFont);



        final Contact finalContact = contact;
        holder.chatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, ChatActivity.class);
                i.putExtra("personId", finalContact.getNumber());
                ctx.startActivity(i);
            }
        });


      holder.callimage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(ctx, OnCallActivity.class);
            i.putExtra("personId",finalContact.getNumber());
             ctx.startActivity(i);
        }
    });
    }

    @Override
    public int getItemCount() {
        return namecontact.size();
    }
}
