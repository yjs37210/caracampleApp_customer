package com.example.caracample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    String ip = "192.168.35.251";
    EditText input_id;
    EditText input_pw;
    Button btn_login;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);
        btn_login = findViewById(R.id.btn_login);
        input_id = findViewById(R.id.input_id);
        input_pw = findViewById(R.id.input_pw);

        String url = "http://"+ip+":8081/ThirdProject/carsLogin.do";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("true")){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("car_name",input_id.getText().toString());
                    input_id.setText("");
                    input_pw.setText("");
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                data.put("car_name",input_id.getText().toString());
                data.put("pw",input_pw.getText().toString());
                return data;
            }
        };

        stringRequest.setTag("MAIN");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue.add(stringRequest);
            }
        });
    }
}