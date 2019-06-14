package com.example.week6labcarrental.firebase;

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
     *
     * @param db
     * @param documentId
     * @return
     */
    public static User getUserInformation(FirebaseFirestore db, String documentId) {
        final User user = new User();
        DocumentReference userRef = db.collection("users").document(documentId);
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener <DocumentSnapshot> () {
                    @Override
                    public void onComplete( Task < DocumentSnapshot > task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            StringBuilder fields = new StringBuilder("");
                            user.setEmail(doc.get("email").toString());
                            user.setFullName(doc.get("fullName").toString());
                            user.setUserId(doc.get("userId").toString());
                            user.setRole(Integer.parseInt(doc.get("role").toString()));

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                    }
                });
        return user;
    }
}
