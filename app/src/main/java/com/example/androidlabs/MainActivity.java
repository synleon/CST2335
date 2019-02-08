package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    EditText editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference for SharedPreferences
        String key = getString(R.string.preference_file_key);
        prefs = getSharedPreferences(key, Context.MODE_PRIVATE);

        // get reserved email from SharedPreferences
        key = getString(R.string.preference_reserved_email);
        String email = prefs.getString(key, ""); ;

        // push reserved email to edit control
        editEmail = findViewById(R.id.editTextEmail);
        editEmail.setText(email);

        Button btnLogin = findViewById(R.id.login);

        btnLogin.setOnClickListener( v -> {
            Intent profilePage = new Intent(MainActivity.this, ProfileActivity.class);


            // get email user typed
            String emailTyped = editEmail.getText().toString();
            profilePage.putExtra(getString(R.string.preference_reserved_email), emailTyped);

            startActivity(profilePage);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor  = prefs.edit();
        String key = getString(R.string.preference_reserved_email);
        String emailtyped = editEmail.getText().toString();
        editor.putString(key, emailtyped);
        editor.commit();

    }
}
