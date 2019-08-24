package com.example.alayb.myapplication.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alayb.myapplication.EditProfile;
import com.example.alayb.myapplication.PasswordChange;
import com.example.alayb.myapplication.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by alayb on 24-Jul-19.
 */

public class FragmentProfile extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_profile,container,false);
        return view;
    }
    TextView username,fname,lname,email,phone,add,add2;
    ImageView img,btn;
    Button pass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username=view.findViewById(R.id.profile_username);
        fname=view.findViewById(R.id.profile_fname);
        lname=view.findViewById(R.id.profile_lname);
        email=view.findViewById(R.id.profile_email);
        phone=view.findViewById(R.id.profile_phone);
        img=view.findViewById(R.id.profile_image);
        add=view.findViewById(R.id.profile_add1);
        add2 =view.findViewById(R.id.profile_add2);
        btn=view.findViewById(R.id.edt_profile);
        pass=view.findViewById(R.id.edt_password);
        sharedPreferences=getActivity().getSharedPreferences("Login",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        username.setText(sharedPreferences.getString("userName",""));
        fname.setText(sharedPreferences.getString("firstName",""));
        lname.setText(sharedPreferences.getString("lastName",""));
        email.setText(sharedPreferences.getString("email",""));
        phone.setText(sharedPreferences.getString("phone",""));
        add.setText(sharedPreferences.getString("address",""));
        add2.setText(sharedPreferences.getString("address2",""));
        Glide.with(this).load(sharedPreferences.getString("profile","")).into(img);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),EditProfile.class);
                startActivity(i);
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),PasswordChange.class);
                startActivity(i);
            }
        });
    }
}