package com.seemran.koble.PhonebookContacts;

import android.content.Context;
import android.content.Intent;
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
import com.seemran.koble.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Seemran on 7/16/2017.
 */

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>
{
    ArrayList<Contact> namecontact;
    Context ctx;

    public ContactAdapter(ArrayList<Contact> namecontact, Context context )
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
            ContactLayout=(LinearLayout)itemView.findViewById(R.id.contactlayout);
            chatimage=(ImageView) itemView.findViewById(R.id.chat);
            callimage=(ImageView)itemView.findViewById(R.id.call);
        }

    }
    //implement
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact, parent, false);

        return new ContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.MyViewHolder holder, int position) {
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
        ctx.startActivity(i);
    }
});



    }

    @Override
    public int getItemCount() {
        return namecontact.size();
    }
}
