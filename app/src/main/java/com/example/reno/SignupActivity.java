package com.example.reno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ImageUserPhoto;
    TextView messagetext, balance;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    private long backPressedTime;
    private Toast backToast;


    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone, conpassword;
    private ProgressBar progressBar;
    public Spinner first;
    public  Spinner second;
    TextView tvSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        ImageUserPhoto = findViewById(R.id.imageholder);
        messagetext = findViewById(R.id.textView6);
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        conpassword = findViewById(R.id.editText5);
        editTextPhone = findViewById(R.id.edit_text_phone);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        tvSignIn = findViewById(R.id.textView2);
        balance = findViewById(R.id.balancemain);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);

        String  Level [] = {"Select Level","100 Level","200 Level","300 Level","400 Level", "500 Level"};
        String Department [] = {"Select Department", "Agricultural Economics", "Agricultural Extension", "Animal Science and Technology", "Crop Science and Technology",
                "Fisheries and Aquaculture Technology", "Forestry and Wildlife Technology", "Soil Science and Technology", "Anatomy", "Physiology",
        "Biochemistry", "Biology", "Biotechnology", "Microbiology", "Forensic Science", "Computer Science", "Information Technology", "Cyber Security",
                "Software Engineering", "Agricultural & Bio-Resources Engineering", "Chemical Engineering", "Civil Engineering", "Electrical & Electronics Engineering",
                "Food Science Technology", "Material & Metallurgical Engineering", "Mechanical Engineering", "Mechatronics Engineering", "Petroleum Engineering",
                "Polymer & Textile Engineering", "Architecture", "Building Technology", "Environmental Technology", "Quantity Surveying", "Surveying & Geoinformatics",
                "Urban & Regional Planning", "Biomedical Technology", "Dental Technology", "Optometry", "Prosthetics & Othortics", "Public Health Technology",
        "Financial Management Technology", "Maritime Management Technology", "Project Management Technology", "Transport Management Technology", "Management Technology",
        "Chemistry", "Geology", "Mathematics", "Physics", "Science Laboratory Technology", "Statistics"};

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Department);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        second.setAdapter(arrayAdapter);


        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Level);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first.setAdapter(secondAdapter);

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        ImageUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }
            }
        });
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent, "select picture"),REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(SignupActivity.this, "Please grant access for required permission",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(SignupActivity.this,
                                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    PReqCode);
            }
        }
        else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESCODE && data != null){

            //The user has successfully picked an image
            pickedImgUri = data.getData();
            ImageUserPhoto.setImageURI(pickedImgUri);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        String Confirmpassword = conpassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String Level = first.getSelectedItem().toString();
        final String Department= second.getSelectedItem().toString();
        final String message = messagetext.getText().toString();
        final String currentbal = balance.getText().toString();
        final String usertoken=messagetext.getText().toString();

                if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }


        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }
        if(!password.equals(Confirmpassword)){
            conpassword.setError("Passwords does not match");
            conpassword.requestFocus();
            return;
        }
        if(Level.equals("Select Level") && Department.equals("Select Department")) {
            Toast.makeText(getApplicationContext(), "Error!!! Please select level and department", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Level.equals("Select Level")) {
            Toast.makeText(getApplicationContext(), "Error!!! Please select level", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Department.equals("Select Department")){
            Toast.makeText(getApplicationContext(), "Error, Please select department", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError(getString(R.string.input_error_phone));
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 11) {
            editTextPhone.setError(getString(R.string.input_error_phone_invalid));
            editTextPhone.requestFocus();
            return;
        }
        if(pickedImgUri == null){
            Toast toast;
            toast=Toast.makeText(getApplicationContext(),"PLEASE CHOOSE A PICTURE",Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(50,200,50);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    email,
                                    phone,
                                    password,
                                    Level,
                                    Department,
                                    message,
                                    currentbal,
                                    usertoken
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(SignupActivity.this, HomeActivity.class);

                                        startActivity(i);
                                        updateUserInfo(name,pickedImgUri, mAuth.getCurrentUser());
                                        finish();
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {

        StorageReference mStroage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStroage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            updateUI();
                                        }
                                    }
                                });

                    }
                });

            }
        });
    }

    private void updateUI() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            finish();
            finishAffinity();
            System.exit(0);;
            return;
        }else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }



}
