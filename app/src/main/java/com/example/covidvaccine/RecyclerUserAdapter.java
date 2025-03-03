package com.example.covidvaccine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerUserAdapter extends RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder> {
    private Context con;
    private ArrayList name, email, phone, aadhar, dob, gender, status;

    public RecyclerUserAdapter(Context con, ArrayList name, ArrayList email, ArrayList phone, ArrayList aadhar, ArrayList dob, ArrayList gender, ArrayList status) {
        this.con = con;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.aadhar = aadhar;
        this.dob = dob;
        this.gender = gender;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(con).inflate(R.layout.show_user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(name.get(position).toString());
        holder.email.setText(email.get(position).toString());
        holder.phone.setText(phone.get(position).toString());
        holder.aadhar.setText(aadhar.get(position).toString());
        holder.dob.setText(dob.get(position).toString());
        holder.gender.setText(gender.get(position).toString());
        holder.status.setText(status.get(position).toString());

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone, aadhar, dob, gender, status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nm);
            email = itemView.findViewById(R.id.gml);
            phone = itemView.findViewById(R.id.phno);
            aadhar = itemView.findViewById(R.id.adhno);
            dob = itemView.findViewById(R.id.dob);
            gender = itemView.findViewById(R.id.gndr);
            status = itemView.findViewById(R.id.sts);
        }
    }
}
