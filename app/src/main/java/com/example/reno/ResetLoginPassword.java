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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetLoginPassword extends DialogFragment {
public ImageView cancleRL;
public EditText EditRLP,EditRConfirmLP;
public Button ButtonLRP;
public String LoginPin;
public ProgressDialog progressDialog;
public ProgressDialog SuccessProgressDialog;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;
    public String setPin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_reset_login_password,container,false);

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



        cancleRL=v.findViewById(R.id.ImageResetLoginPassword);
        EditRLP=v.findViewById(R.id.editResetLoginPassword);
        EditRConfirmLP=v.findViewById(R.id.editResetConfirmLoginPassword);
        ButtonLRP=v.findViewById(R.id.ButtonResetLoginPassword);

        cancle();
        update();

        return v;
    }


    public void cancle(){
        cancleRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();

            }
        });
    }

    public void update(){
        try {
            ButtonLRP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean getConnected=getIsConnected();
                    if (getConnected == true){
                        setPin=EditRLP.getText().toString();
                        String ConfirmPin = EditRConfirmLP.getText().toString();

                        if (setPin.isEmpty()) {
                            EditRLP.setError("please Enter a password");
                            EditRLP.requestFocus();

                        }
                        else if (setPin.length() < 6) {
                            EditRLP.setError("Password not up to 6 digits");
                            EditRLP.requestFocus();


                        }else if (ConfirmPin.isEmpty()) {
                            EditRConfirmLP.setError("please Enter a password");
                            EditRConfirmLP.requestFocus();


                        } else if (ConfirmPin.length() < 6) {
                            EditRConfirmLP.setError("password not up to 6 digits");
                            EditRConfirmLP.requestFocus();

                        } else if (!setPin.equals(ConfirmPin)) {
                            EditRConfirmLP.setError("Password does not match");
                            EditRConfirmLP.requestFocus();

                        } else if (setPin.equals(ConfirmPin)){
                            try{
                                progressDialog.create();
                                progressDialog.show();
                                currentUser.updatePassword(setPin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                            DatabaseReference databaseReference= firebaseDatabase.getReference("Users").child(uid).child("password");
                                            databaseReference.setValue(setPin).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("passu", "onFailure:" +e.getMessage());
                                                }
                                            });
                                            progressDialog.cancel();
                                            SuccessProgressDialog.create();
                                            SuccessProgressDialog.show();
                                            SuccessProgressDialog.setMessage(Html.fromHtml("<font color='#000000'>Successful Updated Password..\nEnjoy your stay dear</font>"));
                                            getDialog().cancel();
                                            Toast.makeText(getContext(), "Succesful", Toast.LENGTH_LONG).show();
                                        }else{
                                            progressDialog.cancel();
                                            SuccessProgressDialog.create();
                                            SuccessProgressDialog.show();
                                            SuccessProgressDialog.setMessage(Html.fromHtml("<font color='#000000'>Error Occurred!!\n please check the internet</font>"));
                                            Toast.makeText(getContext(), "checkConnection", Toast.LENGTH_LONG).show();
                                        }
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
