package com.example.reno;

import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.android.volley.NetworkError;
    import com.android.volley.NoConnectionError;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.TimeoutError;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.example.reno.messenger.DBHelper;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;
    import com.google.android.material.snackbar.Snackbar;

    import org.apache.http.conn.ConnectTimeoutException;

    import java.io.UnsupportedEncodingException;
    import java.net.ConnectException;
    import java.net.SocketTimeoutException;
    import java.net.URLEncoder;
    import java.nio.charset.StandardCharsets;

public class SendMessageActivity extends AppCompatActivity {

    private EditText editSender;
    private EditText editRecipient;
    private EditText editMessage;
    private FloatingActionButton btnSend;
    private TextView textStatus;
    private ProgressDialog progressDialog;
    private String smsApiUrl;
    int count;
    private Spinner spinnerRecipient;
    private DBHelper dbHelper;
    private String addedNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        editSender = findViewById(R.id.edit_sender);
        spinnerRecipient = findViewById(R.id.spinner_recipient);
        editMessage = findViewById(R.id.edit_message);

        btnSend = findViewById(R.id.btn_Send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });

        textStatus = findViewById(R.id.text_status);

        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        progressDialog.setCancelable(false);

        count = 1;
        addedNumbers = "";
        dbHelper = new DBHelper(this);
        Cursor c = dbHelper.retrieve();
        while (c.moveToNext()) {
            String phone = c.getString(1);
            addedNumbers = phone + ", " + addedNumbers;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                startActivity(new Intent(this, AddNumber.class));
                return true;
            case R.id.menu_clear:
                clearFields();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearFields() {
        editSender.setText(""); editMessage.setText("");
        editSender.requestFocus();
    }

    private void validateInput() {
        textStatus.setVisibility(View.INVISIBLE);
        textStatus.setTextColor(getResources().getColor(R.color.black));

        String sender = editSender.getText().toString().trim();
        String recipient = spinnerRecipient.getSelectedItem().toString().trim();
        String message = editMessage.getText().toString().trim();

        if (sender.isEmpty()) {
            editSender.setError("Sender ID required.");
            editSender.requestFocus();
        }
        else if (message.isEmpty()) {
            editMessage.setError("Message required.");
            editMessage.requestFocus();
        }
        else {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            sendMessage(sender, recipient, message);
        }
    }

    private void sendMessage(final String msgSender, final String msgRecipient, final String msgMessage) {
        textStatus.setText("Processing...");
        textStatus.setVisibility(View.VISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            smsApiUrl = "https://www.estoresms.com/smsapi.php?username=chibuz&password=c91706766&sender=" +
                    URLEncoder.encode(msgSender, StandardCharsets.UTF_8.name()) + "&recipient=" +
                    URLEncoder.encode(addedNumbers, StandardCharsets.UTF_8.name()) + "&message=" +
                    URLEncoder.encode(msgMessage, StandardCharsets.UTF_8.name()) + "&";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, smsApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        switch (response) {
                            case "OK" :
                                textStatus.setText("SMS sent successfully!");
                                textStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                                break;
                            case "-2904":
                                textStatus.setText("SMS sending failed!");
                                textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                break;
                            case "-2905":
                                textStatus.setText("Authentication error!");
                                textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                break;
                            case "-2906":
                                textStatus.setText("Credit Exhausted!");
                                textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                break;
                            case "-2907":
                                textStatus.setText("Gateway Unavailable!");
                                textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                break;
                            case "-2916":
                                textStatus.setText("Blocked message content!");
                                textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                break;
                            case "-2917":
                                textStatus.setText("Blocked sender id!");
                                textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                break;
                            default:
                                String text;
                                if (response.contains("OK")) {
                                    textStatus.setTextColor(getResources().getColor(R.color.colorGreen));
                                    text = "SMS sent successfully!";
                                }
                                else if (response.contains("-2904")) {
                                    textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                    text = "SMS sending failed.\nCheck your inputs.";
                                }
                                else if (response.contains("-2906")) {
                                    Toast.makeText(SendMessageActivity.this, "Credit Exhausted", Toast.LENGTH_SHORT).show();
                                    textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                    text = "Credit Exhausted.";
                                }
                                else {
                                    textStatus.setTextColor(getResources().getColor(R.color.colorRed));
                                    text = "Sender ID: " + msgSender + "\n" +
                                            "Destination numbers: " + msgRecipient + "\n" +
                                            "Message: " + msgMessage + "\n" +
                                            "Status: "+ response;
                                }
                                textStatus.setText(text);
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String err;
                textStatus.setTextColor(getResources().getColor(R.color.colorRed));

                if (error instanceof NetworkError || error.getCause() instanceof ConnectException) {

                    err = "Your device is not connected to the internet.";
                    Toast.makeText(SendMessageActivity.this, err, Toast.LENGTH_SHORT).show();
                }
                else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException) {
                    err = "Connection timeout.";
                    Toast.makeText(SendMessageActivity.this, err, Toast.LENGTH_SHORT).show();
                }
                else {
                    err = "An unknown error occurred. \nError message: " + error.toString();
                    Toast.makeText(SendMessageActivity.this, err, Toast.LENGTH_SHORT).show();
                }

                textStatus.setText(err);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
    }
}
