package com.example.reno.messenger;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.reno.R;

public class MyViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener {
    TextView textPhone;
    MyLongClickListener longClickListener;

    public MyViewHolder(View view) {
        textPhone = view.findViewById(R.id.text_phone_model);
        view.setOnLongClickListener(this);
        view.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Action");
        menu.add(0, 0, 0, "Edit");
        menu.add(0, 1, 0, "Delete");
    }

    public void setLongClickListener(MyLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        this.longClickListener.onItemLongClick();
        return false;
    }
}
