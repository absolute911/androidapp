package com.example.project48;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.PopupMenu; // 導入 PopupMenu 類
import android.widget.TextView;

import com.example.project48.Forum.ForumActivity;
import com.example.project48.misc.SessionManager;
import com.example.project48.detail.detailActivity;

public class MainpageActivity extends AppCompatActivity {

    private DrawerLayout drawer; // 定義 DrawerLayout 作為類成員變量，便於在其他方法中訪問

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String username = sessionManager.getUsername();
        TextView userName = findViewById(R.id.textView111);
        if (sessionManager.isLoggedIn()) {
            // User is logged in, proceed to the next activity or show logged in state
            userName.setText("你好,用戶" + username);
        } else {
            // User is not logged in, redirect to login activity
            userName.setText("Please login");
        }

        // 設置 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 初始化 DrawerLayout


        // 找到 ImageButton 並設置點擊事件監聽器
        ImageButton imageButtonLogout = findViewById(R.id.imageButtonaddButton);
        imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 執行登出操作
                logout();
            }
        });


        //toilet detail button
        ImageButton imageButtonMapButton = findViewById(R.id.imageButtonMapButton);
        imageButtonMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail();
            }
        });
        //toilet list button
        ImageButton imageButtonMapButton2 = findViewById(R.id.imageButtonMapButton2);
        imageButtonMapButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list();
            }
        });

        ImageButton imageFormButton = findViewById(R.id.imageButton);
        imageFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form();
            }
        });

        ImageButton imageForumButton = findViewById(R.id.imageButtonSettingsButton2);
        imageForumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forum();

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainpageActivity", "onOptionsItemSelected: item selected " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_overflow:
                showPopupMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // 登出操作
    private void logout() {
        // 在這裡執行登出操作，例如清除用戶會話、導航到登錄屏幕等
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.clearLoginSession();
        Intent intent = new Intent(MainpageActivity.this, IntroActivity.class);
        startActivity(intent);
        finish(); // 關閉當前活動
    }

    private void detail(){
        Intent intent = new Intent(MainpageActivity.this, detailActivity.class);
        startActivity(intent);
        //finish();
    }

    private void list(){
        Intent intent = new Intent(MainpageActivity.this, listActivity.class);
        startActivity(intent);
        //finish();
    }

    private void form(){
        Intent intent = new Intent(MainpageActivity.this, AddToiletActivity.class);
        startActivity(intent);
        //finish();
    }

    private void forum(){
        Intent intent = new Intent(MainpageActivity.this, ForumActivity.class);
        startActivity(intent);
        //finish();
    }

    // 實現顯示彈出菜單的邏輯
    private void showPopupMenu() {
        Log.d("MainpageActivity", "showPopupMenu: Showing popup menu");
        View menuView = findViewById(R.id.action_overflow); // 确保这是正确的视图ID
        PopupMenu popup = new PopupMenu(MainpageActivity.this, menuView);
        popup.getMenuInflater().inflate(R.menu.lateral_navigation_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item click here
                return false;
            }
        });
        popup.show();
    }


}

