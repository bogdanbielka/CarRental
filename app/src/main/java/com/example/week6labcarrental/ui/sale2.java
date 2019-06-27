package com.example.week6labcarrental.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.week6labcarrental.R;
import com.example.week6labcarrental.model.Car;

public class sale2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale2);
        Button btnCalc = findViewById(R.id.btnCalc);
        Button btnBook = findViewById(R.id.btnBook);
        TextView txtCar = findViewById(R.id.txtCar);
        Intent intent = getIntent();
        String make = intent.getStringExtra("make");
        String model = intent.getStringExtra("model");
        Double pHour = intent.getDoubleExtra("pHour",0);
        Double pDay = intent.getDoubleExtra("pDay",0);

        StringBuilder s = new StringBuilder();
        s.append("Car : " + make + " " + model + "\n");
        s.append("Price per hour: " + pHour + "\n");
        s.append("Price per day: " + pDay);
        txtCar.setText(s);

    }
}
