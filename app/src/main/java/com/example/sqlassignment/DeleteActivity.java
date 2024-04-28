package com.example.sqlassignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity {
    EditText appNameToDelete;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        appNameToDelete = findViewById(R.id.appNameToDelete);
        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appName = appNameToDelete.getText().toString().trim();

                // Pass back the app name to the previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("appName", appName);
                resultIntent.putExtra("refreshList", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
