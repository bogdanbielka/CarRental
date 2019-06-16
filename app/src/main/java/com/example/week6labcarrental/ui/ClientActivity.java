package com.example.week6labcarrental.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.week6labcarrental.R;
import com.example.week6labcarrental.controller.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class ClientActivity extends AppCompatActivity {

    Button btnPickup, btnReturn;
    DatePickerDialog.OnDateSetListener setListener;
    TextView txtpickup, txtreturn;
    String pickupdate, returndate; // serves as holder for the dates
    // Firebase auth
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnPickup = findViewById(R.id.btnPickup);
        btnReturn = findViewById(R.id.btnReturn);
        txtpickup = findViewById(R.id.txtPickup);
        txtreturn = findViewById(R.id.txtReturn);

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        btnPickup.setOnClickListener(new View.OnClickListener() {
            Calendar cal = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ClientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = day + "/" + month + "/" + year;
                        pickupdate = date;
                        txtpickup.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = day + "/" + month + "/" + year;
            }
        };

        btnReturn.setOnClickListener(new View.OnClickListener() {
            Calendar cal = Calendar.getInstance();
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ClientActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = day + "/" + month + "/" + year;
                        returndate = date;
                        txtreturn.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Authentication.checkSignIn(mAuth, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_signout:
                //Do sign out
                mAuth.signOut();
                Authentication.checkSignIn(mAuth, this);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
