package com.example.week6labcarrental.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.week6labcarrental.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ManagerActivity extends AppCompatActivity {
    //declarations
    private FirebaseFirestore db;
    private final String COLLECTION_NAME = "cars";
    private final String Tag = "ManagerActivity";
    //In CarData.json file, availability spelling wrong as "availibility"
    private final String[] key = {"carId", "category", "pricePerHour", "pricePerDay", "carMake", "carModel", "color", "availability"};
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        initialize();

        Button addBtn = findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
            }
        });
        Button displayAllBtn = findViewById(R.id.btnDispAll);
        displayAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAllData();
            }
        });
        Button searchBtn = findViewById(R.id.btnSearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchData();
            }
        });
        Button updateBtn = findViewById(R.id.btnUpdate);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });
        Button deleteBtn = findViewById(R.id.btnDelete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

        //Listening to any change
        final CollectionReference docRef = db.collection(COLLECTION_NAME);
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Tag, "Listen Failed.", e);
                    return;
                }
                if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                    List<DocumentChange> data = queryDocumentSnapshots.getDocumentChanges();
                    Map<String, Object> cars;
                    for (DocumentChange dc : data) {
                        cars = dc.getDocument().getData();
                        Log.d(Tag, "Curret data:");
                        Log.d(Tag, cars.get(key[0]) + "," + cars.get(key[1]) + "," + cars.get(key[2]) + "," + cars.get(key[3]) + "," + cars.get(key[4]) + "," + cars.get(key[5]) + "," + cars.get(key[6]) + "," + cars.get(key[7]));
                    }
                } else {
                    Log.d(Tag, "Current data : null");
                }
            }
        });
    }

    //====================Connection
    private void initialize() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
        db.setFirestoreSettings(settings);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //====================End

    private void addData(){
        Map<String, Object> cars = new HashMap<>();
        EditText et1 = findViewById(R.id.carIdEdit);
        String ID = et1.getText().toString();

        Spinner et2 = findViewById(R.id.carCategoryEdit);
        final String [] catrgoryList ={"Sedan","Hatchbacks","SUV","Pickup trucks", "Sports car","Luxury vehicles","MPV"};
        ArrayAdapter<String> catrgoryListAA = new ArrayAdapter<>(ManagerActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                catrgoryList);
        et2.setAdapter(catrgoryListAA);
        et2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String category = catrgoryList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText et3 = findViewById(R.id.carPriceHourEdit);
        String pricePerHour = et3.getText().toString();
        EditText et4 = findViewById(R.id.carPriceDayEdit);
        String pricePerDay = et4.getText().toString();
        EditText et5 = findViewById(R.id.carMakerEdit);
        String maker = et5.getText().toString();
        EditText et6 = findViewById(R.id.carModelEdit);
        String model = et6.getText().toString();
        EditText et7 = findViewById(R.id.carColorEdit);
        String color = et7.getText().toString();
        Spinner et8 = findViewById(R.id.carAvailableEdit);
        final String [] availabilityList ={"True","False"};
        ArrayAdapter<String> availabilityListAA = new ArrayAdapter<>(ManagerActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                availabilityList);
        et8.setAdapter(availabilityListAA);
        et8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String availability = availabilityList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void displayAllData(){

    }

    private void searchData(){

    }

    private void updateData(){

    }

    private void deleteData(){

    }
}


