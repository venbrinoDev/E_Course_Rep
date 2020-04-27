package com.example.reno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.reno.ui.SignoutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    ImageView ImageUserPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    TextView textviewname, textviewlevel, textviewdepartment, textviewphone, textviewemail,welcome, Profiletosettings, Profilebal;
    TextView faqs, rates, privatepoly, apps;
    Button upload;
    DatabaseReference databaseReference;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            uid = mCurrentUser.getUid();
        }
        apps = findViewById(R.id.appinfo);
        faqs = findViewById(R.id.faq);
        rates = findViewById(R.id.rate);
        privatepoly = findViewById(R.id.privacypolicy);
        Profiletosettings = findViewById(R.id.profilesettings);
        ImageUserPhoto = findViewById(R.id.imageView2);
        textviewname = findViewById(R.id.textView5);
        textviewemail = findViewById(R.id.textView6);
        textviewdepartment = findViewById(R.id.textView7);
        textviewlevel = findViewById(R.id.textView8);
        textviewphone = findViewById(R.id.textView9);
        Profilebal = findViewById(R.id.profilebalance);
        welcome = findViewById(R.id.welcome);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = String.valueOf(dataSnapshot.child("name").getValue());
                String email = String.valueOf(dataSnapshot.child("email").getValue());
                String level = String.valueOf(dataSnapshot.child("Level").getValue());
                String phone = String.valueOf(dataSnapshot.child("phone").getValue());
                String department = String.valueOf(dataSnapshot.child("Department").getValue());
                String balancetext = String.valueOf(dataSnapshot.child("currentbal").getValue());

                ImageUserPhoto = findViewById(R.id.imageView2);
                textviewlevel.setText(level);
                textviewphone.setText(phone);
                textviewemail.setText(email);
                textviewdepartment.setText(department);
                textviewname.setText(name);
                welcome.setText(name);
                Profilebal.setText(balancetext + " NGN");

                if (currentUser.getPhotoUrl() == null) {
                    ImageUserPhoto.setBackgroundResource(R.drawable.profile);
                } else {
                    try {
                        Glide.with(ProfileActivity.this).load(currentUser.getPhotoUrl()).into(ImageUserPhoto);
                    }catch (Exception e){
                        Log.d("messing", "onDataChange:"+e.getMessage());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        Profiletosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SignoutActivity.class);
                startActivity(intent);
            }
        });


        faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FAQSActivity.class);
                startActivity(intent);
            }
        });

        rates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        privatepoly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Privacypolicy privacypolicy = new Privacypolicy();
                privacypolicy.show(getSupportFragmentManager(),"paying");
            }
        });

        apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, Appinfo.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
    }
}