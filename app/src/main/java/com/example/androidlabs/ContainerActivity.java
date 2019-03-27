package com.example.androidlabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Bundle dataToPass = getIntent().getExtras();

        // add a fragment
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(dataToPass);
        detailFragment.setTablet(true);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, detailFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
