package com.example.week6labcarrental.controller;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.week6labcarrental.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Authentication {
    /**
     * This function register new user
     * @param context
     * @param mAuth
     * @param email
     * @param password
     */
    public static void registerNewUser(final Dialog dialog, final FirebaseFirestore db, final Context context, final FirebaseAuth mAuth, final ProgressDialog progressDialog, final String email, String password, final String fullName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser userInfo = mAuth.getCurrentUser();
                            Map<String, Object> user = new HashMap<>();
                            user.put("userId", userInfo.getUid());
                            user.put("fullName", fullName);
                            user.put("email", email);
                            user.put("role", 1); // Set default role to 1 (User Role)
                            addNewUserToFirebase(db, user, userInfo.getUid());
                            progressDialog.hide();
                            dialog.dismiss();
                            Toast.makeText(context, "Successfully register!", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * This function sign in user with email and password
     * @param context
     * @param mAuth
     * @param email
     * @param password
     */
    public static void signIn(final Context context, final FirebaseAuth mAuth, final ProgressDialog progressDialog, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            // move to main activity
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.hide();
                    }
                });
    }
    public static void addNewUserToFirebase(FirebaseFirestore db, Map<String, Object> user, String userId) {

        // Add a new document with a generated ID
        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Add User", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Log.w("Add User", "Error writing document", e);
                    }
                });
    }
}
