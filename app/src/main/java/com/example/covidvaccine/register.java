package com.example.covidvaccine;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class register extends AppCompatActivity {

    EditText fname,lname, email, password,confirmPassword,dob,phone,aadhar;
    private boolean checked;
    RadioButton male,female;
    String gender = "";
    String dateMessage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fname = findViewById(R.id.et_reg_Fname);
        lname = findViewById(R.id.et_reg_Lname);
        password = findViewById(R.id.et_reg_password);
        dob = findViewById(R.id.et_reg_DOB);
        phone = findViewById(R.id.et_reg_phone);
        email = findViewById(R.id.et_reg_email_addresss);
        aadhar = findViewById(R.id.et_reg_aadhar);
        confirmPassword = findViewById(R.id.et_reg_conf_password);

    }
    public void goLogin(View view) {
        startActivity(new Intent(register.this, login.class));
    }

    public void showDatePicker(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setFlag(DatePickerFragment.FLAG_REGISTER);
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
        EditText meditText = findViewById(R.id.et_reg_DOB);
        meditText.setText(dateMessage);
    }

    @SuppressLint("NonConstantResourceId")
    public void onRadioButtonClicked(View view) {
        male = findViewById(R.id.rb_reg_male);
        female = findViewById(R.id.rb_reg_female);
        if(male.isChecked())
        {
            gender = "Male";
            checked = true;
        }
        else if(female.isChecked()) {
            gender = "Female";
            checked = true;
        }
    }

    public boolean validation() {

        boolean validPassed = true;
        if (!fname.getText().toString().matches("^[a-zA-z]+$") || fname.getText().toString().isEmpty()) {
            fname.setError("Please enter valid first name");
            fname.requestFocus();
            validPassed = false;
        }
        if (!lname.getText().toString().matches("^[a-zA-z]+$") || lname.getText().toString().isEmpty()) {
            lname.setError("Please enter valid last names");
            lname.requestFocus();
            validPassed = false;
        }
        if (aadhar.getText().toString().isEmpty() || aadhar.getText().toString().matches("^\\d{13}$") ) {
            aadhar.setError("Please enter valid aadhar no.");
            aadhar.requestFocus();
            validPassed = false;
        }

        if (dateMessage.isEmpty()) {
            dob.setError("Date of birth can't be empty");
            dob.requestFocus();
            validPassed = false;
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("password can not left empty");
            password.requestFocus();
            validPassed = false;
        }
        if (!confirmPassword.getText().toString().matches(password.getText().toString()) || confirmPassword.getText().toString().isEmpty()) {
            confirmPassword.setError("Confirm password can't be empty OR doesn't match with password");
            confirmPassword.requestFocus();
            validPassed = false;
        }
        if (!phone.getText().toString().matches("^\\d{10}$") || phone.getText().toString().isEmpty()) {
            phone.setError("Please enter valid phone no.");
            phone.requestFocus();
            validPassed = false;
        }
        if (email.getText().toString().isEmpty() || !email.getText().toString().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            email.setError("Invalid email");
            email.requestFocus();
            validPassed = false;
        }
        if (!checked) {
            displayToast("Please select the gender", 1);
            validPassed = false;
        }

        return validPassed;
    }
    public void displayToast(String message, int type) {

        if (type == 0) {
            Toast.makeText(getApplicationContext(), message,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), message,
                    Toast.LENGTH_LONG).show();
        }
    }

    public void regi(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        databaseHelper myDB = new databaseHelper(com.example.covidvaccine.register.this);
        if (validation()) // check if the validation passed
        {
            // start adding the user after confirm the user is not exist in the system by checking the email and phone and passport and username;
            if (myDB.insertData(phone.getText().toString(), fname.getText().toString() + " " + lname.getText().toString(), password.getText().toString(), email.getText().toString(), gender, dateMessage, aadhar.getText().toString(),"Pending")) {
                registerAlertDialog();// show message login successfully and send to another login page

            }
            else {
                Toast.makeText(this, "Something went wrong !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void registerAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(com.example.covidvaccine.register.this);
        builder.setCancelable(true);
        builder.setTitle("Registration Successful");
        builder.setMessage("Redirecting to Login interface.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
            finish();
        });
        builder.show();
        //Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
    }
}