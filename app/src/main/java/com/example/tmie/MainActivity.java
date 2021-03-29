package com.example.tmie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tmie.fragment.FragCourse;
import com.example.tmie.fragment.FragCompany;
import com.example.tmie.fragment.FragSupport;
import com.example.tmie.fragment.FragBoard;
import com.example.tmie.fragment.FragHome;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Bundle bundle = new Bundle();

    private FragmentManager fm;
    private FragmentTransaction ft;

    private FragCourse fragCourse;
    private FragCompany fragCompany;
    private FragSupport fragSupport;
    private FragBoard fragBoard;
    private Fragment fragment_ac;

    private boolean fr_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_default);

        FragHome fragHome = new FragHome();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragHome)
                .commit();
        fragment_ac = new Fragment();

        bottomNavigationView = findViewById(R.id.bottomNavi);
        // 메뉴 바 아이콘을 눌렀을 때의 화면 동작
        // 각 화면 코드는 fragment 폴더에 있음

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    // 홈 화면(약속 목록)으로 이동
                    case R.id.menu_home:
                        FragHome fragHome = new FragHome();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragHome)
                                .commit();
                        return true;
                    case R.id.menu_board:
                        FragBoard fragBoard = new FragBoard();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragBoard)
                                .commit();
                        return true;
                    case R.id.menu_support:
                        FragSupport fragSupport = new FragSupport();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragSupport)
                                .commit();

                        return true;
                    case R.id.menu_company:
                        FragCompany fragCompany = new FragCompany();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragCompany)
                                .commit();
                        return true;
                    case R.id.menu_course:
                        FragCourse fragCourse = new FragCourse();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragCourse)
                                .commit();
                        return true;
                }
                return false;
            }

        });
        }

}