package com.cuansaver.app.ui.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cuansaver.app.R;
import com.cuansaver.app.databinding.FragmentHomeBinding;
import com.cuansaver.app.model.Data;
import com.cuansaver.app.ui.ItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ArrayList<Data> datas;
    private ItemAdapter itemAdapter;
    private RecyclerView rv;

    final Calendar myCalendar= Calendar.getInstance();
    private EditText etDate;

    private String uid, selectionDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SetupVariables();
        SetupDatepicker();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(itemAdapter);
        ReadData();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void SetupVariables(){
        etDate = binding.date;
        Intent intent = getActivity().getIntent();
        uid = intent.getStringExtra("uid");
        database = FirebaseDatabase.getInstance("https://cuan-saver-app-default-rtdb.firebaseio.com");
        reference = database.getReference().child("Expenses").child(uid);
        rv = binding.recyclerView;
        datas = new ArrayList<>();
        itemAdapter = new ItemAdapter(getActivity(), datas, uid);
    }
    private void SetupDatepicker(){
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                UpdateLabel();
            }
        };
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void UpdateLabel(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date cal = myCalendar.getTime();
        etDate.setText(dateFormat.format(cal));
        selectionDate = dateFormat.format(cal);
        ReadData();
    }

    private void ReadData(){
        Query query = reference.orderByChild("date").equalTo(selectionDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datas.clear();
                int totalAmount = 0;
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    datas.add(data);
                    totalAmount = data.getAmount();
                    binding.totalAmount.setText("Rp. "+String.format("%,d",totalAmount).replace(",", "."));
                }
                itemAdapter.notifyDataSetChanged();
                totalAmount = 0;
                for (DataSnapshot ds : snapshot.getChildren()){
                    Map< String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount+=pTotal;
                    binding.totalAmount.setText("Rp. "+String.format("%,d",totalAmount).replace(",", "."));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}