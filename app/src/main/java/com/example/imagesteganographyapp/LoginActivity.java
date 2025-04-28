package com.example.imagesteganographyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth auth;
    boolean isPasswordVisible = false;
    TextView forgotPassTv;
    ImageView showBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPassEdt);
        Button loginButton = findViewById(R.id.loginButton);
       forgotPassTv = findViewById(R.id.forgotPasswordTextView);
        showBtn = findViewById(R.id.showBtn);

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgotPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

            }
        });



       showBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               togglePasswordVisibility();
           }
       });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                          Intent i = new Intent(LoginActivity.this,MainActivity.class);
                          startActivity(i);
                          finish();
                    } else {
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void togglePasswordVisibility() {
        if (isPasswordVisible) {

            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showBtn.setImageResource(R.drawable.hidden);
        }
        else {

            passwordEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showBtn.setImageResource(R.drawable.show);
        }

        passwordEditText.setSelection(passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
}
