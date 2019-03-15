package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    ImageButton imageButton;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function:" + "onDestroy()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function:" + "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function:" + "onResume()");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(ACTIVITY_NAME, "In function:" + "onCreate()");

        setContentView(R.layout.activity_profile);

        Intent prevPage = getIntent();

        String emailTyped = prevPage.getStringExtra(getString(R.string.preference_reserved_email));

        EditText editText = findViewById(R.id.editText4);
        editText.setText(emailTyped);

        ImageButton imageButton = findViewById(R.id.imageButtonPicture);

        imageButton.setOnClickListener(v -> {


            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        });

        Button gotochatButton = findViewById(R.id.buttongotochat);

        gotochatButton.setOnClickListener( v -> {
            Intent chatIntent = new Intent(ProfileActivity.this, ChatRoomActivity.class);
            startActivity(chatIntent);
        });

        Button gotoToolbarButton = findViewById(R.id.buttongototoolbar);

        gotoToolbarButton.setOnClickListener( v -> {
            Intent toolbarIntent = new Intent(ProfileActivity.this, TestToolbar.class);
            startActivity(toolbarIntent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function:" + "onStart()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function:" + "onStop()");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(ACTIVITY_NAME, "In function:" + "onActivityResult()");


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBmp = (Bitmap)extras.get("data");
            imageButton = findViewById(R.id.imageButtonPicture);
            imageButton.setImageBitmap(imageBmp);
        }
    }
}
