package com.example.covidvaccine;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerCenterAdapter extends RecyclerView.Adapter<RecyclerCenterAdapter.ViewHolder>{
    private Context con;
    private String userRole,phone,dose;
    private databaseHelper myDB;
    private ArrayList  city, area, address, date, center, time;
    private ArrayList<Integer> centerIds;
    private slotAppointment slotAppointment;
    public RecyclerCenterAdapter(Context con, ArrayList<Integer> centerIds, ArrayList city, ArrayList area, ArrayList center , ArrayList address, ArrayList date, ArrayList time, String userRole, String phone , slotAppointment slotAppointment) {
        this.con = con;
        this.centerIds = centerIds;
        this.city = city;
        this.area = area;
        this.address = address;
        this.date = date;
        this.center = center;
        this.time = time;
        this.userRole = userRole;
        this.phone = phone;
        this.slotAppointment = slotAppointment;
        myDB = databaseHelper.getInstance(con);
        if(!phone.equals("1234567890"))
        {
            Cursor c = myDB.getUser(phone);
            c.moveToFirst();
            dose = c.getString(7);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(con).inflate(R.layout.show_center_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.city.setText(city.get(position).toString());
        holder.area.setText(area.get(position).toString());
        holder.address.setText(address.get(position).toString());
        holder.date.setText(date.get(position).toString());
        holder.center.setText(center.get(position).toString());
        holder.time.setText(time.get(position).toString());
        holder.cid.setText(centerIds.get(position).toString());

        if(userRole.equals("admin")){
            holder.btn_book.setVisibility(GONE);
            holder.btn_remove.setVisibility(VISIBLE);
        }else {
            holder.btn_book.setVisibility(VISIBLE);
            holder.btn_remove.setVisibility(GONE);
        }
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    int cityId = centerIds.get(position);
                    // Remove from database
                    myDB.deleteCenter(cityId);
                    // Remove from lists
                    city.remove(position);
                    area.remove(position);
                    center.remove(position);
                    address.remove(position);
                    date.remove(position);
                    time.remove(position);
                    centerIds.remove(position);

                    // Notify adapter
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    Toast.makeText(con, "Center Removed!!", Toast.LENGTH_SHORT).show();
                    slotAppointment.refreshSpinner();
                }
            }
        });
        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dose.equals("2nd Dose"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(con);
                    builder.setCancelable(true);
                    builder.setTitle("Fully Vaccinated!!");
                    builder.setMessage("You have already taken 2nd dose!!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
                    builder.show();
                }
                else
                {
                    Intent intent = new Intent(con, bookCertificate.class);
                    intent.putExtra("phone",phone);
                    intent.putExtra("cid", centerIds.get(position).toString());
                    con.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return city.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView city, area, address, date, center,time , cid;
        Button btn_book,btn_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.ct);
            area = itemView.findViewById(R.id.area);
            address = itemView.findViewById(R.id.address);
            date = itemView.findViewById(R.id.date);
            center = itemView.findViewById(R.id.center);
            time = itemView.findViewById(R.id.time);
            cid = itemView.findViewById(R.id.tv_cid);
            btn_book = itemView.findViewById(R.id.btn_book);
            btn_remove = itemView.findViewById(R.id.btn_Remove);


        }
    }
}
