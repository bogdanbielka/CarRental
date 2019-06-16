package com.example.week6labcarrental.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.week6labcarrental.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
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

    private void addData() {
        Map<String, Object> cars = new HashMap<>();
        EditText et1 = findViewById(R.id.carIdEdit);
        String ID = et1.getText().toString();
        EditText et2 = findViewById(R.id.carCategoryEdit);
        String category = et2.getText().toString();
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
        EditText et8 = findViewById(R.id.carAvailableEdit);
        String availeble = et8.getText().toString();


        cars.put(key[0], ID);
        cars.put(key[1], category);
        cars.put(key[2], pricePerHour);
        cars.put(key[3], pricePerDay);
        cars.put(key[4], maker);
        cars.put(key[5], model);
        cars.put(key[6], color);
        cars.put(key[7], availeble);

        db.collection(COLLECTION_NAME)
                .add(cars)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(Tag, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(Tag, "Error adding document " + e);
                    }
                });

    }

    private void displayAllData() {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            TextView tv = findViewById(R.id.resultTxt);
                            tv.setText("");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tv.append(document.getId() + " => " + document.getData() + "\n");
                                //display it one by one
                                //tv.append("\t\t" + key[0]+ " is " + document.getData().get(key[0])+"\n");
                                Log.d(Tag, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(Tag, "Error getting documents", task.getException());
                        }
                    }
                });
    }

    private void searchData() {
        CollectionReference ref = db.collection(COLLECTION_NAME);
        EditText et = findViewById(R.id.carIdEdit);

        Query query = ref.whereEqualTo(key[0], et.getText().toString());
        UID = "";
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            TextView tv = findViewById(R.id.resultTxt);
                            tv.setText("");
                        }
                    }
                });
    }

    private void updateData() {

    }

    private void deleteData() {

    }
}





