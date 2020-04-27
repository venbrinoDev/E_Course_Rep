package com.example.reno;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class CardCollection extends AppCompatActivity {

    //variables
    private Card card;
    private Charge charge;
    public ProgressBar prog;

    private EditText emailField;
    private EditText cardNumberField;
    private EditText expiryMonthField;
    private EditText expiryYearField;
    private EditText cvvField;


    private String email, cardNumber, cvv;
    private int expiryMonth, expiryYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PaystackSdk.initialize(getApplicationContext());
        setContentView(R.layout.activity_card_collection);

        Button payBtn = (Button) findViewById(R.id.pay_button);
        prog = findViewById(R.id.proggress);
        prog.setVisibility(View.INVISIBLE);

        emailField = (EditText) findViewById(R.id.edit_email_address);
        cardNumberField = (EditText) findViewById(R.id.edit_card_number);
        expiryMonthField = (EditText) findViewById(R.id.edit_expiry_month);
        expiryYearField = (EditText) findViewById(R.id.edit_expiry_year);
        cvvField = (EditText) findViewById(R.id.edit_cvv);


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                try {
                    email = emailField.getText().toString().trim();
                    cardNumber = cardNumberField.getText().toString().trim();
                    expiryMonth = Integer.parseInt(expiryMonthField.getText().toString().trim());
                    expiryYear = Integer.parseInt(expiryYearField.getText().toString().trim());
                    cvv = cvvField.getText().toString().trim();

                    //String cardNumber = "4084084084084081";
                    //int expiryMonth = 11; //any month in the future
                    //int expiryYear = 18; // any year in the future
                    //String cvv = "408";
                    card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

                    if (card.isValid()) {
                        Toast.makeText(CardCollection.this, "Card is Valid", Toast.LENGTH_LONG).show();
                        prog.setVisibility(View.VISIBLE);

                        final ProgressDialog dialog = new ProgressDialog(CardCollection.this);

                        dialog.setMessage("Performing transaction... please wait");
                        dialog.setProgressStyle(getColor(R.color.colorGreen));
                        dialog.show();


                        //From The Method

                        charge = new Charge();

                        //set the card to charge
                        charge.setCard(card);

                        //call this method if you set a plan
                        //charge.setPlan("PLN_yourplan");

                        charge.setEmail(email); //dummy email address

                        charge.setAmount(1000); //test amount

                        PaystackSdk.chargeCard(CardCollection.this, charge, new Paystack.TransactionCallback() {
                            @Override
                            public void onSuccess(Transaction transaction) {
                                prog.setVisibility(View.INVISIBLE);
                                Toast.makeText(CardCollection.this, "Transaction success", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                                // This is called only after transaction is deemed successful.
                                // Retrieve the transaction, and send its reference to your server
                                // for verification.

                                String paymentReference = generateReference();
                                Toast.makeText(CardCollection.this, "Transaction Successful! payment reference: " + paymentReference, Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void beforeValidate(Transaction transaction) {
                                // This is called only before requesting OTP.
                                // Save reference so you may send to server. If
                                // error occurs with OTP, you should still verify on server.
                            }

                            @Override
                            public void onError(Throwable error, Transaction transaction) {
                                //handle error here
                                prog.setVisibility(View.INVISIBLE);
                                Toast.makeText(CardCollection.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });



                        //where the method ends


                    } else {
                        Toast.makeText(CardCollection.this, "Card not Valid", Toast.LENGTH_LONG).show();
                        prog.setVisibility(View.INVISIBLE);
                        Toast.makeText(CardCollection.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Method to perform the charging of the card
     */

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
            emailField.requestFocus();
        }

        String cardNumber = cardNumberField.getText().toString();
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberField.setError("Required.");
            valid = false;
        } else {
            cardNumberField.setError(null);
            cardNumberField.requestFocus();
        }


        String expiryMonth = expiryMonthField.getText().toString();
        if (TextUtils.isEmpty(expiryMonth)) {
            expiryMonthField.setError("Required.");
            valid = false;
        } else {
            expiryMonthField.setError(null);
            expiryMonthField.requestFocus();
        }

        String expiryYear = expiryYearField.getText().toString();
        if (TextUtils.isEmpty(expiryYear)) {
            expiryYearField.setError("Required.");
            valid = false;
        } else {
            expiryYearField.setError(null);
            expiryYearField.requestFocus();
        }

        String cvv = cvvField.getText().toString();
        if (TextUtils.isEmpty(cvv)) {
            cvvField.setError("Required.");
            valid = false;
        } else {
            cvvField.setError(null);
            cvvField.requestFocus();
        }

        return valid;
    }
    public String generateReference() {
        String keys = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = (int)(keys.length() * Math.random());
            sb.append(keys.charAt(index));
        }
        String key = sb.toString();

        Toast.makeText(CardCollection.this, key, Toast.LENGTH_SHORT).show();
        return sb.toString();

    }

}
