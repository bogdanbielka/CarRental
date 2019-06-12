package com.example.week6labcarrental.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.week6labcarrental.R;
import com.example.week6labcarrental.controller.PopUp;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView createAccount;
    Dialog dialog;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(this);
        // find resource id
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccount:
                PopUp.openRegisterPopup(dialog, LoginActivity.this, progressDialog);
                break;
        }
    }
}
