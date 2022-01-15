package com.cuansaver.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cuansaver.app.ActivityMain;
import com.cuansaver.app.R;
import com.cuansaver.app.databinding.ActivityAuthLoginBinding;
import com.cuansaver.app.databinding.ActivityLandingBinding;

public class ActivityLogin extends AppCompatActivity {
    private ActivityAuthLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        binding = ActivityAuthLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { intendedAction(); }
        });
    }
    protected void intendedAction(){
        Intent intent = null;
        intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }
}