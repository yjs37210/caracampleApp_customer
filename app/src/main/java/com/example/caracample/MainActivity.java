package com.example.caracample;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navi;
    Button btn_review;
    private Intent intent;
    String temperature;
    String humidity;

    MapFragment mapFragment;
    ControlFragment controlFragment;
    QnAFragment qnAFragment;
    WaterFragment waterFragment;
    TextView tv_car_name;
    TextView tv_temp;
    TextView tv_hum;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://getdata-from-rapa-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference();

    @Override
    public void onBackPressed() {
        if(isServiceRunning(MyService.class.getName())){
            stopService(intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = new MapFragment();
        controlFragment = new ControlFragment();
        qnAFragment = new QnAFragment();
        waterFragment = new WaterFragment();
        tv_temp = findViewById(R.id.tv_temp);
        tv_hum = findViewById(R.id.tv_hum);
        tv_car_name = findViewById(R.id.tv_car_name);
        navi = findViewById(R.id.bottomNavigationView);
        btn_review = findViewById(R.id.btn_review);
        String car_name = getIntent().getStringExtra("car_name");

        intent = new Intent(this, MyService.class);
        startService(intent);
        android.util.Log.i("Start Service","StartService()");

        tv_car_name.setText(car_name+" 이용을 환영합니다!");
        getSupportFragmentManager().beginTransaction().replace(R.id.container,mapFragment).commit();
        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_Map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,mapFragment).commit();
                        break;
                    case R.id.tab_Control:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,controlFragment).commit();
                        break;
                    case R.id.tab_water:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,waterFragment).commit();
                        break;
                    case R.id.tab_QnA:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,qnAFragment).commit();
                        break;
                }
                return true;
            }
        });

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
                intent.putExtra("car_name",car_name);
                startActivity(intent);
            }
        });

        ref.child("cars_func").child("car_name").setValue(car_name.substring(0,8));

        ref.child("cars_func").child(car_name.substring(0,8)).child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                temperature = snapshot.getValue().toString();
                tv_temp.setText("온도 : " +temperature + " ℃");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("cars_func").child(car_name.substring(0,8)).child("humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                humidity = snapshot.getValue().toString();
                tv_hum.setText("습도 : "+humidity + " %");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Boolean isServiceRunning(String class_name){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(class_name.equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}