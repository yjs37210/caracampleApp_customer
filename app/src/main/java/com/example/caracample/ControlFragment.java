package com.example.caracample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class ControlFragment extends Fragment {

    ImageView img_led_liv;
    ImageView img_led_kit;
    ImageView img_led_bath;
    ImageView img_led_outdoor;
    ImageView img_air_power;
    ImageView img_sunroof_power;

    String led_liv_status;
    String led_kit_status;
    String led_bath_status;
    String led_outdoor_status;
    String air_power_status;
    String sunroof_power_status;

    Button btn_all_on;
    Button btn_all_off;




    FirebaseDatabase database = FirebaseDatabase.getInstance("https://getdata-from-rapa-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_control, container, false);
        img_led_liv = fragment.findViewById(R.id.img_led_liv);
        img_led_kit = fragment.findViewById(R.id.img_led_kit);
        img_led_bath = fragment.findViewById(R.id.img_led_bath);
        img_led_outdoor = fragment.findViewById(R.id.img_led_outdoor);
        img_air_power = fragment.findViewById(R.id.img_air_power);
        img_sunroof_power = fragment.findViewById(R.id.img_sunroof_power);
        btn_all_off = fragment.findViewById(R.id.btn_all_off);
        btn_all_on = fragment.findViewById(R.id.btn_all_on);

        String car_name = getActivity().getIntent().getStringExtra("car_name").substring(0,8);

        ref.child("cars_func").child(car_name).child("led").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(led.class) != null) {
                    led led_post = snapshot.getValue(led.class);
                    led_liv_status = led_post.getLiv();
                    if(led_liv_status.equals("off")){
                        img_led_liv.setImageResource(R.drawable.led_liv_off);
                    }else if(led_liv_status.equals("on")){
                        img_led_liv.setImageResource(R.drawable.led_liv_on);
                    }
                    led_kit_status = led_post.getKit();
                    if(led_kit_status.equals("off")){
                        img_led_kit.setImageResource(R.drawable.led_kit_off);
                    }else if(led_kit_status.equals("on")){
                        img_led_kit.setImageResource(R.drawable.led_kit_on);
                    }
                    led_bath_status = led_post.getBath();
                    if(led_bath_status.equals("off")){
                        img_led_bath.setImageResource(R.drawable.led_bath_off);
                    }else if(led_bath_status.equals("on")){
                        img_led_bath.setImageResource(R.drawable.led_bath_on);
                    }
                    led_outdoor_status = led_post.getOutdoor();
                    if(led_outdoor_status.equals("off")){
                        img_led_outdoor.setImageResource(R.drawable.led_light_off);
                    }else if(led_outdoor_status.equals("on")){
                        img_led_outdoor.setImageResource(R.drawable.led_light_on);
                    }
                } else {
                    Toast.makeText(getContext(), "데이터 없음", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("cars_func").child(car_name).child("power").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(power.class) != null){
                    power power_post = snapshot.getValue(power.class);
                    air_power_status = power_post.getAir();
                    if(air_power_status.equals("off")){
                        img_air_power.setImageResource(R.drawable.air_power_off);
                    }else if(air_power_status.equals("on")){
                        img_air_power.setImageResource(R.drawable.air_power_on);
                    }
                    sunroof_power_status = power_post.getSunroof();
                    if(sunroof_power_status.equals("off")){
                        img_sunroof_power.setImageResource(R.drawable.led_sunroof_off);
                    }else if(sunroof_power_status.equals("on")){
                        img_sunroof_power.setImageResource(R.drawable.led_sunroof_on);
                    }
                }else{
                    Toast.makeText(getContext(), "데이터 없음", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img_led_liv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(led_liv_status.equals("off")){
                    led_liv_status = "on";
                }else if(led_liv_status.equals("on")){
                    led_liv_status = "off";
                }
                ref.child("cars_func").child(car_name).child("led").child("liv").setValue(led_liv_status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        img_led_kit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(led_kit_status.equals("off")){
                    led_kit_status = "on";
                }else if(led_kit_status.equals("on")){
                    led_kit_status = "off";
                }
                ref.child("cars_func").child(car_name).child("led").child("kit").setValue(led_kit_status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        img_led_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(led_bath_status.equals("off")){
                    led_bath_status = "on";
                }else if(led_bath_status.equals("on")){
                    led_bath_status = "off";
                }
                ref.child("cars_func").child(car_name).child("led").child("bath").setValue(led_bath_status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        img_led_outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(led_outdoor_status.equals("off")){
                    led_outdoor_status = "on";
                }else if(led_outdoor_status.equals("on")){
                    led_outdoor_status = "off";
                }
                ref.child("cars_func").child(car_name).child("led").child("outdoor").setValue(led_outdoor_status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        img_air_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(air_power_status.equals("off")){
                    air_power_status = "on";
                }else if(air_power_status.equals("on")){
                    air_power_status = "off";
                }
                ref.child("cars_func").child(car_name).child("power").child("air").setValue(air_power_status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        img_sunroof_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sunroof_power_status.equals("off")){
                    sunroof_power_status = "on";
                }else if(sunroof_power_status.equals("on")){
                    sunroof_power_status = "off";
                }
                ref.child("cars_func").child(car_name).child("power").child("sunroof").setValue(sunroof_power_status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_all_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("cars_func").child(car_name).child("led").child("bath").setValue("off");
                ref.child("cars_func").child(car_name).child("led").child("kit").setValue("off");
                ref.child("cars_func").child(car_name).child("led").child("liv").setValue("off");
                ref.child("cars_func").child(car_name).child("led").child("outdoor").setValue("off");
                ref.child("cars_func").child(car_name).child("power").child("air").setValue("off");
                ref.child("cars_func").child(car_name).child("power").child("sunroof").setValue("off");

            }
        });
        btn_all_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("cars_func").child(car_name).child("led").child("bath").setValue("on");
                ref.child("cars_func").child(car_name).child("led").child("kit").setValue("on");
                ref.child("cars_func").child(car_name).child("led").child("liv").setValue("on");
                ref.child("cars_func").child(car_name).child("led").child("outdoor").setValue("on");
                ref.child("cars_func").child(car_name).child("power").child("air").setValue("on");
                ref.child("cars_func").child(car_name).child("power").child("sunroof").setValue("on");

            }
        });






        return fragment;
    }

}