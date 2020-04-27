package com.example.reno.ui.send;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.reno.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SendFragment extends Fragment {

    public Spinner first;
    public Button send;
    public EditText message;
    String mess;

    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);

        first = root.findViewById(R.id.firstone);
        send = root.findViewById(R.id.feedbackbtn);
        message = root.findViewById(R.id.messageinput);

        final String Level[] = {"General Feedback", "Bug Report", "Suggestions", "Other"};


        ArrayAdapter<String> secondAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Level);
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        first.setAdapter(secondAdapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String reno = first.getSelectedItem().toString();
                mess = message.getText().toString();
                final String main = message.getText().toString();
                if (!(main.isEmpty())){
                  Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                  "mailto","EcourseRep@gmail.com", null));
                    intent.putExtra(Intent.EXTRA_SUBJECT, reno);
                    intent.putExtra(Intent.EXTRA_TEXT, main);
                    startActivity(Intent.createChooser(intent, main));
                }
                else if (main.isEmpty()){
                    message.setError("Please enter message");
                    message.requestFocus();

                }

            }
        });
        return root;
    }

}