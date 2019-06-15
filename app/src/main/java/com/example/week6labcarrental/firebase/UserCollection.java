package com.example.week6labcarrental.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.week6labcarrental.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserCollection {
    public static final String LOAD_USER_DATA_DONE = "loadUserDataDone";
    /**
     * This function add a new user to User Collection
     * @param db  db reference
     * @param user user info
     * @param userId user id is user for Document name
     */
    public static void addNewUserToFirebase(FirebaseFirestore db, User user, String userId) {
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

    /**
     * Get user information, broadcast user info when finish
     * @param db
     * @param documentId
     */
    public static void getUserInformation(final Context context, FirebaseFirestore db, String documentId) {
        final User user = new User();
        DocumentReference userRef = db.collection("users").document(documentId);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener <DocumentSnapshot> () {
                    @Override
                    public void onComplete( Task < DocumentSnapshot > task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            user.setEmail(doc.getString("email"));
                            user.setFullName(doc.getString("fullName"));
                            user.setUserId(doc.getString("userId"));
                            user.setRole(doc.getLong("role").intValue());

                        }
                        //Broadcast the result
                        Intent broadcast = new Intent();

                        broadcast.setAction(LOAD_USER_DATA_DONE);
                        broadcast.putExtra("user_data",user);

                        context.sendBroadcast(broadcast);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                    }
                });

    }
}
