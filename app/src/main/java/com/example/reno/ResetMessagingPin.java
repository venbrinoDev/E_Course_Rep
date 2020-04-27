package com.example.reno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetMessagingPin extends DialogFragment {
    public ImageView cancleRmp;
    public Button ButtonRmp;
    public EditText editRmp,editConfirmRmp;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;
    public ProgressDialog progressDialog;
    public ProgressDialog SuccessProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_reset_messaging_pin,container,false);

        setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            uid = mCurrentUser.getUid();
        }


        progressDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(Html.fromHtml("<font color='#000000'>Loading please wait..</font>"));
        progressDialog.setCancelable(false);

        SuccessProgressDialog=new ProgressDialog(getActivity());
        SuccessProgressDialog.setIndeterminate(true);
        SuccessProgressDialog.setCancelable(true);

        cancleRmp=v.findViewById(R.id.cancleResetMessagingPin);
        ButtonRmp=v.findViewById(R.id.ButtonResetMessagingPin);
        editRmp=v.findViewById(R.id.editResetMessagingPin);
        editConfirmRmp=v.findViewById(R.id.editResetConfirmMessagingPin);
        cancle();
        update();

        return  v;
    }

    public void cancle(){
        cancleRmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
    }

    public void update(){
        try{
            ButtonRmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean getConnected= getIsConnected();
                    if (getConnected == true){
                        String setPin=editRmp.getText().toString();
                        String ConfirmPin = editConfirmRmp.getText().toString();
                        if (setPin.isEmpty()) {
                            editRmp.setError("please Enter a pin");
                            editRmp.requestFocus();

                        }
                        else if (setPin.length() < 4) {
                            editRmp.setError("pin not up to 4 digits");
                            editRmp.requestFocus();


                        }else if (ConfirmPin.isEmpty()) {
                            editConfirmRmp.setError("please Enter a pin");
                            editConfirmRmp.requestFocus();


                        } else if (ConfirmPin.length() < 4) {
                            editConfirmRmp.setError("pin not up 4 digits");
                            editConfirmRmp.requestFocus();

                        } else if (!setPin.equals(ConfirmPin)) {
                            editConfirmRmp.setError("pin does not match");
                            editConfirmRmp.requestFocus();

                        } else if (setPin.equals(ConfirmPin)){
                            try{
                                progressDialog.create();
                                progressDialog.show();
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference=firebaseDatabase.getReference("Users").child(uid).child("message");
                                databaseReference.setValue(setPin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.cancel();
                                            SuccessProgressDialog.create();
                                            SuccessProgressDialog.show();
                                            SuccessProgressDialog.setMessage(Html.fromHtml("<font color='#000000'>Successful Updated Pin..\nEnjoy your stay dear</font>"));
                                            getDialog().cancel();
                                            Toast.makeText(getContext(), "Successful", Toast.LENGTH_LONG).show();

                                        }else{
                                            progressDialog.cancel();
                                            SuccessProgressDialog.create();
                                            SuccessProgressDialog.show();
                                            SuccessProgressDialog.setMessage(Html.fromHtml("<font color='#000000'>Error Occurred!!\n please check the internet</font>"));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("passu", "onFailure:" +e.getMessage());
                                    }
                                });
                            }catch (Exception e){
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }

                    }else{
                        Toast.makeText(getContext(), "check Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }





    public Boolean getIsConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }
}
