package com.example.shiyan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FriendsAppMainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_app_main);

        toolbar = findViewById(R.id.haoyouliebiao_toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.inflateMenu(R.menu.mainmenu);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HaoyouliebiaoActivity())
                .commit();
    }
    
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.haoyouliebiao_bottom_menu:
                    selectedFragment = new HaoyouliebiaoActivity();
                    toolbar = findViewById(R.id.haoyouliebiao_toolbar);

                    break;
                case R.id.myprofile_bottom_menu:
                    selectedFragment = new ProfileFragment();
                    toolbar = findViewById(R.id.profile_toolbar);
                    break;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };
}