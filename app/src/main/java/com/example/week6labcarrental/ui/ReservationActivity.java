package com.example.week6labcarrental.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.week6labcarrental.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.google.common.reflect.Reflection.initialize;

public class ReservationActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private Button btnReserve;

    private TextView txCar, txColor, txPrice, txDate;

    private Double pday;
    private String make, model, color;

    final String PREF_NAME = "MyPrefsFile";
    SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        final String PickUpDate;

        btnReserve = findViewById(R.id.btnReserve);

        txCar = findViewById(R.id.txtCarInfo);
        txColor = findViewById(R.id.txtColorInfo);
        txPrice = findViewById(R.id.txtPriceInfo);
        txDate = findViewById(R.id.txtDateInfo);

        intent = getIntent();

        pday = intent.getDoubleExtra("carPrice",0);
        make = intent.getStringExtra("carMake");
        model = intent.getStringExtra("carModel");
        color = intent.getStringExtra("carColor");
        PickUpDate = pref.getString("PickUpDate","");

        txCar.setText(make + " " + model);
        txColor.setText(color);
        txPrice.setText(pday.toString());
        //txDate.setText(PickUpDate);

        mAuth = FirebaseAuth.getInstance();
        initialize();

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
