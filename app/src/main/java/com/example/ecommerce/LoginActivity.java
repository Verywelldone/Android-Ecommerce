package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.model.Users;
import com.example.ecommerce.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberInput, passwordInput;
    private Button loginButton;

    private ProgressDialog loadingBar;
    private TextView adminLink;
    private TextView notAdminLink;

    private String parentDbName = "Users";
    private CheckBox rememberMeChkb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginButton = (Button) findViewById(R.id.login_btn);
        phoneNumberInput = (EditText) findViewById(R.id.login_phone_number_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);

        loadingBar = new ProgressDialog(this);
        rememberMeChkb = (CheckBox) findViewById(R.id.remember_me_chkb);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);

        Paper.init(this);

        loginButton.setOnClickListener(v -> loginUser());

        adminLink.setOnClickListener(v -> {
            loginButton.setText("Login as Admin");
            adminLink.setVisibility(View.INVISIBLE);
            notAdminLink.setVisibility(View.VISIBLE);
            parentDbName = "Admins";
        });

        notAdminLink.setOnClickListener(v -> {
            loginButton.setText("Login");
            adminLink.setVisibility(View.VISIBLE);
            notAdminLink.setVisibility(View.INVISIBLE);
            parentDbName = "Users";
        });
    }

    private void loginUser() {
        String phoneNumber = phoneNumberInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please write your phone number... ", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password... ", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(phoneNumber, password);
        }
    }

    private void allowAccessToAccount(String phoneNumber, String password) {

        if (rememberMeChkb.isChecked()) {
            Paper.book().write(Prevalent.userPhoneKey, phoneNumber);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(phoneNumber).exists()) {
                    Users userData = snapshot.child(parentDbName).child(phoneNumber).getValue(Users.class);

                    assert userData != null;
                    if (userData.getPhoneNumber().equals(phoneNumber)) {
                        if (userData.getPassword().equals(password)) {
                            {
                                if (parentDbName.equals("Admins")) {
                                    Toast.makeText(LoginActivity.this, "Welcome Admin. You are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                    startActivity(intent);
                                } else if (parentDbName.equals("Users")) {
                                    Toast.makeText(LoginActivity.this, "Logged in Succesfully...", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

//                                     TODO: MainActivity should be restored to HomeActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }

                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect...", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Account whit this phone number do not exists!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Please create a new Account!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}