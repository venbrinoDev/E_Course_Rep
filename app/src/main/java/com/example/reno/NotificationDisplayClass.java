package com.example.reno;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import androidx.databinding.BindingAdapter;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso;

public class NotificationDisplayClass implements Parcelable {
    public String NotificationTitle;
    public String NotificationMessage;
    public String NotificationTime;
    public String NoticationId;
    public String ImageUrl;

    public NotificationDisplayClass(String notificationTitle, String notificationMessage, String notificationTime, String noticationId,String imageUrl) {
        NotificationTitle = notificationTitle;
        NotificationMessage = notificationMessage;
        NotificationTime = notificationTime;
        NoticationId = noticationId;
        ImageUrl=imageUrl;
    }

    protected NotificationDisplayClass(Parcel in) {
        NotificationTitle = in.readString();
        NotificationMessage = in.readString();
        NotificationTime = in.readString();
        NoticationId = in.readString();
        ImageUrl=in.readString();
    }

    public static final Creator<NotificationDisplayClass> CREATOR = new Creator<NotificationDisplayClass>() {
        @Override
        public NotificationDisplayClass createFromParcel(Parcel in) {
            return new NotificationDisplayClass(in);
        }

        @Override
        public NotificationDisplayClass[] newArray(int size) {
            return new NotificationDisplayClass[size];
        }
    };

    public String getNotificationTitle() {
        return NotificationTitle;
    }
public String getImageUrl(){
        return ImageUrl;
}

    public void setNotificationTitle(String notificationTitle) {
        NotificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return NotificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        NotificationMessage = notificationMessage;
    }

    public String getNotificationTime() {
        return NotificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        NotificationTime = notificationTime;
    }

    public String getNoticationId() {
        return NoticationId;
    }

    public void setNoticationId(String noticationId) {
        NoticationId = noticationId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(NotificationTitle);
        dest.writeString(NotificationMessage);
        dest.writeString(NotificationTime);
        dest.writeString(NoticationId);
        dest.writeString(ImageUrl);
    }

   @BindingAdapter({"android:imageUrl"})
    public static  void loadImage(ImageView view,String imageUrl){
          if (!imageUrl.isEmpty() ){
              Picasso.with(view.getContext())
                      .load(imageUrl)
                      .placeholder(R.drawable.testimage)
                      .into(view);
          }else{
              view.setBackgroundResource(R.drawable.testimage);
          }
   }
}
