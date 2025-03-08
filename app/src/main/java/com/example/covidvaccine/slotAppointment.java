package com.example.covidvaccine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class slotAppointment extends AppCompatActivity {
    BottomNavigationView navigationView;
    private Spinner centerSpinner;
    Intent intent;
    String message;
    String phone,userRole;
    RecyclerView recyclerView;
    databaseHelper myDb;
    RecyclerCenterAdapter adapter;
    ArrayList<String>  city, area, center, address,date,time;
    ArrayList<Integer> centerIds;
    ArrayList<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.slot_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        navigationView = findViewById(R.id.bottom_navigation);
        intent = getIntent();
        message = intent.getStringExtra("isAdmin");
        phone = intent.getStringExtra("phone");



        centerSpinner = findViewById(R.id.center_spinner);
        recyclerView = findViewById(R.id.recyclerViewVaccine);
        myDb = new databaseHelper(slotAppointment.this);
        setupSpinner();

    }
    private void setupSpinner() {
        // Get the list of cities from the database
        cityList = getDataFromDatabase();
        cityList.add(0, "All City");
        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityList);
        centerSpinner.setAdapter(cityAdapter);

        // Set the listener for item selection
        centerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();
                if (!selectedCity.equals("All City")) {
                    displayData(selectedCity);
                } else {
                    displayData(selectedCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private ArrayList<String> getDataFromDatabase() {
        ArrayList<String> itemList = new ArrayList<>();

        // Query to Get Data
        Cursor cursor = myDb.getCity();

        // Extract Data from Cursor
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0); // Get data from first column
                itemList.add(name);
            } while (cursor.moveToNext());
        }

        // Close Cursor and Database
        cursor.close();
        return itemList;
    }

    private void displayData(String ct) {
        if(message.equals("true")){
            userRole = "admin";
        }
        else {
            userRole = "notadmin";
        }

        centerIds = new ArrayList<>();
        city = new ArrayList<>();
        area = new ArrayList<>();
        center = new ArrayList<>();
        address = new ArrayList<>();
        date = new ArrayList<>();
        time = new ArrayList<>();
        adapter = new RecyclerCenterAdapter(this , centerIds, city, area, center, address, date, time,userRole,phone,slotAppointment.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Cursor c;
        if (ct.equals("All City")) {
            c = myDb.getAllCenter();
            clearRecyclerView();
        } else {
            c = myDb.getCenterOFCity(ct);
            clearRecyclerView();

        }
        if(c.getCount() == 0){
            Toast.makeText(this, "No Data Found!!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            while (c.moveToNext()) {
                centerIds.add(Integer.valueOf(c.getString(0)));
                city.add(c.getString(1));
                area.add(c.getString(2));
                center.add(c.getString(3));
                address.add(c.getString(4));
                date.add(c.getString(5));
                time.add(c.getString(6));
            }
        }
        c.close();
        adapter.notifyDataSetChanged();

    }

    public void refreshSpinner() {
        setupSpinner();
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // new code for navigation bar
        NavigationItemSelected();
    }

    private void NavigationItemSelected() {
        //initialize navigation and assign variable
        navigationView.setSelectedItemId(R.id.nav_vaccine);

        //this to know which of navigation bar are selected to go next
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        Intent intent = new Intent(slotAppointment.this ,profile.class );
                        intent.putExtra("phone",phone);
                        intent.putExtra("isAdmin",message);
                        startActivity(intent);

                        return true;
                    case R.id.nav_vaccine:
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Unexpected value: " + item.getItemId(), Toast.LENGTH_LONG);
                }
                return false;
            }
        });
        FloatingActionButton addSlot = findViewById(R.id.fabtn_add_new_slot);
        if(message.equals("true"))
        {
            addSlot.setVisibility(View.VISIBLE);
            addSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(slotAppointment.this, add_new_slot.class);
                    intent.putExtra("phone",phone);
                    intent.putExtra("isAdmin",message);
                    startActivity(intent);
                }
            });
        }
        else {
            addSlot.setVisibility(View.GONE);
        }
    }
    private void clearRecyclerView() {
        // Clear the data in the adapter
        city.clear();
        area.clear();
        center.clear();
        address.clear();
        date.clear();
        time.clear();
        adapter.notifyDataSetChanged();
    }
}