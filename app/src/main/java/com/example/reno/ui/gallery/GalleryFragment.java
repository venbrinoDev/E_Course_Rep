package com.example.reno.ui.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reno.DiologsendActivity;
import com.example.reno.NotificationDBHelper;
import com.example.reno.NotificationDisplayAdapter;
import com.example.reno.NotificationDisplayClass;
import com.example.reno.NotificationTouchHelper;
import com.example.reno.R;
import com.example.reno.SendMessageDialog;
import com.example.reno.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;

public class GalleryFragment extends Fragment implements NotificationTouchHelper.RecyclerItemTouchHelperListener {
public RecyclerView recyclerView;
public NotificationDisplayAdapter mAdapter;
public RecyclerView.LayoutManager mLayoutManager;
public ArrayList<NotificationDisplayClass> notificationDisplayClasses;
public RelativeLayout ContentNoti;
public NotificationDBHelper notificationDBHelper;

    public GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = root.findViewById(R.id.notifiRecylerView);
        ContentNoti=root.findViewById(R.id.notificationContent);
        notificationDBHelper=new NotificationDBHelper(getActivity());
          notificationDisplayClasses=new ArrayList<>();
          buildRecycler();
          LoadNotification();


            return root;
    }




    public void  buildRecycler(){
        notificationDisplayClasses = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        mLayoutManager= new LinearLayoutManager(getActivity());
         mAdapter = new NotificationDisplayAdapter(notificationDisplayClasses);
         recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback simpleCallback= new NotificationTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

  CheckForItem();

    }
    public void LoadNotification(){


        Cursor res=notificationDBHelper.getAllData();
        if (res.getCount()==0){
         Toast.makeText(getActivity(),"No Notification Available",Toast.LENGTH_SHORT).show();
        }else{
            while (res.moveToNext()){
                String Title=res.getString(1);
                String Message=res.getString(2);
                String Time = res.getString(3);
                String ID = res.getString(4);
                String imageUrl=res.getString(5);
                Log.d("messing", ID+"\t"+Title+"\t"+Message+"\t"+Time);
                int position=0;
                notificationDisplayClasses.add(position,new NotificationDisplayClass(Title,Message,Time,ID,imageUrl));
            }
        }


        CheckForItem();
    }

    public  void CheckForItem(){
        if (mAdapter.getItemCount()==0){
            ContentNoti.setVisibility(View.VISIBLE);
        }else{
            ContentNoti.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position){
        NotificationDisplayClass notificationDisplayClass=notificationDisplayClasses.get(position);
        final String title=notificationDisplayClass.getNotificationTitle();
        final String message=notificationDisplayClass.getNotificationMessage();
        final String time=notificationDisplayClass.getNotificationTime();
       final String ID=notificationDisplayClass.getNoticationId();
       final String imageUrl=notificationDisplayClass.getImageUrl();

        if (viewHolder instanceof NotificationDisplayAdapter.NotiificationViewHolder){
            final NotificationDisplayClass deletedItem  = notificationDisplayClasses.get(viewHolder.getAdapterPosition());
            final int deletedIndex=viewHolder.getAdapterPosition();

            mAdapter.RemoveItem(viewHolder.getAdapterPosition());
            Snackbar snackbar =Snackbar
                    .make(getActivity().findViewById(R.id.snacking),"Notification Message Deleted", BaseTransientBottomBar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAdapter.RestoreItem(deletedIndex,deletedItem);
                            notificationDBHelper.insertData(title,message,time,ID,imageUrl);
                            CheckForItem();
                        }
                    });
            notificationDBHelper.DeleteData(ID);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            CheckForItem();

        }

    }
}