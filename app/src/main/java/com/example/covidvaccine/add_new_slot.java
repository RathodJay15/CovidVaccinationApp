package com.example.covidvaccine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class add_new_slot extends AppCompatActivity {

    EditText city, area, center, address, date, time;
    String dateMessage, message,phone;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_new_slot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        intent = getIntent();
        message = intent.getStringExtra("isAdmin");
        phone = intent.getStringExtra("phone");

        city = findViewById(R.id.et_ct);
        area = findViewById(R.id.et_area);
        center = findViewById(R.id.et_center_nm);
        address = findViewById(R.id.et_address);
        time = findViewById(R.id.et_time);
    }
    public void showDatePicker(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setFlag(DatePickerFragment.FLAG_ADD_SLOT);
        newFragment.show(getSupportFragmentManager(),
                getString(R.string.datepicker));
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        dateMessage = (day_string +
                "/" + month_string +
                "/" + year_string);
        EditText meditText = findViewById(R.id.et_date);
        meditText.setText(dateMessage);
    }

    public void addSlot(View view){


        databaseHelper myDB = new databaseHelper(com.example.covidvaccine.add_new_slot.this);

        if (myDB.insertCenter(city.getText().toString(),area.getText().toString(),center.getText().toString(),address.getText().toString(),dateMessage,time.getText().toString())) {
            addSlotAlertDialog();
        }
        else {
            Toast.makeText(this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
        }

    }
    public void addSlotAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.covidvaccine.add_new_slot.this);
        builder.setCancelable(true);
        builder.setTitle("New Center/Slot");
        builder.setMessage("New center/slot is added successfully!!");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(add_new_slot.this, slotAppointment.class);
            intent.putExtra("phone",phone);
            intent.putExtra("isAdmin",message);
            startActivity(intent);
            finish();
        });
        builder.show();
    }
}