package com.example.week6labcarrental.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.week6labcarrental.model.Car;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CarCollection {
    public static final String LOAD_CAR_DATA_DONE = "doneLoadCarData";
    public static final String COLLECTION_NAME = "cars";
    /**
     *
     * @param context
     * @param db Firebase store
     * @return List of cars in the DB
     */
    public static ArrayList<Car> getAllCars(final Context context, FirebaseFirestore db) {
        final ArrayList<Car> cars = new ArrayList<>();
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete( Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Car car = new Car();
                                Log.d("load doc", doc.getId() + " => " + doc.getData());
                                car.setCategory(doc.getString("category"));
                                car.setCarModel(doc.getString("carModel"));
                                car.setCarMake(doc.getString("carMake"));
                                car.setColor(doc.getString("color"));
                                car.setPricePerDay(Double.parseDouble(doc.getString("pricePerDay")));
                                car.setPricePerHour(Double.parseDouble(doc.getString("pricePerHour")));
                                car.setAvailibility(doc.getBoolean("availability"));
                                car.setCarId(doc.getString("carId"));
                                //add car to list
                                cars.add(car);
                            }
                        } else {
                            Log.w("load doc", "Error getting documents.", task.getException());
                        }
                        //Broadcast the result
                        Intent broadcast = new Intent();

                        broadcast.setAction(LOAD_CAR_DATA_DONE);
                        broadcast.putExtra("cars_data",cars);

                        context.sendBroadcast(broadcast);
                    }
                });
        return cars;
    }
}
