package com.example.caracample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {


    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home);

        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 롤링배너 움직이게 하는 코드
        initViews();
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
        HomeAdapter adapter = new HomeAdapter();
        adapter.setData(createPageList());

        final ViewPager pager = findViewById(R.id.viewPager);
        pager.setAdapter(adapter);
    }

    @NonNull
    private List<View> createPageList() {
        List<View> pageList = new ArrayList<>();
        pageList.add(createPageView(R.drawable.banner1)); // 배너1
        pageList.add(createPageView1(R.drawable.banner2)); // 배너2
        return pageList;
    }

    @NonNull
    private View createPageView(int background) {
        View view = new View(this);
        view.setBackgroundResource(R.drawable.banner1); // 배너1 보이게 가져오기

        return view;
    }
    @NonNull
    private View createPageView1(int color) {
        View view = new View(this);
        view.setBackgroundResource(R.drawable.banner2); // 배너2 보이게 가져오기
        return view;
    }


}
