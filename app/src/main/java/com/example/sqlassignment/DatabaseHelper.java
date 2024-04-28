package com.example.sqlassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserDatabase.db";
    public static final int DATABASE_VERSION = 5;
    public static final String COL_IS_DELETED = "is_deleted";
    public static final String TABLE_NAME = "users";
    public static final String COL_ID = "id";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_GENDER = "gender";

    public static final String TABLE_NAME_PASSWORDS = "passwords";
    public static final String COL_PASSWORD_ID = "id";
    public static final String COL_USER_ID = "user_id"; // New foreign key column
    public static final String COL_APP_NAME = "app_name";
    public static final String CO_PASSWORD = "password";
    public static final String COL_URL = "url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FIRST_NAME + " TEXT, "
                + COL_LAST_NAME + " TEXT, "
                + COL_EMAIL + " TEXT, "
                + COL_PASSWORD + " TEXT, "
                + COL_GENDER + " TEXT)";

        String SQL_CREATE_PASSWORDS_TABLE = "CREATE TABLE " + TABLE_NAME_PASSWORDS + " ("
                + COL_PASSWORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_ID + " INTEGER, " // Foreign key column
                + COL_APP_NAME + " TEXT, "
                + CO_PASSWORD + " TEXT, "
                + COL_URL + " TEXT, "
                + COL_IS_DELETED + " INTEGER DEFAULT 0,"
        +"FOREIGN KEY(" + COL_USER_ID + ") REFERENCES " + TABLE_NAME + "(" + COL_ID + "))";

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_PASSWORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PASSWORDS);
        onCreate(db);
    }
}