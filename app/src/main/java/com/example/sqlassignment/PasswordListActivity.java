package com.example.sqlassignment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlassignment.DatabaseHelper;
import com.example.sqlassignment.Password;
import com.example.sqlassignment.PasswordListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PasswordListActivity extends AppCompatActivity {

    private RecyclerView passwordRecyclerView;
    private PasswordListAdapter passwordListAdapter;
    private List<Password> passwordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        passwordRecyclerView = findViewById(R.id.passwordRecyclerView);
        passwordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME_PASSWORDS, null, null, null, null, null, null);

        if (cursor != null) {
            passwordList.clear();
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String appName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_APP_NAME));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CO_PASSWORD));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_URL));

                passwordList.add(new Password(appName, password, url));
            }
            cursor.close();
        }
        db.close();

        passwordListAdapter = new PasswordListAdapter(passwordList);
        passwordRecyclerView.setAdapter(passwordListAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("refresh_list");
        registerReceiver(refreshReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(refreshReceiver);
    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Refresh the password list
            // Implement your code to refresh the list here
        }
    };
}