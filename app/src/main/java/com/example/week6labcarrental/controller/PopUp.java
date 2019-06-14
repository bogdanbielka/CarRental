package com.example.week6labcarrental.controller;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.week6labcarrental.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
