package com.example.project48;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project48.Login.LoginActivity;
import com.example.project48.Login.SignupActivity;
import com.example.project48.detail.detailActivity;
import com.example.project48.misc.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;

    FrameLayout fragmentContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bottomNavigationView = findViewById(R.id.bottomNav);
        viewPager2 = findViewById(R.id.viewPager);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.bottom_detail:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.bottom_list:
                        viewPager2.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_detail).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bottom_list).setChecked(true);
                        break;
                }
                super.onPageSelected(position);
            }
        });
    }

    public ViewPager2 getViewPager2() {
        return viewPager2;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("DetailFragment", "onOptionsItemSelected: item selected " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_overflow:
                showPopupMenu();
                return true;
            default:
                return false;
        }
    }
    private void showPopupMenu() {
        View menuView = findViewById(R.id.action_overflow); // Make sure this is the correct view ID
        PopupMenu popup = new PopupMenu(MainActivity.this, menuView);

        // Check if the user is logged in
        SessionManager sessionManager = new SessionManager(MainActivity.this);
        boolean isLoggedIn = sessionManager.isLoggedIn();

        // Inflate the appropriate menu based on the user's login status
        if (isLoggedIn) {
            popup.getMenuInflater().inflate(R.menu.after_login_navigation_menu, popup.getMenu());
        } else {
            popup.getMenuInflater().inflate(R.menu.login_navigation_menu, popup.getMenu());
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (isLoggedIn) {
                    // Handle menu items for logged-in users
                    if (itemId == R.id.menu_add_point_toilet_add) {
                        // Handle profile menu item click
                        Intent intent = new Intent(MainActivity.this, AddToiletActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.menu_point_detail_report) {
                        // Handle logout menu item click
                        sessionManager.clearLoginSession();
                        Intent intent = new Intent(MainActivity.this, detailActivity.class);
                        startActivity(intent);
                        return true;
                    }
                } else {
                    // Handle menu items for non-logged-in users
                    if (itemId == R.id.menu_header_login) {
                        // Handle login menu item click
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return true;
                    } else if (itemId == R.id.menu_signup_login) {
                        // Handle signup menu item click
                        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });
        popup.show();
    }
}
