package com.example.sqlassignment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    RadioGroup genderRadioGroup;
    Button signUpButton, exitButton;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        signUpButton = findViewById(R.id.signUpButton);
        exitButton = findViewById(R.id.exitButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Initialize SQLiteOpenHelper
        openHelper = new DatabaseHelper(this);


        // Sign Up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String gender = ((RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert user data into SQLite database
                db = openHelper.getWritableDatabase();
                long result = insertData(firstName, lastName, email, password, gender);
                if (result != -1) {
                    Toast.makeText(MainActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Exit button click listener
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
            }
        });
    }

    private long insertData(String firstName, String lastName, String email, String password, String gender) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_FIRST_NAME, firstName);
        contentValues.put(DatabaseHelper.COL_LAST_NAME, lastName);
        contentValues.put(DatabaseHelper.COL_EMAIL, email);
        contentValues.put(DatabaseHelper.COL_PASSWORD, password);
        contentValues.put(DatabaseHelper.COL_GENDER, gender);

        try {
            db = openHelper.getWritableDatabase();
            return db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return an error code
        }
    }

}
