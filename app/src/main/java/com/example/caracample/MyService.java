package com.example.caracample;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {

    ServiceThread th = new ServiceThread();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://getdata-from-rapa-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference();
    String danger_car_name = "";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        android.util.Log.i("서비스 테스트", "onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i("서비스 테스트", "onDestroy()");
        th.interrupt();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // foreground
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setContentText("카라캠플 실행중");
        builder.setContentTitle("카라캠플");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "default", NotificationManager.IMPORTANCE_DEFAULT));
        }

        Notification notification = builder.build();
        android.util.Log.i("서비스 테스트", "onStartCommand()");
        startForeground(1,notification);
        th.start();

        return super.onStartCommand(intent, flags, startId);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            // 코드버전 (제출 시 고치기)
            for (int i = 65; i <= 73; i++) {
                final int temp = i - 65;
                char c = (char) i;
                String car_name = "Caravan" + c;
                ref.child("cars_func").child(car_name).child("power").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue(power.class) != null) {
                            power power_post = snapshot.getValue(power.class);
                            if (power_post.getFire().equals("on")) {
                                danger_car_name += car_name + " ";
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "데이터 없음", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            // 화재 알림
            if (!danger_car_name.equals("")) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String NOTIFICATION_ID = "10001";
                String NOTIFICATION_NAME = "동기화";
                int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_NAME, IMPORTANCE);
                    notificationManager.createNotificationChannel(channel);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyService.this, NOTIFICATION_ID)
                        .setContentTitle("화재 경보")
                        .setContentText(danger_car_name + "에서 화재가 발생하였습니다.")
                        .setSmallIcon(R.drawable.alarm);
                notificationManager.notify(0, builder.build());
                danger_car_name = "";

            }else{

            }
            // 화재 알림 끝

        }
    };

    class ServiceThread extends Thread {

        @Override
        public void run() {
            while (true) {
                Message msg = new Message();
                handler.sendMessage(msg);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }


}