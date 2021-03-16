package com.example.caracample;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class QnAFragment extends Fragment {
    String ip = "192.168.35.251";
    EditText input_msg;
    Button btn_submit;
    TextView tv_car_name;

    RequestQueue requestQueue; // 데이터가 전송되는 통로
    StringRequest stringRequest; // 내가 보낼 데이터

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(getActivity());
        View fragment = inflater.inflate(R.layout.fragment_qn_a, container, false);
        input_msg = fragment.findViewById(R.id.input_msg);
        btn_submit = fragment.findViewById(R.id.btn_submit);

        String url = "http://"+ip+":8081/ThirdProject/consultInsert.do";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    Toast.makeText(getActivity(), "문의가 정상적으로 전달되었습니다.", Toast.LENGTH_SHORT).show();
                    input_msg.setText("");
                }else{
                    Toast.makeText(getActivity(), "문의 전달이 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            // StringRequest 내의 메소드 오버로딩

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> data = new HashMap<>();
                tv_car_name = getActivity().findViewById(R.id.tv_car_name);
                String car_name = tv_car_name.getText().toString();
                data.put("car_name",car_name);
                data.put("cmt",input_msg.getText().toString());
                return data;
            }
        };

        stringRequest.setTag("MAIN"); // stringRequest 태그를 항상 지정해 주어야 함 (여러개 쓸 때 구분해주기 위해)

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_msg.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "문의사항을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    requestQueue.add(stringRequest);
                }
            }
        });

        return fragment;
    }
}