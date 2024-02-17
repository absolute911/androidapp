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

public class MainpageActivity extends AppCompatActivity {

    private DrawerLayout drawer; // 定義 DrawerLayout 作為類成員變量，便於在其他方法中訪問

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 設置 Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 初始化 DrawerLayout


        // 找到 ImageButton 並設置點擊事件監聽器
        ImageButton imageButtonLogout = findViewById(R.id.imageButtonSettingsIcon3);
        imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 執行登出操作
                logout();
            }
        });

        ImageButton imageButtonMapButton = findViewById(R.id.imageButtonMapButton);
        imageButtonMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail();
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
        Intent intent = new Intent(MainpageActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // 關閉當前活動
    }

    private void detail(){
        Intent intent = new Intent(MainpageActivity.this, detailActivity.class);
        startActivity(intent);
        finish();
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

