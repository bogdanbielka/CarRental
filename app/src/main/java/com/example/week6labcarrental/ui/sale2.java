package com.example.week6labcarrental.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week6labcarrental.R;
import com.example.week6labcarrental.firebase.BookingCollection;
import com.example.week6labcarrental.model.Booking;
import com.example.week6labcarrental.model.Car;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.google.common.reflect.Reflection.initialize;

public class sale2 extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale2);
        //Button btnCalc = findViewById(R.id.btnCalc);
        Button btnBook = findViewById(R.id.btnBook);
        TextView txtCar = findViewById(R.id.txtCar);
        TextView txtDate = findViewById(R.id.txtDate);
        final EditText txtName = findViewById(R.id.txtName);
        final EditText txtDriverLicense = findViewById(R.id.txtDriverLicense);
        final EditText txtDeposit = findViewById(R.id.txtDeposit);
        Intent intent = getIntent();
        String make = intent.getStringExtra("make");
        String model = intent.getStringExtra("model");
        final Double pHour = intent.getDoubleExtra("pHour",0);
        final Double pDay = intent.getDoubleExtra("pDay",0);
        final String datePickup;
        String dateReturn;
        final String MY_PREFS_NAME = "MyPrefsFile";
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String id = UUID.randomUUID().toString();
        mAuth = FirebaseAuth.getInstance();
        initialize();

        StringBuilder s = new StringBuilder();
        s.append("Car : " + make + " " + model + "\n");
        s.append("Price per hour: " + pHour + "\n");
        s.append("Price per day: " + pDay);
        txtCar.setText(s);

        final StringBuilder s1 = new StringBuilder();
        s1.append(make + " " + model);
        final String car1 = s1.toString();
        datePickup = prefs.getString("datePickup","");

        StringBuilder sb = new StringBuilder();
        sb.append("Pickup Date : " + datePickup + "\n");
        txtDate.setText(sb);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString();
                String license = txtDriverLicense.getText().toString();
                String str = txtDeposit.getText().toString();
                Double deposit = Double.parseDouble(str);
                //Booking booking = new Booking(id,name,license,deposit,car,pHour,pDay,datePickup);
                //db.collection("bookings").document(b.getID()).set(b);

                Map<String,Object> m = new HashMap<>();
                m.put("name",name);
                m.put("license",license);
                m.put("deposit",deposit);
                m.put("car",car1);
                m.put("priceHour",pHour);
                m.put("priceDay",pDay);
                m.put("PickupDate",datePickup);

                db.collection("bookings")
                        .document(id)
                        .set(m)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Add Booking", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure( Exception e) {
                                Log.w("Add Booking", "Error writing document", e);
                            }
                        });

                Toast.makeText(getApplicationContext(),"Booking succesfully added",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(sale2.this,SaleActivity.class);
                sale2.this.startActivity(i);
            }
        });
    }
}
