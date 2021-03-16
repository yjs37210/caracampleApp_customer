package com.example.caracample;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.w3c.dom.Text;

public class MapFragment extends Fragment {

    ImageView[] imgs = new ImageView[9];
    String car_name;
    TextView tv_car_name;
    Button btn_fire;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_map, container, false);

        for (int i = 0; i < imgs.length; i++) {
            int imgID = getResources().getIdentifier("img" + (i + 1), "id", getActivity().getPackageName());
            imgs[i] = fragment.findViewById(imgID);
        }

        btn_fire = fragment.findViewById(R.id.btn_fire);
        tv_car_name = getActivity().findViewById(R.id.tv_car_name);
        car_name = tv_car_name.getText().toString();
        char c = car_name.charAt(7); // 글자만 잘라오기
        int num = (int) c - 64;
        char c_mini = (char)(c+32); // 소문자로 변환하는 코드(이미지는 대문자 불가)

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imgs[num-1]); //gif를 가져오기 위한 소스
        int myID = getResources().getIdentifier("caravan"+c_mini+"gif","drawable",getActivity().getPackageName());
        Glide.with(getActivity()).load(myID).into(gifImage);

        btn_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FireActivity.class);
                startActivity(intent);
            }
        });

        return fragment;
    }
}