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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private Button createAccountButton;
    private EditText phoneNumberInput, usernameInput, passwordInput;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button) findViewById(R.id.register_btn);
        usernameInput = (EditText) findViewById(R.id.register_username_input);
        phoneNumberInput = (EditText) findViewById(R.id.register_phone_number_input);
        passwordInput = (EditText) findViewById(R.id.register_password_input);

        loadingBar = new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String username = usernameInput.getText().toString();
        String phoneNumber = phoneNumberInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please write your name... ", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Please write your phone number... ", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write a secure password... ", Toast.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Create account");
            loadingBar.setMessage("Please wait while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            validatePhoneNumber(username, phoneNumber, password);
        }
    }

    private void validatePhoneNumber(String username, String phoneNumber, String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Users").child(phoneNumber).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phoneNumber", phoneNumber);
                    userdataMap.put("password", password);
                    userdataMap.put("username", username);

                    rootRef.child("Users").child(phoneNumber).updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Your account has been created!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();


                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Network Error: Please try again!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "This " + phoneNumber + " already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}