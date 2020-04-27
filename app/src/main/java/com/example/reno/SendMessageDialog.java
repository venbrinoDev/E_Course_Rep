package com.example.reno;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.reno.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendMessageDialog extends DialogFragment {
    public Button cancle, request, ok;
    public EditText check;
    public String confirm;
    public  String enter;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.activity_diologsend,container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseUser mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            uid = mCurrentUser.getUid();
        }
        
       cancle = v.findViewById(R.id.canclebtn);
       request = v.findViewById(R.id.requestbtn);
       ok = v.findViewById(R.id.okbtn);

       setCancelable(false);
       cancle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getDialog().dismiss();
               FragmentTransaction transaction = getFragmentManager().beginTransaction();
               transaction.replace(R.id.nav_host_fragment, new HomeFragment());
               transaction.addToBackStack(null);
               transaction.commit();

           }
       });

       request.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                       "mailto","fr1ncis.j1mes54@gmail.com", null));
               intent.putExtra(Intent.EXTRA_SUBJECT, "Sms pin Request");
               intent.putExtra(Intent.EXTRA_TEXT, "I would like to purchase a pin.");
               startActivity(Intent.createChooser(intent, "Reno:"));
               getDialog().dismiss();
           }
       });
       check = v.findViewById(R.id.check);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.getText().toString().equals(confirm)){
                    getDialog().dismiss();
                    Toast.makeText(getActivity(), "message sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    check.setError("Invalid message pin");
                    check.requestFocus();
                }
            }
        });


        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                confirm = String.valueOf(dataSnapshot.child("message").getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Please Check Internet Connection",Toast.LENGTH_SHORT).show();
            }
        });


       return v;
    }
}
