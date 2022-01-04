package com.cuansaver.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cuansaver.app.auth.ActivityLogin;
import com.cuansaver.app.auth.ActivityRegister;
import com.cuansaver.app.databinding.ActivityLandingBinding;

public class ActivityLanding extends AppCompatActivity {
    private ActivityLandingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        binding.btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show(); intendedAction("login"); }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) { intendedAction("register"); }
        });
    }
    protected void intendedAction(String page){
        Intent intent = null;
        switch (page){
            case "login":
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ActivityLogin.class);
                startActivity(intent);
                break;
            case "register":
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ActivityRegister.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, ActivityMain.class);
                startActivity(intent);
                return;
        }

    }
}