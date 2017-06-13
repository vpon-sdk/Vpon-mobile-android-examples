package com.example.vpadnnative;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String activityKey = UUID.randomUUID().toString();
        StorageCenter storage = StorageCenter.instance();
        storage.set(activityKey, this);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            RecyclerViewListFragment fragment = RecyclerViewListFragment.newInstance(activityKey);
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

    }
}
