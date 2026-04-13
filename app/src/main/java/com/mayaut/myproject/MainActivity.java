package com.mayaut.myproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        Fragment homeFragment = new HomeFragment();
        Fragment alarmFragment = new AlarmFragment();
        Fragment flashcardsFragment = new FlashcardsFragment();

        setCurrentFragment(homeFragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                setCurrentFragment(homeFragment);
            } else if (itemId == R.id.nav_alarm) {
                setCurrentFragment(alarmFragment);
            } else if (itemId == R.id.nav_flashcards) {
                setCurrentFragment(flashcardsFragment);
            }
            return true;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}
