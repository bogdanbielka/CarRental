package com.example.week6labcarrental.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.week6labcarrental.R;
import com.example.week6labcarrental.controller.Authentication;
import com.example.week6labcarrental.controller.PopUp;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView createAccount;
    Dialog dialog;
    ProgressDialog progressDialog;
    Button logIn;
    EditText email, password;
    // Firebase auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(this);
        // find resource id
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(this);
        logIn = findViewById(R.id.login);
        logIn.setOnClickListener(this);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount:
                PopUp.openRegisterPopup(dialog, LoginActivity.this, progressDialog, mAuth);
                break;
            case R.id.login:
                progressDialog.setMessage("Login...");
                progressDialog.show();
                String em = email.getText().toString();
                String pw = password.getText().toString();
                Authentication.signIn(this, mAuth, progressDialog, em, pw);
                break;
        }
    }
}
