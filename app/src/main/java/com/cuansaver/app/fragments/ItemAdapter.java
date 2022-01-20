package com.cuansaver.app.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cuansaver.app.R;
import com.cuansaver.app.model.Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private Context mContext;
    private List<Data> myDataList;
    private String postid;
    private String note;
    private int amount;
    private String item;
    private String category;
    private String uid;
    private String oldDate;
    private String firebase_url = "https://cuansaver-default-rtdb.asia-southeast1.firebasedatabase.app";


    public ItemAdapter(Context mContext, List<Data> myDataList, String uid) {
        this.mContext = mContext;
        this.myDataList = myDataList;
        this.uid = uid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.container_item, parent, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Data data = myDataList.get(position);
        holder.item.setText(data.getItem());
        holder.amount.setText("Rp. "+String.format("%,d",data.getAmount()).replace(",", "."));
        holder.date.setText(data.getDate());
        holder.notes.setText("Note: "+data.getNotes());
        holder.category.setText("Category: "+data.getCategory());

        switch (data.getCategory()){
            case "Transport":
                holder.imageView.setImageResource(R.drawable.ic_item_transport);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.ic_item_food);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_item_entertainment);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.ic_item_other);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.ic_item_error);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postid = data.getId();
                note = data.getNotes();
                amount = data.getAmount();
                item = data.getItem();
                category = data.getCategory();
                oldDate = data.getDate();

                updateData();
            }
        });

    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View myView = inflater.inflate(R.layout.activity_item_update, null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        final TextView mItem = myView.findViewById(R.id.item);
        final EditText mAmount = myView.findViewById(R.id.amount);
        final EditText mNote = myView.findViewById(R.id.note);
        final Spinner mSpinner = myView.findViewById(R.id.spinner);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        mNote.setText(note);
        mNote.setSelection(note.length());

        List<String> Lines = new ArrayList<String>(){{add("Transport"); add("Food"); add("Entertainment"); add("Other");}};
        int pos = category!=null && !category.trim().isEmpty() ? Lines.indexOf(category) : 0;
        mSpinner.setSelection(pos);

        Button updateBtn  = myView.findViewById(R.id.update);
        Button deleteBtn = myView.findViewById(R.id. delete);

        // Update Item
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(mAmount.getText().toString());
                note = mNote.getText().toString();
                category = mSpinner.getSelectedItem().toString();

                Data data = new Data(item, oldDate, postid, note, amount, category);
                DatabaseReference reference = FirebaseDatabase.getInstance(firebase_url).getReference().child("Expenses").child(uid);
                reference.child(postid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "Updated successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "failed " +task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        // Delete Item
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference = FirebaseDatabase.getInstance(firebase_url).getReference().child("Expenses").child(uid);
                reference.child(postid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "Failed to delete " +task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item, amount, date, notes, category;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            notes  = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView);
            category = itemView.findViewById(R.id.category);
        }
    }
}
