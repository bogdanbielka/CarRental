package com.example.week6labcarrental.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.week6labcarrental.R;
import com.example.week6labcarrental.adapter.ItemClickListener;
import com.example.week6labcarrental.adapter.MyRecyclerAdapter;
import com.example.week6labcarrental.adapter.UserRecyclerAdapter;
import com.example.week6labcarrental.controller.Authentication;
import com.example.week6labcarrental.controller.PopUp;
import com.example.week6labcarrental.firebase.CarCollection;
import com.example.week6labcarrental.firebase.UserCollection;
import com.example.week6labcarrental.model.Car;
import com.example.week6labcarrental.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ManagerActivity extends AppCompatActivity implements  ItemClickListener {
    //declarations
    private FirebaseFirestore db;
    public static final String COLLECTION_NAME = "cars";
    private final String Tag = "ManagerActivity";
    //In CarData.json file, availability spelling wrong as "availibility"
    private final String[] key = {"carId", "category", "pricePerHour", "pricePerDay", "carMake", "carModel", "color", "availability"};
    private String UID;
    // Firebase auth
    private FirebaseAuth mAuth;

    EditText search;
    LinearLayout searchBox;
    Dialog dialog;
    // lists
    ArrayList<Car> carsList;
    ArrayList<User> usersList;
    // Listener response
    BroadcastReceiver response;
    //Recycler View
    RecyclerView recyclerView;
    MyRecyclerAdapter carRecyclerAdapter;
    UserRecyclerAdapter userRecyclerAdapter;
    ItemClickListener itemClickListener;

    Button addNewUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        carsList = new ArrayList<>();
        initialize();
        dialog = new Dialog(this);

        searchBox = findViewById(R.id.searchBox);
        addNewUser = findViewById(R.id.btnAddNew);

        recyclerView =  findViewById(R.id.myRec);
        carRecyclerAdapter = new MyRecyclerAdapter(carsList, this);
        userRecyclerAdapter = new UserRecyclerAdapter(usersList, this);
        recyclerView.setAdapter(carRecyclerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(ManagerActivity.this, 1));

        TabLayout myTabLayout = (TabLayout) findViewById(R.id.myTabLayout);
        myTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        // change adapter in RecyclerView
                        recyclerView.setAdapter(carRecyclerAdapter);
                        searchBox.setVisibility(View.GONE);
                        addNewUser.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        //change adapter in RecyclerView
                        recyclerView.setAdapter(userRecyclerAdapter);
                        searchBox.setVisibility(View.GONE);
                        addNewUser.setVisibility(View.GONE);
                        break;
                    case 2:
                        searchBox.setVisibility(View.GONE);
                        addNewUser.setVisibility(View.GONE);
                        break;
                    case 3:
                        searchBox.setVisibility(View.VISIBLE);
                        addNewUser.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                }
            }
        });
        // add new user button click
        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show pop up
                PopUp.openAddNewCarPopup(ManagerActivity.this, db, dialog);
            }
        });

        //initialize response
        response = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case CarCollection.LOAD_CAR_DATA_DONE :
                        //get car Data from intent
                        carsList = (ArrayList<Car>) intent.getSerializableExtra("cars_data");
                        Log.i("carlist", String.valueOf(carsList.size()));
                        carRecyclerAdapter.changeData(carsList);
                        carRecyclerAdapter.notifyDataSetChanged();
                        break;
                    case UserCollection.LOAD_USER_LIST_DATA_DONE :
                        //get user Data from intent
                        usersList = (ArrayList<User>) intent.getSerializableExtra("users_data");
                        userRecyclerAdapter.changeData(usersList);
                        userRecyclerAdapter.notifyDataSetChanged();
                        break;
                    case CarCollection.ADD_CAR_DATA_DONE :
                        //get user Data from intent
                        Car newCar = (Car) intent.getSerializableExtra("new_car_data");
                        carsList.add(newCar);
                        //carRecyclerAdapter.changeData(carsList);
                        carRecyclerAdapter.notifyDataSetChanged();
                        break;
                    case CarCollection.UPDATE_CAR_DATA_DONE :

                        carRecyclerAdapter.notifyDataSetChanged();
                        break;

                }
            }
        };

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
    //====================Connection
    private void initialize() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
        db.setFirestoreSettings(settings);
        //get all the cars
        CarCollection.getAllCars(this,db);
        //get all users
        UserCollection.getAllUsers(this,db);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        super.onPause();
        unregisterReceiver(response);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // An IntentFilter can match against actions, categories, and data
        IntentFilter filter = new IntentFilter();
        filter.addAction(CarCollection.LOAD_CAR_DATA_DONE);
        filter.addAction(UserCollection.LOAD_USER_LIST_DATA_DONE);
        filter.addAction(CarCollection.ADD_CAR_DATA_DONE);
        filter.addAction(CarCollection.UPDATE_CAR_DATA_DONE);
        registerReceiver(response,filter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Car clickedCar = carsList.get(position);
        switch (view.getId()) {
            case R.id.carItemLayout:
                //open PopUp
                PopUp.openUpdateCarPopup(ManagerActivity.this, db,dialog,  clickedCar);
                break;
        }

    }

    @Override
    public void onItemLongClick(View view, int position) {
        Toast.makeText(this, "Long clicked", Toast.LENGTH_SHORT).show();
        //call the delete function

    }
