package com.cuansaver.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cuansaver.app.databinding.ActivityItemInsertBinding;
import com.cuansaver.app.databinding.ActivityLandingBinding;
import com.cuansaver.app.model.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityInsert extends AppCompatActivity {
    private ActivityItemInsertBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance("https://cuansaver-default-rtdb.asia-southeast1.firebasedatabase.app");
        uid = getIntent().getStringExtra("uid");
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertItem();
            }
        });
    }
    private void InsertItem(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        ref = database.getReference().child("Expenses").child(uid);
        String id = ref.push().getKey();

        String name = binding.name.getText().toString();
        Integer amount = 0;
        try {
            amount = Integer.parseInt(binding.amount.getText().toString());
        }catch (Exception e){
            Toast.makeText(ActivityInsert.this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String notes = binding.note.getText().toString();
        String category = binding.spinner.getSelectedItem().toString();

        if(name == null || name.trim().isEmpty() ){
            Toast.makeText(ActivityInsert.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Data data = new Data(name, date, id, notes, amount, category);
        ref.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ActivityInsert.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ActivityInsert.this, "Failed to add Item", Toast.LENGTH_SHORT).show();
                }

            }
        });
        finish();
    }
}