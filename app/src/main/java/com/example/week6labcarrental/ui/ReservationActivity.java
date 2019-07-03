package com.example.week6labcarrental.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week6labcarrental.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.common.reflect.Reflection.initialize;

public class ReservationActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private Button btnReserve, btnBack;

    private TextView txCar, txColor, txPrice, txDate;

    private Double pday;
    private String make, model, color, car;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String PickUpDate;

        btnReserve = findViewById(R.id.btnReserve);
        btnBack = findViewById(R.id.btnBack);

        txCar = findViewById(R.id.txtCarInfo);
        txColor = findViewById(R.id.txtColorInfo);
        txPrice = findViewById(R.id.txtPriceInfo);
        txDate = findViewById(R.id.txtDateInfo);

        intent = getIntent();

        final String PREF_NAME = "MyPrefsFile";
        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        final String id = UUID.randomUUID().toString();

        pday = intent.getDoubleExtra("carPrice", 0);
        make = intent.getStringExtra("carMake");
        model = intent.getStringExtra("carModel");
        color = intent.getStringExtra("carColor");
        PickUpDate = pref.getString("PickUpDate", "");

        txCar.setText(make + " " + model);
        txColor.setText(color);
        txPrice.setText(pday.toString());
        txDate.setText(PickUpDate);
        car = make + " " + model;

        mAuth = FirebaseAuth.getInstance();
        initialize();

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("car", car);
                map.put("cost", pday);
                map.put("date", PickUpDate);

                db.collection("reservations")
                        .document(id)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Reservation Added", "DocumentSnapshot successfully written!");
                                Toast.makeText(getApplicationContext(), "Your reservation ID is " + id, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Reservation Added", "Error!");
                    }
                });
                //Toast.makeText(getApplicationContext(), "Reservation Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReservationActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);

                builder.setMessage("Start reservation again?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ReservationActivity.this, ClientActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }
}
