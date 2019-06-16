package com.example.week6labcarrental.ui;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.week6labcarrental.R;

import java.util.Calendar;

public class ClientActivity extends AppCompatActivity {

    Button btnPickup, btnReturn;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        btnPickup = findViewById(R.id.btnPickup);
        btnReturn = findViewById(R.id.btnReturn);

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
                        btnPickup.setText(date);
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
                        btnReturn.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }
}
