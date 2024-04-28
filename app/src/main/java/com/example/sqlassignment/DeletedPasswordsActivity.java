package com.example.sqlassignment;

import static com.example.sqlassignment.DatabaseHelper.COL_APP_NAME;
import static com.example.sqlassignment.DatabaseHelper.COL_IS_DELETED;
import static com.example.sqlassignment.DatabaseHelper.COL_PASSWORD_ID;
import static com.example.sqlassignment.DatabaseHelper.COL_URL;
import static com.example.sqlassignment.DatabaseHelper.COL_USER_ID;
import static com.example.sqlassignment.DatabaseHelper.CO_PASSWORD;
import static com.example.sqlassignment.DatabaseHelper.TABLE_NAME_PASSWORDS;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeletedPasswordsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DeletedPasswordsAdapter adapter;
    private List<Password> deletedPasswords;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_passwords);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        deletedPasswords = getDeletedPasswords();

        adapter = new DeletedPasswordsAdapter(deletedPasswords);
        recyclerView.setAdapter(adapter);
    }

    private List<Password> getDeletedPasswords() {
        deletedPasswords = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME_PASSWORDS,
                new String[]{DatabaseHelper.COL_PASSWORD_ID, DatabaseHelper.COL_APP_NAME, DatabaseHelper.CO_PASSWORD, DatabaseHelper.COL_URL},
                DatabaseHelper.COL_IS_DELETED + " = ?",
                new String[]{"1"},
                null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int passwordId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_PASSWORD_ID));
            @SuppressLint("Range") String appName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_APP_NAME));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CO_PASSWORD));
            @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_URL));
            deletedPasswords.add(new Password(appName, password, url));
        }

        cursor.close();
        db.close();

        return deletedPasswords;
    }
}