//====================End
//
//    private void addData() {
//        Map<String, Object> cars = new HashMap<>();
//        EditText et1 = findViewById(R.id.carIdEdit);
//        String ID = et1.getText().toString();
//        EditText et2 = findViewById(R.id.carCategoryEdit);
//        String category = et2.getText().toString();
//        EditText et3 = findViewById(R.id.carPriceHourEdit);
//        String pricePerHour = et3.getText().toString();
//        EditText et4 = findViewById(R.id.carPriceDayEdit);
//        String pricePerDay = et4.getText().toString();
//        EditText et5 = findViewById(R.id.carMakerEdit);
//        String maker = et5.getText().toString();
//        EditText et6 = findViewById(R.id.carModelEdit);
//        String model = et6.getText().toString();
//        EditText et7 = findViewById(R.id.carColorEdit);
//        String color = et7.getText().toString();
//        EditText et8 = findViewById(R.id.carAvailableEdit);
//        String available = et8.getText().toString();
//
//        cars.put(key[0], ID);
//        cars.put(key[1], category);
//        cars.put(key[2], pricePerHour);
//        cars.put(key[3], pricePerDay);
//        cars.put(key[4], maker);
//        cars.put(key[5], model);
//        cars.put(key[6], color);
//        cars.put(key[7], available);
//
//        et1.setText("");
//        et2.setText("");
//        et3.setText("");
//        et4.setText("");
//        et5.setText("");
//        et6.setText("");
//        et7.setText("");
//        et8.setText("");
//
//        db.collection(COLLECTION_NAME)
//                .add(cars)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(Tag, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(Tag, "Error adding document " + e);
//                    }
//                });
//
//    }

