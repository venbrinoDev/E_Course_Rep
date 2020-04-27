package com.example.reno;


    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.app.AlertDialog;
    import android.app.Dialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.Cursor;
    import android.os.Bundle;
    import android.text.Html;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.reno.messenger.CustomAdapter;
    import com.example.reno.messenger.DBHelper;
    import com.example.reno.messenger.PhoneNumbers;
    import com.example.reno.ui.home.HomeFragment;
    import com.google.android.material.floatingactionbutton.FloatingActionButton;
    import com.google.android.material.snackbar.Snackbar;

    import java.util.ArrayList;

public class AddNumber extends AppCompatActivity {

    private ListView listItem;
    private CustomAdapter adapter;
    ArrayList<PhoneNumbers> phoneNumbersArrayList = new ArrayList<>();
    private DBHelper helper;
    final Boolean forUpdate = true;
    private AlertDialog dialog;
    private EditText txtPhone;
    private TextView textTitle;
    public FloatingActionButton fabUpp;
    private TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_number);
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        listItem = findViewById(R.id.list_item);
        TextView textEmpty = findViewById(R.id.text_empty);
        listItem.setEmptyView(textEmpty);
        adapter = new CustomAdapter(this, phoneNumbersArrayList);

        fabUpp = findViewById(R.id.fab_upl);
        fabUpp.setVisibility(View.INVISIBLE);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog(false);

            }
        });


        fabUpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(listItem, "Phone number Uploaded To cloud", Snackbar.LENGTH_SHORT).show();
                fabUpp.setVisibility(View.INVISIBLE);
            }
        });

        helper = new DBHelper(this);
        this.getPhoneNumbers();
    }


    private void displayDialog(Boolean forUpdate) {
        dialog = new AlertDialog.Builder(this).create();
        View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_layout, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        textTitle = dialogView.findViewById(R.id.text_title_dialog);
        txtPhone = dialogView.findViewById(R.id.txtPhone);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);
        Button btnClose = dialogView.findViewById(R.id.btn_close);

        if (!forUpdate) {
            textTitle.setText("Add Phone Numbers");
            btnAdd.setText("Add");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtPhone.getText().toString().isEmpty()) {
                        txtPhone.setError("Number required.");
                        txtPhone.requestFocus();
                    }
                    else {
                        if (txtPhone.getText().toString().length() > 10) {
                            if (helper.checkIfPhoneExist(txtPhone.getText().toString())) {
                                txtPhone.setError("Phone number already exist");
                                txtPhone.requestFocus();
                            }
                            else {
                                addPhone(txtPhone.getText().toString());
                                fabUpp.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            txtPhone.setError("Invalid phone number");
                        }
                    }
                }
            });
        }
        else {
            txtPhone.setText(adapter.getSelectedItemPhone());
            textTitle.setText("Edit Phone Number");
            btnAdd.setText("Edit");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtPhone.getText().toString().isEmpty()) {
                        txtPhone.setError("Number required.");
                        txtPhone.requestFocus();
                    }
                    else {
                        if (txtPhone.getText().toString().length() > 10) {
                            if (helper.checkIfPhoneExist(txtPhone.getText().toString())) {
                                txtPhone.setError("Phone number already exist");
                                txtPhone.requestFocus();
                            }
                            else {
                                updatePhone(txtPhone.getText().toString());
                            }
                        }
                        else {
                            txtPhone.setError("Invalid phone number");
                            txtPhone.requestFocus();
                        }
                    }
                }
            });
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        CharSequence title = item.getTitle();
        if (title == "Edit") {
            displayDialog(forUpdate);
        }
        else if (title == "Delete") {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Confirmation");
            builder.setMessage("Sure to delete?");
            builder.setNegativeButton(Html.fromHtml("<font color='#000000'>Cancel</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setPositiveButton(Html.fromHtml("<font color='#000000'>Delete</font>"), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deletePhone();
                }
            });
            builder.show();
        }
        return super.onContextItemSelected(item);
    }

    private void deletePhone() {
        int id = adapter.getSelectedItemId();
        boolean isDeleted = helper.delete(id);
        if (isDeleted) {
            Snackbar.make(listItem, "Phone number deleted.", Snackbar.LENGTH_SHORT).show();
            this.getPhoneNumbers();
        }
        else {
            Toast.makeText(this, "Unable To Delete", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePhone(String newPhone) {
        int id = adapter.getSelectedItemId();
        boolean isUpdated = helper.update(newPhone, id);
        if (isUpdated) {
            this.getPhoneNumbers();
            dialog.cancel();
            Snackbar.make(listItem, "Phone number updated.", Snackbar.LENGTH_SHORT).show();
            fabUpp.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(this, "Unable To Update", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPhone(String phone) {
        boolean isSaved = helper.add(phone);
        if (isSaved) {
            this.getPhoneNumbers();
            dialog.cancel();
            Snackbar.make(listItem, "Phone number added.", Snackbar.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Unable to Add", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPhoneNumbers() {
        phoneNumbersArrayList.clear();
        Cursor cursor = helper.retrieve();
        PhoneNumbers phoneNumbers = null;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String phone = cursor.getString(1);
            phoneNumbers = new PhoneNumbers(id, phone);
            phoneNumbers.setId(id);
            phoneNumbers.setPhone(phone);
            phoneNumbersArrayList.add(phoneNumbers);
        }
        listItem.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
    }
}
