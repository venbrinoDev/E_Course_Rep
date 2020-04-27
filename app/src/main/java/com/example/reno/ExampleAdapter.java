package com.example.reno;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Transliterator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>{
    private static ArrayList<ExampleClasss> mList;
   private  OnItemClickedListener mListener;
   private mOnitemChecked itemchanged;
   public static ExampleViewHolder holding;
   public  String theDate;
   public String yourMessage;

    public interface mOnitemChecked{
      void OnswitchItem(int Position, Boolean isccheked);
  }
     public interface  OnItemClickedListener {
        void onItemClick(int Position);
    }
    public void setOnItemChecked(mOnitemChecked check){
        itemchanged=check;
    }


    public void setOnItemClickedListener(OnItemClickedListener listener){

   mListener =listener;
    }


    public static class ExampleViewHolder extends  RecyclerView.ViewHolder{

        public static final String TAG="bomo";
        public ImageView mImageView;
        public TextView mDate;
        public Switch switchButon;
        public  TextView mMessage;
        public ImageView mDelete;
        public   RelativeLayout viewBackground;
        public   RelativeLayout viewForeground;
        public ExampleViewHolder(@NonNull View itemView, final OnItemClickedListener listener, final mOnitemChecked onitemChecked) {
            super(itemView);
            final Context context = itemView.getContext();
            mImageView = itemView.findViewById(R.id.timeImage);
             mDate = itemView.findViewById(R.id.timeText);
            mMessage = itemView.findViewById(R.id.messageText);
             switchButon= itemView.findViewById(R.id.aswitch);
            viewBackground = itemView.findViewById(R.id.background);
             viewForeground = itemView.findViewById(R.id.foreground);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(listener !=null){
                         int position = getAdapterPosition();
                         if(position != RecyclerView.NO_POSITION){
                             listener.onItemClick(position);
                         }
                     }
                 }
             });


             switchButon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     if(onitemChecked !=null){
                         int position = getAdapterPosition();
                         if(position != RecyclerView.NO_POSITION){
                             onitemChecked.OnswitchItem(position,isChecked);
                         }
                     }
                 }
             });
             switchButon.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    int position=getAdapterPosition();
                     ExampleClasss example=mList.get(position);
                     final int deletedDate=example.getDate().hashCode();
                    if (switchButon.isChecked()){
                        SharedPreferences preference=context.getSharedPreferences("pre",MODE_PRIVATE);
                        SharedPreferences.Editor editor= preference.edit();
                        editor.putBoolean("check"+deletedDate,true);
                        editor.commit();
                    }else{
                        SharedPreferences preference=context.getSharedPreferences("pre",MODE_PRIVATE);
                        SharedPreferences.Editor editor= preference.edit();
                        editor.putBoolean("check"+deletedDate,false);
                        editor.commit();
                    }
                 }
             });
        }

    }

    public ExampleAdapter (ArrayList<ExampleClasss> exampleList){
       mList = exampleList;
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_view,parent,false);
        ExampleViewHolder evh = new ExampleViewHolder(v,mListener,itemchanged);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExampleViewHolder holder, int position) {

       Context context = holder.itemView.getContext();
        ExampleClasss currentItem = mList.get(position);
        holder.mImageView.setImageResource(currentItem.getmImageResource());
         holder.mDate.setText(currentItem.getDate());
         holder.mMessage.setText(currentItem.getmMESSAGE());

         int deletedDate=currentItem.getDate().hashCode();
         SharedPreferences preference=context.getSharedPreferences("pre",MODE_PRIVATE);
         boolean checking=preference.getBoolean("check"+deletedDate,true);
         if (checking==true){
             holder.switchButon.setChecked(true);
         }else if(checking==false){
             holder.switchButon.setChecked(false);
         }
         theDate=currentItem.getDate();
         yourMessage=currentItem.getmMESSAGE();
    }
public String getCurrentDate(){
        return theDate;
}
public String getYourMessage(){
        return yourMessage;
}
    @Override
    public int getItemCount(){
        return mList.size();
    }
public void RemoveItem(int position){

        mList.remove(position);
        notifyItemRemoved(position);

}
public void RestoreItem(ExampleClasss item, int position){
   mList.add(position,item);
   notifyItemInserted(position);
}
}
