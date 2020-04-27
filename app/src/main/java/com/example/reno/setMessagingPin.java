package com.example.reno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class setMessagingPin extends DialogFragment {
    public ImageView cancleButton;
    public EditText EditsetMessagingPin, EditConfirmMessagingPin;
    public Button buttonSetMessaging;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;
    public  ProgressDialog progressDialog;
    public ProgressDialog SuccessProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_set_messaging_pin, container, false);

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





        cancleButton = v.findViewById(R.id.cancleSetMessagingPin);
        EditsetMessagingPin = v.findViewById(R.id.editSetMessaging);
        EditConfirmMessagingPin = v.findViewById(R.id.editSetConfirmMessagingPin);
        buttonSetMessaging = v.findViewById(R.id.buttonSetMessagingPin);

        cancleDialog();
        updateValue();
        return v;
    }

    public void cancleDialog() {
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
    }


    public void updateValue() {
        try {
            buttonSetMessaging.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isConnected = getIsConnected();
                    if (isConnected == true){
                        String setPin = EditsetMessagingPin.getText().toString();
                        String  ConfirmPin = EditConfirmMessagingPin.getText().toString();
                        if (setPin.isEmpty()) {
                            EditsetMessagingPin.setError("please Enter a pin");
                            EditsetMessagingPin.requestFocus();

                        }
                        else if (setPin.length() < 4) {
                            EditsetMessagingPin.setError("pin not up to 4 digits");
                            EditsetMessagingPin.requestFocus();


                        }else if (ConfirmPin.isEmpty()) {
                            EditConfirmMessagingPin.setError("please Enter a pin");
                            EditConfirmMessagingPin.requestFocus();


                        } else if (ConfirmPin.length() < 4) {
                            EditConfirmMessagingPin.setError("pin not up to 4 digits");
                            EditConfirmMessagingPin.requestFocus();

                        } else if (!setPin.equals(ConfirmPin)) {
                            EditConfirmMessagingPin.setError("pin does not match");
                            EditConfirmMessagingPin.requestFocus();

                        } else if (setPin.equals(ConfirmPin)){
                            try{
                                progressDialog.create();
                                progressDialog.show();
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference= firebaseDatabase.getReference("Users").child(uid).child("message");
                                databaseReference.setValue(setPin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.cancel();
                                            SuccessProgressDialog.create();
                                            SuccessProgressDialog.show();
                                            SuccessProgressDialog.setMessage(Html.fromHtml("<font color='#000000'>Successful..\nEnjoy your stay dear</font>"));
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
                                        Log.d("passu", "onFailure: "+e.getMessage());
                                    }
                                });
                            }catch (Exception e){
                                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }


                    } else{
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