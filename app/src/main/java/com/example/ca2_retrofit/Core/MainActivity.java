package com.example.ca2_retrofit.Core;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ca2_retrofit.News.NewsFragment;
import com.example.ca2_retrofit.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();
        }

    }
}
