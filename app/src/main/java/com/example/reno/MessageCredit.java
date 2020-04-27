package com.example.reno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MessageCredit extends AppCompatActivity {

    public CardView AtmPayment;
    public CardView BankPayment;
    public TextView phonedail, phonemessage;
    public TextView phonedial2, phonemessage2;
    public TextView phonedial3, phonemessage3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_credit);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        AtmPayment = findViewById(R.id.cardView);
        AtmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageCredit.this, CardCollection.class);
                startActivity(intent);
            }
        });

        BankPayment = findViewById(R.id.cardViewtwo);
        BankPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageCredit.this, paystackweb.class);
                startActivity(intent);
            }
        });


        //for the phone text

        phonedail = findViewById(R.id.number1);
        phonedail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:08089502903"));
                startActivity(intent);
            }
        });

        phonedial2 = findViewById(R.id.number2);
        phonedial2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:08052491357"));
                startActivity(intent);
            }
        });
        phonedial3 = findViewById(R.id.number3);
        phonedial3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:090525646454"));
                startActivity(intent);
            }
        });

        phonemessage = findViewById(R.id.message2);
        phonemessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sms_uri = Uri.parse("smsto:+2348089502903");
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "Hello Excel, I would like to purchase an Ecourse Rep message pin");
                startActivity(sms_intent);
            }
        });

        phonemessage2 = findViewById(R.id.message3);
        phonemessage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sms_uri = Uri.parse("smsto:+2348089502903");
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "Hello Ifeanyi, I would like to purchase an Ecourse Rep message pin");
                startActivity(sms_intent);
            }
        });

        phonemessage3 = findViewById(R.id.message4);
        phonemessage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sms_uri = Uri.parse("smsto:+2348089502903");
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "Hello Chibuzor, I would like to purchase an Ecourse Rep message pin");
                startActivity(sms_intent);
            }
        });


        //for the phone text

        //for the message

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
    }
}
