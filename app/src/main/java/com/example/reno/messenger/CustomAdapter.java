package com.example.reno.messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.reno.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context c;
    ArrayList<PhoneNumbers> phoneNumbers;
    LayoutInflater inflater;
    PhoneNumbers myPhoneNumbers;

    public CustomAdapter(Context context, ArrayList<PhoneNumbers> phoneNumbers) {
        this.c = context;
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public int getCount() {
        return phoneNumbers.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneNumbers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(inflater == null) {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.model,parent,false);
        }

        MyViewHolder holder = new MyViewHolder(convertView);
        holder.textPhone.setText(phoneNumbers.get(position).getPhone());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.setLongClickListener(new MyLongClickListener() {
            @Override
            public void onItemLongClick() {
                myPhoneNumbers = (PhoneNumbers) getItem(position);
            }
        });

        return convertView;
    }

    public int getSelectedItemId() {
        return myPhoneNumbers.getId();
    }

    public String getSelectedItemPhone() {
        return myPhoneNumbers.getPhone();
    }
}
