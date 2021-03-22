package com.example.caracample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://getdata-from-rapa-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference();

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
        android.util.Log.i("서비스 테스트", "onDestroy()");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setContentText("text");
        builder.setContentTitle("title");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "default", NotificationManager.IMPORTANCE_DEFAULT));
        }

        Notification notification = builder.build();
        android.util.Log.i("서비스 테스트", "onStartCommand()");
        startForeground(1,notification);

        for(char i = 65; i <= 73; i++){
            final int temp = i - 65;
            String car_name = "Caravan" + i;
            ref.child("cars_func").child(car_name).child("power").child("fire").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.getValue().toString().equals("on")){

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
                                .setContentText(car_name + "에서 화재가 발생하였습니다.")
                                .setSmallIcon(R.drawable.alarm);
                        notificationManager.notify(temp, builder.build());

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
    }

}