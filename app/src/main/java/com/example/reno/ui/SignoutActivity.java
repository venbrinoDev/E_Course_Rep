package com.example.reno.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.reno.setMessagingPin;
import com.example.reno.ResetMessagingPin;
import com.example.reno.ResetLoginPassword;
import com.example.reno.LoginActivity;
import com.example.reno.MainActivity;
import com.example.reno.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignoutActivity extends AppCompatActivity {

    Button btnLogout;

    public FloatingActionButton MessagingPin,FloatingResetMessagingPin,FloatingResetLoginPassword;
    public EditText edtMessagingPin,edtResetMessagingPin,edtResetLoginPin;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;
    public String MssgPin;
    public String LoginPin;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signout);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            uid = mCurrentUser.getUid();
        }




        MessagingPin=findViewById(R.id.floatingSetMessagingPin);
        edtMessagingPin=findViewById(R.id.etdSetMessagingPin);
        FloatingResetMessagingPin=findViewById(R.id.floatingResetMessage);
        edtResetMessagingPin=findViewById(R.id.edtResetMessagingPin);
        FloatingResetLoginPassword=findViewById(R.id.floatingResetLoginPin);
        edtResetLoginPin=findViewById(R.id.edtResetLoginPin);

        btnLogout = findViewById(R.id.button3);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intTomain = new Intent(SignoutActivity.this, LoginActivity.class);
                startActivity(intTomain);
                finish();
            }
        });
        SetMessagingPin();
        ResetMessagingPin();
        ResetLoginMessagingPin();

        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(Html.fromHtml("<font color='#000000'>Loading please wait..</font>"));
        progressDialog.setCancelable(false);

    }

    public String getMessagingPin(){

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MssgPin= String.valueOf(dataSnapshot.child("message").getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return MssgPin;
    }
    public void SetMessagingPin(){
        MessagingPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Boolean getConected=getIsConnected();
             if (getConected ==true){

             String passwordEnter=edtMessagingPin.getText().toString();
            if(passwordEnter.isEmpty()){
                 edtMessagingPin.setError("please enter a password");
                 edtMessagingPin.requestFocus();
             }else{
         progressDialog.create();
         progressDialog.show();
                 AuthCredential credential= EmailAuthProvider.getCredential(currentUser.getEmail(),passwordEnter);
                 currentUser.reauthenticate(credential)
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if (task.isSuccessful()){
                                     progressDialog.cancel();
                                     setMessagingPin setMessagingPin = new setMessagingPin();
                                     setMessagingPin.show(getSupportFragmentManager(),"setMessagingPin");
                                     edtMessagingPin.setText(null);
                                 }else{
                                     progressDialog.cancel();
                                     edtMessagingPin.setError("password Incorrect or check internet con");
                                     edtMessagingPin.requestFocus();
                                 }
                             }
                         });

             }

             }else{
                 Toast.makeText(getApplicationContext(),"check Internet Connection",Toast.LENGTH_LONG).show();
             }
            }
        });
    }
    public void ResetMessagingPin(){
    FloatingResetMessagingPin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean getConnected=getIsConnected();
            if (getConnected == true){
                String ResetPin=getMessagingPin();
                String passwordLogin=edtResetMessagingPin.getText().toString();
                if (ResetPin == null){
                    Toast.makeText(getApplicationContext(), "Please check Connection and try again", Toast.LENGTH_SHORT).show();
                }else if (passwordLogin.isEmpty()){
                    edtResetMessagingPin.setError("please enter a password");
                    edtResetMessagingPin.requestFocus();
                }else if (passwordLogin.equals(ResetPin)){
                    ResetMessagingPin resetMessagingPin=new ResetMessagingPin();
                    resetMessagingPin.show(getSupportFragmentManager(),"resetMessagingPin");
                    edtResetMessagingPin.setText(null);
                }else{
                    edtResetMessagingPin.setError("password Incorrect");
                    edtResetMessagingPin.requestFocus();
                }
            }else{
                Toast.makeText(getApplicationContext(),"check Internet Connection",Toast.LENGTH_LONG).show();

            }

        }
    });
    }
    public void ResetLoginMessagingPin(){

        FloatingResetLoginPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean getConnected = getIsConnected();
                if (getConnected == true){

                    String paswordResetLoginPin=edtResetLoginPin.getText().toString();

                    if (paswordResetLoginPin.isEmpty()) {
                        edtResetLoginPin.setError("please enter a password");
                        edtResetLoginPin.requestFocus();

                    }else {
                        progressDialog.create();
                        progressDialog.show();
                        AuthCredential credential= EmailAuthProvider.getCredential(currentUser.getEmail(),paswordResetLoginPin);
                        currentUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.cancel();
                                            ResetLoginPassword  resetLoginPassword= new ResetLoginPassword();
                                            resetLoginPassword.show(getSupportFragmentManager(),"resetLoginPassword");
                                            edtResetLoginPin.setText(null);
                                        }else{
                                            progressDialog.cancel();
                                            edtResetLoginPin.setError("password Incorrect or check internet connection");
                                            edtResetLoginPin.requestFocus();
                                        }
                                    }
                                });

                    }
                }else{
                    Toast.makeText(getApplicationContext(),"check Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public Boolean getIsConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED
                ||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED){
          return true;
        }else{
         return false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
    }
}
