package com.example.reno;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;

public class NotificationDisplayAdapter extends RecyclerView.Adapter<NotificationDisplayAdapter.NotiificationViewHolder> {
public ArrayList<NotificationDisplayClass> notificationDisplayClass;

    public NotificationDisplayAdapter(ArrayList<NotificationDisplayClass> notificationDisplayClass) {
        this.notificationDisplayClass = notificationDisplayClass;
    }

    @NonNull
    @Override
    public NotiificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_display_layout,parent,false);
        return new NotiificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiificationViewHolder holder, int position) {
   final NotificationDisplayClass Current=notificationDisplayClass.get(position);
   holder.NotificationTitle.setText(Current.getNotificationTitle());
   holder.NotificationMessage.setText(Current.getNotificationMessage());
   holder.NotificationTime.setText(Current.getNotificationTime());
   holder.NotiFIcationId.setText(Current.getNoticationId());

    }

    @Override
    public int getItemCount() {
        return notificationDisplayClass.size();
    }


    public class NotiificationViewHolder extends RecyclerView.ViewHolder{
         public  TextView NotificationTitle;
          public  TextView NotificationMessage;
          public  TextView NotificationTime;
        public   TextView NotiFIcationId;
        public RelativeLayout viewBackgroung;
        public RelativeLayout viewForeground;
        public NotiificationViewHolder(@NonNull View itemView) {
            super(itemView);
            viewBackgroung=itemView.findViewById(R.id.notifybackground);
            viewForeground=itemView.findViewById(R.id.notifyforeground);
            NotificationTitle=itemView.findViewById(R.id.navTitle);
            NotificationMessage=itemView.findViewById(R.id.navMessage);
            NotificationTime=itemView.findViewById(R.id.navTime);
            NotiFIcationId=itemView.findViewById(R.id.navId);
            CardView frameLayout=itemView.findViewById(R.id.cardView);
           frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    NotificationDisplayClass notifyClaa=notificationDisplayClass.get(position);
                    Intent intent= new Intent(v.getContext(),Notification_Item.class);
                    intent.putExtra("items",notifyClaa);
                    v.getContext().startActivity(intent);
                }
            });
        }


    }
    public void RemoveItem(int Postion){
        notificationDisplayClass.remove(Postion);
        notifyItemRemoved(Postion);
    }
    public void RestoreItem(int Postion ,NotificationDisplayClass notify){
        notificationDisplayClass.add(Postion,notify);
        notifyItemInserted(Postion);
    }


}
