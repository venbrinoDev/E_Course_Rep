package com.example.reno;

import android.os.Parcel;
import android.os.Parcelable;

public class ExampleClasss implements Parcelable{

    public Boolean check;
    private int mImageResource;
    private String mDate;
    public String mMESSAGE;
    public ExampleClasss(int ImageResource, String DATE, String MESSAGE) {
        mImageResource = ImageResource;
        mDate = DATE;
        mMESSAGE = MESSAGE;
    }
    public void setChecked(Boolean chi){
        check=chi;
    }
    public Boolean getChecked(){
        return check;
    }


    public void setmMESSAGE(String mMESSAGE) {
        this.mMESSAGE = mMESSAGE;
    }

    protected ExampleClasss(Parcel in) {
        mImageResource = in.readInt();
        mDate = in.readString();
        mMESSAGE = in.readString();
    }



    public static final Creator<ExampleClasss> CREATOR = new Creator<ExampleClasss>() {
        @Override
        public ExampleClasss createFromParcel(Parcel in) {
            return new ExampleClasss(in);
        }

        @Override
        public ExampleClasss[] newArray(int size) {
            return new ExampleClasss[size];
        }
    };

    public int getmImageResource() {
        return mImageResource;
    }
    public String getDate(){
        return mDate;
    }

    public String getmMESSAGE(){
        return mMESSAGE;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageResource);
        dest.writeString(mDate);
        dest.writeString(mMESSAGE);
    }
}
