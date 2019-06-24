package com.example.week6labcarrental.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.week6labcarrental.R;
import com.example.week6labcarrental.ui.ManagerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PopUp {
    /**
     * This function open a popup for user to register a new account
     *
     * @param dialog
     * @param context
     * @param progressDialog
     * @param mAuth
     */
    public static void openRegisterPopup(final FirebaseFirestore db, final Dialog dialog, final Context context, final ProgressDialog progressDialog, final FirebaseAuth mAuth) {

        dialog.setContentView(R.layout.register_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView closeBtn = dialog.findViewById(R.id.btnClose);
        final EditText email = dialog.findViewById(R.id.txtEmail);
        final EditText fullName = dialog.findViewById(R.id.txtFullName);
        final EditText txtpassword = dialog.findViewById(R.id.txtPassword);
        final EditText confirmPassword = dialog.findViewById(R.id.txtConfirmPassword);
        final Button btnSignUp = dialog.findViewById(R.id.btnRegister);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = fullName.getText().toString().trim();
                String emailString = email.getText().toString().trim();
                String pw = txtpassword.getText().toString().trim();
                String pw1 = confirmPassword.getText().toString().trim();
                if(TextUtils.isEmpty(emailString)){
                    Toast.makeText(context,"Please fill up email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(context,"Please fill up name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pw)){
                    Toast.makeText(context,"Please fill up password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pw1)){
                    Toast.makeText(context,"Please fill up confirm password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pw.equals(pw1)){
                    Toast.makeText(context,"Password doesn't match. Try Again",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setMessage("Register...");
                progressDialog.show();
                //Register new user
                Authentication.registerNewUser(dialog,db,context, mAuth, progressDialog,emailString, pw, userName);

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public static void openAddNewCarPopup(final FirebaseFirestore db, final Dialog dialog, final Context context, final FirebaseAuth mAuth) {

        dialog.setContentView(R.layout.add_new_car_popup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView closeBtn = dialog.findViewById(R.id.btnClose);
                Map<String, Object> cars = new HashMap<>();
        String ID =  UUID.randomUUID().toString();
        EditText et2 = dialog.findViewById(R.id.carCategoryEdit);
        String category = et2.getText().toString();
        EditText et3 = dialog.findViewById(R.id.carPriceHourEdit);
        String pricePerHour = et3.getText().toString();
        EditText et4 = dialog.findViewById(R.id.carPriceDayEdit);
        String pricePerDay = et4.getText().toString();
        EditText et5 = dialog.findViewById(R.id.carMakerEdit);
        String maker = et5.getText().toString();
        EditText et6 = dialog.findViewById(R.id.carModelEdit);
        String model = et6.getText().toString();
        EditText et7 = dialog.findViewById(R.id.carColorEdit);
        String color = et7.getText().toString();
        CheckBox carAvailable = dialog.findViewById(R.id.carAvailableCheckBox);
        boolean available = carAvailable.isChecked();

//        cars.put(key[0], ID);
//        cars.put(key[1], category);
//        cars.put(key[2], pricePerHour);
//        cars.put(key[3], pricePerDay);
//        cars.put(key[4], maker);
//        cars.put(key[5], model);
//        cars.put(key[6], color);
//        cars.put(key[7], available);

        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
        et6.setText("");
        et7.setText("");
        carAvailable.setChecked(true);

        db.collection(ManagerActivity.COLLECTION_NAME)
                .add(cars)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(Tag, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.d(Tag, "Error adding document " + e);
                    }
                });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
