package com.example.imagesteganographyapp;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText,fName, lName,phoneEdt;
    private TextView tvPassCondition,btnGoToLogin;
    private ImageView showPasswordIcon;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference("users");
    private boolean isPasswordVisible = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        tvPassCondition = findViewById(R.id.passwordCriteriaTextView);
        showPasswordIcon = findViewById(R.id.showPasswordIcon);
        Button registerButton = findViewById(R.id.registerButton);
        phoneEdt =findViewById(R.id.phoneEdt);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);



       btnGoToLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
           }
       });

        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });


        passwordEditText.addTextChangedListener(new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvPassCondition.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswordCriteria(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}


        });



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showPasswordIcon.setImageResource(R.drawable.hidden);
        } else {
            passwordEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showPasswordIcon.setImageResource(R.drawable.show);
        }

        passwordEditText.setSelection(passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void validatePasswordCriteria(String password) {

        boolean hasLowercase = false;
        boolean hasSpecial = false;
        boolean hasNumber = false;


        String specialChars = "!@#$%^&*()-_=+{}[]|:;<>,.?/~`";
        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            }
            if (Character.isDigit(ch)) {
                hasNumber = true;
            }
            if (specialChars.contains(String.valueOf(ch))) {
                hasSpecial = true;
            }
        }


        boolean isLongEnough = password.length() >= 6;


        if (hasLowercase && hasSpecial && hasNumber && isLongEnough) {
            tvPassCondition.setText("You meet all conditions.");
            tvPassCondition.setTextColor(getColor(R.color.btnColor));
        } else {
            tvPassCondition.setText("Password must be at least 6 characters long and include a letter, a special character, and a number.");
            tvPassCondition.setTextColor(Color.RED);
        }

    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String firstName = fName.getText().toString().trim();
        String lastName = lName.getText().toString().trim();
        String phone = phoneEdt.getText().toString().trim();



        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(emailPattern)) {
            emailEditText.setError("Invalid email format");
            return;
        }

        if (tvPassCondition.getCurrentTextColor() == Color.RED) {
            passwordEditText.setError("Your password is not strong");
            return;
        }

        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty())
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {


                            String userId = auth.getCurrentUser().getUid();
                            UserModel model = new UserModel(firstName,lastName,email,password,phone);
                            ref.child(userId).setValue(model).addOnCompleteListener(task1 -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }




    }



    @Override
    protected void onStart() {
        super.onStart();


        if (auth.getCurrentUser() != null && auth.getCurrentUser().getUid() != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }
}

