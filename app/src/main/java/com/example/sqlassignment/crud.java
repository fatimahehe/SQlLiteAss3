package com.example.sqlassignment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class crud extends AppCompatActivity {

    int userId;
    EditText appNameText, passwordText, urlText;
    Button btnAdd, btnDelete, btnView, btnUpdate,backupBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud);

        // Initialize EditTexts
        appNameText = findViewById(R.id.appNameText);
        passwordText = findViewById(R.id.passwordText);
        urlText = findViewById(R.id.urlText);

        // Initialize Buttons
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        backupBtn=findViewById(R.id.btnBackup);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set click listeners for buttons
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appName = appNameText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                String url = urlText.getText().toString().trim();

                // Check if any of the fields are empty
                if (TextUtils.isEmpty(appName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(url)) {
                    Toast.makeText(crud.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert data into the database
                long result = insertData(appName, password, url);

                // Check if insertion was successful
                if (result != -1) {
                    Toast.makeText(crud.this, "Password added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(crud.this, "Failed to add password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the DeleteActivity
                Intent intent = new Intent(crud.this, DeleteActivity.class);
                startActivityForResult(intent, 123);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement code to view passwords
                Intent intent = new Intent(crud.this, PasswordListActivity.class);
                intent.putExtra("userId", getUserIdFromDatabase());// Replace userID with the actual user ID
                startActivity(intent);
            }
        });
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(crud.this, DeletedPasswordsActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appName = appNameText.getText().toString().trim();
                final String password = passwordText.getText().toString().trim();
                final String url = urlText.getText().toString().trim();

                // Check if any of the fields are empty
                if (TextUtils.isEmpty(appName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(url)) {
                    Toast.makeText(crud.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the app name exists in the database
                if (isAppExists(appName)) {
                    // Update the password entry in the database
                    int rowsUpdated = updatePassword(appName, password, url);

                    // Check if update was successful
                    if (rowsUpdated > 0) {
                        Toast.makeText(crud.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(crud.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(crud.this, "App does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to insert data into the passwords table
    private long insertData(String appName, String password, String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_APP_NAME, appName);
        values.put(DatabaseHelper.CO_PASSWORD, password);
        values.put(DatabaseHelper.COL_URL, url);

        // Inserting Row
        long result = -1;
        try {
            result = db.insertOrThrow(DatabaseHelper.TABLE_NAME_PASSWORDS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }
        return result;
    }

    @SuppressLint("Range")
    private int getUserIdFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int userId = -1; // Default value
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, new String[]{DatabaseHelper.COL_ID}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID));
            cursor.close();
        }
        db.close();
        return userId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
            // Get the app name from the DeleteActivity result
            String appNameToDelete = data.getStringExtra("appName");

            // Delete the password entry from the database
            int rowsDeleted = deletePassword(appNameToDelete);

            // Check if deletion was successful
            if (rowsDeleted > 0) {
                Toast.makeText(crud.this, "Password deleted successfully", Toast.LENGTH_SHORT).show();
                boolean refreshList = data.getBooleanExtra("refreshList", false);
                if (refreshList) {
                    Intent refreshIntent = new Intent("refresh_list");
                    sendBroadcast(refreshIntent);
                }
            } else {
                Toast.makeText(crud.this, "Failed to delete password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to delete a password entry from the database based on app name
    private int deletePassword(String appName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        try {
            rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME_PASSWORDS,
                    DatabaseHelper.COL_APP_NAME + " = ?",
                    new String[]{appName});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close(); // Closing database connection
        }

        return rowsDeleted;
    }
    // Method to check if the app name exists in the database
    private boolean isAppExists(String appName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME_PASSWORDS,
                new String[]{DatabaseHelper.COL_APP_NAME},
                DatabaseHelper.COL_APP_NAME + " = ?",
                new String[]{appName},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Method to update a password entry in the database
    private int updatePassword(String appName, String newPassword, String newUrl) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CO_PASSWORD, newPassword);
        values.put(DatabaseHelper.COL_URL, newUrl);

        int rowsUpdated = db.update(DatabaseHelper.TABLE_NAME_PASSWORDS, values,
                DatabaseHelper.COL_APP_NAME + " = ?", new String[]{appName});
        db.close();
        return rowsUpdated;
    }

}