//    private void displayAllData() {
//        EditText et1 = findViewById(R.id.carIdEdit);
//        EditText et2 = findViewById(R.id.carCategoryEdit);
//        EditText et3 = findViewById(R.id.carPriceHourEdit);
//        EditText et4 = findViewById(R.id.carPriceDayEdit);
//        EditText et5 = findViewById(R.id.carMakerEdit);
//        EditText et6 = findViewById(R.id.carModelEdit);
//        EditText et7 = findViewById(R.id.carColorEdit);
//        EditText et8 = findViewById(R.id.carAvailableEdit);
//        et1.setText("");
//        et2.setText("");
//        et3.setText("");
//        et4.setText("");
//        et5.setText("");
//        et6.setText("");
//        et7.setText("");
//        et8.setText("");
//        db.collection(COLLECTION_NAME)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            TextView tv = findViewById(R.id.resultTxt);
//                            tv.setText("");
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                tv.append(document.getId() + " => " + document.getData() + "\n");
//                                //display it one by one
//                                //tv.append("\t\t" + key[0]+ " is " + document.getData().get(key[0])+"\n");
//                                Log.d(Tag, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(Tag, "Error getting documents", task.getException());
//                        }
//                    }
//                });
//    }
//
//    private void searchData() {
//        CollectionReference ref = db.collection(COLLECTION_NAME);
//        EditText et = findViewById(R.id.carIdEdit);
//
//        Query query = ref.whereEqualTo(key[0], et.getText().toString());
//        //UID = "";
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            TextView tv = findViewById(R.id.resultTxt);
//                            tv.setText("");
//                            EditText et2 = findViewById(R.id.carCategoryEdit);
//                            EditText et3 = findViewById(R.id.carPriceHourEdit);
//                            EditText et4 = findViewById(R.id.carPriceDayEdit);
//                            EditText et5 = findViewById(R.id.carMakerEdit);
//                            EditText et6 = findViewById(R.id.carModelEdit);
//                            EditText et7 = findViewById(R.id.carColorEdit);
//                            EditText et8 = findViewById(R.id.carAvailableEdit);
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                tv.append(document.getId() + " => " + document.getData());
//                                Log.d(Tag, document.getId() + " => " + document.getData());
//
//                                //UID = document.getId();
//                                et2.setText(document.getData().get(key[1]) + "");
//                                et3.setText(document.getData().get(key[2]) + "");
//                                et4.setText(document.getData().get(key[3]) + "");
//                                et5.setText(document.getData().get(key[4]) + "");
//                                et6.setText(document.getData().get(key[5]) + "");
//                                et7.setText(document.getData().get(key[6]) + "");
//                                et8.setText(document.getData().get(key[7]) + "");
//                            }
//
//                        } else {
//                            Log.w(Tag, "Error getting document.", task.getException());
//                        }
//                    }
//                });
//    }
//
//    private void updateData() {
//        CollectionReference ref = db.collection(COLLECTION_NAME);
//        EditText et = findViewById(R.id.carIdEdit);
//
//        Query query = ref.whereEqualTo(key[0], et.getText().toString());
//        UID = "";
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            TextView tv = findViewById(R.id.resultTxt);
//                            tv.setText("");
//                            EditText et1 = findViewById(R.id.carIdEdit);
//                            EditText et2 = findViewById(R.id.carCategoryEdit);
//                            EditText et3 = findViewById(R.id.carPriceHourEdit);
//                            EditText et4 = findViewById(R.id.carPriceDayEdit);
//                            EditText et5 = findViewById(R.id.carMakerEdit);
//                            EditText et6 = findViewById(R.id.carModelEdit);
//                            EditText et7 = findViewById(R.id.carColorEdit);
//                            EditText et8 = findViewById(R.id.carAvailableEdit);
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                tv.append(document.getId() + " => " + document.getData());
//                                Log.d(Tag, document.getId() + " => " + document.getData());
//
//                                UID = document.getId();
//                                Map<String, Object> car = new HashMap<>();
//                                car.put(key[0], et1.getText().toString());
//                                car.put(key[1], et2.getText().toString());
//                                car.put(key[2], et3.getText().toString());
//                                car.put(key[3], et4.getText().toString());
//                                car.put(key[4], et5.getText().toString());
//                                car.put(key[5], et6.getText().toString());
//                                car.put(key[6], et7.getText().toString());
//                                car.put(key[7], et8.getText().toString());
//                                db.collection(COLLECTION_NAME).document(UID).update(car);
//
//                                searchData();
//                            }
//
//                        } else {
//                            Log.w(Tag, "Error getting document.", task.getException());
//                        }
//                    }
//                });
//    }
//
//    private void deleteData(String cardId) {
//        CollectionReference ref = db.collection(COLLECTION_NAME);
//      //  EditText et = findViewById(R.id.carIdEdit);
//
//        Query query = ref.whereEqualTo(key[0], cardId);
//        UID = "";
//        query.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                UID = document.getId();
//                                Log.d(Tag, UID);
//                                db.collection(COLLECTION_NAME).document(UID)
//                                        .delete()
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.d(Tag, "Success delete");
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.w(Tag, "Error delete", e);
//                                            }
//                                        });
//                            }
//                        } else {
//                            Log.w(Tag, "Error getting document.", task.getException());
//                        }
//                    }
//                });
//    }
}





