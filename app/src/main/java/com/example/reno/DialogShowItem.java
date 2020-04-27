package com.example.reno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class DialogShowItem extends DialogFragment {
 public Button okButton,CancleButton;
 public EditText texting;

 private MessageListener Listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_dialog_show_item, container, false);


        setCancelable(true);
        okButton = v.findViewById(R.id.okButton);
        CancleButton = v.findViewById(R.id.cancleButton);
        texting = v.findViewById(R.id.texting);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String  message = texting.getText().toString();

                if(message.isEmpty()){
                    texting.setError("Message not yet set");
                    texting.requestFocus();

                }else{
                    Listener.applyText(message);
                    getDialog().dismiss();
                }

            }
        });

        CancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pro="Be Reminded You May Be Having a Class ,Practical,Church Service or An Event";
                Listener.applyText(pro);
                getDialog().dismiss();

            }
        });


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            Listener=(MessageListener)context;
        } catch (ClassCastException e) {
           throw  new ClassCastException(context.toString() + "Must Implement example Dailog Listener");
        }

    }


    public interface MessageListener{
        void applyText(String name);
    }

}