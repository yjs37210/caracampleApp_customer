package com.example.caracample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaterFragment extends Fragment {

    ProgressBar progressBar;
    TextView tv_water;
    int num = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://getdata-from-rapa-default-rtdb.firebaseio.com/");
    DatabaseReference ref = database.getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_water, container, false);

        progressBar = fragment.findViewById(R.id.progressBar);
        tv_water = fragment.findViewById(R.id.tv_water);
        progressBar.setIndeterminate(false);
        String car_name = getActivity().getIntent().getStringExtra("car_name").substring(0,8);

        ref.child("cars_func").child(car_name).child("water").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setProgress(Integer.parseInt(snapshot.getValue().toString()));
                tv_water.setText(snapshot.getValue()+"L");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return fragment;
    }
}