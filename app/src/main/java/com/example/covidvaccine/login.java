package com.example.covidvaccine;

import android.content.DialogInterface;
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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void goRegister(View view) {
        Intent intent = new Intent(login.this, register.class);
        startActivity(intent);

    }
    public void login(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        databaseHelper myDB = new databaseHelper(login.this);
        //get the data from textedit
        EditText phone = findViewById(R.id.et_phone);
        EditText password = findViewById(R.id.et_password);
        String getLoginFromDB = myDB.loginUser(phone.getText().toString(), password.getText().toString());



        if (getLoginFromDB.equals("Valid")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
            builder.setCancelable(true);
            builder.setTitle("Login Successful");
            builder.setMessage("Redirecting to your profile page.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(login.this, profile.class);
                    intent.putExtra("isAdmin", "false");
                    intent.putExtra("phone",phone.getText().toString());
                    startActivity(intent);
                    finish();

                }
            });
            builder.show();
        } else if (getLoginFromDB.equals("InValidPass")) {
            password.setError("invalid password");
            password.requestFocus();

        } else if (getLoginFromDB.equals("InValidPhone")) {
            if (phone.getText().toString().equals("1234567890") && password.getText().toString().equals("admin")) {
                Intent intent = new Intent(login.this, profile.class);
                intent.putExtra("isAdmin", "true");
                startActivity(intent);
                finish();


            } else {
                phone.setError("invalid phone no.");
                phone.requestFocus();
            }
        }
    }

}