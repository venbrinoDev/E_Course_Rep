package com.example.reno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.regex.Pattern;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

import co.paystack.android.PaystackSdk;


public class Paystack extends DialogFragment {
    private BroadcastReceiver MyReceiver = null;
    private Card card;
    private Charge charge;
    public View v;
    TextView connect;
    public Button cancle;
    public Button confirm;
    public EditText emailshowfield, cardnumberfield, monthfield, yearfield, cvvfield;

    private String email, cardNumber, cvv;
    private int expiryMonth, expiryYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_paystack, container, false);
        setCancelable(false);
        PaystackSdk.initialize(getContext());

        confirm = v.findViewById(R.id.pay_button);
        emailshowfield = v.findViewById(R.id.edit_email_address);
        cardnumberfield = v.findViewById(R.id.edit_card_number);
        monthfield = v.findViewById(R.id.edit_expiry_month);
        yearfield = v.findViewById(R.id.edit_expiry_year);
        cvvfield = v.findViewById(R.id.edit_cvv);

        MyReceiver = new MyReceiver();


        confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recheck();
                }
            });

        Cancle();
        return v;

    }
    public void Cancle(){
        cancle = v.findViewById(R.id.cancle_button);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

    }


    public void recheck(){
        confirm = v.findViewById(R.id.pay_button);
        String card = cardnumberfield.getText().toString();
        String email = emailshowfield.getText().toString();
        String datemonth = monthfield.getText().toString();
        String dateyear = yearfield.getText().toString();
        String cvvcode = cvvfield.getText().toString();


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailshowfield.setError("Invalid email pattern");
            emailshowfield.requestFocus();
        }
        if (email.isEmpty()){
            emailshowfield.setError("Enter email");
            emailshowfield.requestFocus();
        }
        else if (card.isEmpty()){
            cardnumberfield.setError("Enter card number");
            cardnumberfield.requestFocus();
        }
        else if (card.trim().length() < 16){
            cardnumberfield.setError("Invalid card number");
            cardnumberfield.requestFocus();
        }
        else if (datemonth.trim().length() != 2){
            monthfield.setError("invalid");
            monthfield.requestFocus();
        }
        else if (datemonth.startsWith("2")){
            monthfield.setError("Invalid");
            monthfield.requestFocus();
        }
        else if (datemonth.endsWith("3")){
            monthfield.setError("Invalid");
            monthfield.requestFocus();
        }
        else if (dateyear.trim().length() != 2){
            yearfield.setError("Invalid");
            yearfield.requestFocus();
        }
        else if (cvvcode.trim().length() != 3){
            cvvfield.setError("Invalid cvv");
            cvvfield.requestFocus();
        }
        else if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !card.isEmpty() && card.trim().length() >= 16
        && datemonth.trim().length() == 2 && datemonth.startsWith("1") && !datemonth.endsWith("3") && dateyear.trim().length() == 2
        && cvvcode.trim().length() == 3){
            getDialog().dismiss();
            Toast.makeText(getActivity(), "Transaction Successful", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }
}