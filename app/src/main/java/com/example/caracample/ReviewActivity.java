package com.example.caracample;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityManager;

import android.app.AlertDialog;

import android.content.Context;

import android.content.DialogInterface;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageView;

import android.widget.Toast;


import com.android.volley.AuthFailureError;

import com.android.volley.Request;

import com.android.volley.RequestQueue;

import com.android.volley.Response;

import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

import java.util.Map;


public class ReviewActivity extends AppCompatActivity {

    String ip = "192.168.35.251";
    EditText input_review;
    ImageView[] imgs = new ImageView[5];
    Button btn_exit;
    int score = -1;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://getdata-from-rapa-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        requestQueue = Volley.newRequestQueue(this);
        String car_name = getIntent().getStringExtra("car_name");
        btn_exit = findViewById(R.id.btn_exit);
        input_review = findViewById(R.id.input_review);


        for (int i = 0; i < imgs.length; i++) {
            final int temp = i;
            int imgID = getResources().getIdentifier("img_score" + (i + 1), "id", getPackageName());
            imgs[i] = findViewById(imgID);
            imgs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i <= temp; i++) {
                        imgs[i].setImageResource(R.drawable.yellow_star); //btn_star_big_on
                        score = temp;
                    }
                    if (temp != 4) {
                        for (int i = 4; i > temp; i--) {
                            imgs[i].setImageResource(R.drawable.grey_star);
                        }
                    }
                }
            });
        }


        String url = "http://"+ip+":8081/ThirdProject/carsExit.do";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override

            public void onResponse(String response) {

                if (response.equals("1")) {
                    //ref연습
                    led leds = new led("off", "off", "off", "off");
                    ref.child("cars_func").child(car_name).child("led").setValue(leds);
                    power powers = new power("off", "off", "off");
                    ref.child("cars_func").child(car_name).child("power").setValue(powers);
                    Toast.makeText(ReviewActivity.this, "퇴실이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReviewActivity.this, "퇴실 실패", Toast.LENGTH_SHORT).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {


            @Override

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("car_name", car_name);
                data.put("score", score + 1 + "");
                data.put("cmt_review", input_review.getText().toString());
                return data;
            }

        };
        stringRequest.setTag("MAIN");

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (score == -1) {
                    Toast.makeText(ReviewActivity.this, "별점을 남겨주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder msgBuilder = new AlertDialog.Builder(ReviewActivity.this).setTitle(
                            "퇴실하기").setMessage("퇴실하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isServiceRunning(MyService.class.getName())) {
                                Intent intent = new Intent(ReviewActivity.this, MyService.class);
                                stopService(intent);
                            }
                            requestQueue.add(stringRequest);
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog msgDlg = msgBuilder.create();
                    msgDlg.show();
                }
            }
        });

    }

    public Boolean isServiceRunning(String class_name) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (class_name.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }

}