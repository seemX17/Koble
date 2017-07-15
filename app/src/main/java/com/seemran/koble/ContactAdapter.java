package com.seemran.koble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        public MyViewHolder(View itemView) {
            super(itemView);

            userimage = (CircleImageView) itemView.findViewById(R.id.userimage);
            namecontact=(TextView) itemView.findViewById(R.id.namecontact);
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
        holder.namecontact.setTypeface(Login.customFont);
    }

    @Override
    public int getItemCount() {
        return namecontact.size();
    }
}
