package com.example.reno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    EditText emailId, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText6);
        tvSignUp = findViewById(R.id.textView);
        password = findViewById(R.id.editText7);
        btnSignIn = findViewById(R.id.logon);


        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(Html.fromHtml("<font color='#000000'>Logging user in..Please wait</font>"));
        progressDialog.setCancelable(false);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(LoginActivity.this,"You are Logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String password1 = password.getText().toString();
                String email = emailId.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter an email id");
                    emailId.requestFocus();
                }
                else if(password1.isEmpty()){
                    password.setError("Please enter a password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && password1.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && password1.isEmpty())){

                    progressDialog.create();
                    progressDialog.show();
                    mFirebaseAuth.signInWithEmailAndPassword(email, password1).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error, Please Try again",Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();

                            }
                            else {
                                progressDialog.cancel();
                                Intent intHOme = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intHOme);
                                finish();

                            }
                        }
                    });
                }
                else {
                    progressDialog.cancel();
                    Toast.makeText(LoginActivity.this, "An Error Occured",Toast.LENGTH_SHORT).show();
                }

            }


        });

            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intsignup = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intsignup);
                }
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            finish();
            finishAffinity();
            System.exit(0);
            return;
        }else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
