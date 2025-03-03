package com.example.covidvaccine;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class show_user extends AppCompatActivity {

    RecyclerView recyclerView;
    databaseHelper myDb;
    RecyclerUserAdapter adapter;
    ArrayList<String> name, email, phone, aadhar, dob, gender, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.show_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        myDb = new databaseHelper(show_user.this);
        name = new ArrayList<>();
        email = new ArrayList<>();
        phone = new ArrayList<>();
        aadhar = new ArrayList<>();
        dob = new ArrayList<>();
        gender = new ArrayList<>();
        status = new ArrayList<>();
        adapter = new RecyclerUserAdapter(this, name, email, phone, aadhar, dob, gender, status);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayData();
    }
    public void displayData(){
        Cursor c = myDb.getAllUser();
        if(c.getCount() == 0){
            Toast.makeText(this, "No Data Found!!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            while (c.moveToNext()) {
                name.add(c.getString(1));
                email.add(c.getString(3));
                phone.add(c.getString(0));
                aadhar.add(c.getString(6));
                dob.add(c.getString(5));
                gender.add(c.getString(4));
                if (c.getString(7) == null) {
                    status.add("Pending");
                } else {
                    status.add(c.getString(7));
                }
            }
        }
    }
}