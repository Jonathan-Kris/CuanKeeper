package com.cuankeeper.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cuankeeper.app.ActivityMain;
import com.cuankeeper.app.R;
import com.cuankeeper.app.databinding.ActivityAuthLoginBinding;
import com.cuankeeper.app.databinding.ActivityAuthRegisterBinding;

public class ActivityRegister extends AppCompatActivity {
    private ActivityAuthRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_register);

        binding = ActivityAuthRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonRegister.setOnClickListener(new View.OnClickListener(){
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