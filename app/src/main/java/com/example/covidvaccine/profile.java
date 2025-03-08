package com.example.covidvaccine;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profile extends AppCompatActivity {
    BottomNavigationView navigationView;
    Button mButton;
    databaseHelper myDB;
    String message,phone;

    ImageView userImage;

    TextView name, user_aadhar, user_dob, user_phone, user_email, user_gender, user_password, v_status;
    String aadhar, dob, email, gender,nm,password,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        message = intent.getStringExtra("isAdmin");
        mButton = findViewById(R.id.btn_managment);
        if (message.equals("true")) {
            mButton.setVisibility(View.VISIBLE);
        } else {
            mButton.setVisibility(View.GONE);
            phone = intent.getStringExtra("phone");
        }
        showUserInfo();
        navigationView = findViewById(R.id.bottom_navigation);

        Cursor c = myDB.getAllCenter();
        if(c.getCount() == 0){
            myDB.insertcnter(); //insert fresh vaccination data
        }

    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // new code for navigation bar
        NavigationItemSelected();
    }
    private void NavigationItemSelected() {
        //initialize navigation and assign variable
        navigationView.setSelectedItemId(R.id.nav_profile);

        //this to know which of navigation bar are selected to go next
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        return true;
                    case R.id.nav_vaccine:
                        Intent intent = new Intent(profile.this, slotAppointment.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("isAdmin",message);
                        startActivity(intent);
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Unexpected value: " + item.getItemId(), Toast.LENGTH_LONG);
                }
                return false;
            }
        });

    }


    public void showUserInfo() {
        try {
            myDB = new databaseHelper(profile.this);
            name = findViewById(R.id.tv_user_name);
            user_aadhar = findViewById(R.id.tv_user_aadhar);
            user_dob = findViewById(R.id.tv_user_dob);
            user_phone = findViewById(R.id.tv_user_phone);
            user_email = findViewById(R.id.tv_user_email);
            user_gender = findViewById(R.id.tv_user_gender);
            user_password = findViewById(R.id.tv_user_password);
            v_status = findViewById(R.id.tv_v_status);

            if (message.equals("false")) {
                Cursor cursor = myDB.getUser(phone);
                if (cursor.moveToFirst()) {
                    nm = cursor.getString(1);
                    password = cursor.getString(2);
                    email = cursor.getString(3);
                    gender = cursor.getString(4);
                    dob = cursor.getString(5);
                    aadhar = cursor.getString(6);
                    if(cursor.getString(7) == null){
                        status = "pending";
                    }
                    else {
                        status = cursor.getString(7);
                    }
                }

                name.setText(nm);
                user_aadhar.setText(aadhar);
                user_dob.setText(dob);
                user_phone.setText(phone);
                user_email.setText(email);
                user_gender.setText(gender);
                user_password.setText(password);
                v_status.setText(status);
                userImage = findViewById(R.id.tv_user_image);

                if (gender.equals("Female")) {
                    userImage.setBackgroundResource(R.drawable.girl);
                }
                if(gender.equals("Male")) {
                    // gender is male
                    userImage.setBackgroundResource(R.drawable.boy);
                }
            } else {
                name.setText("Admin");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void logout(View view) {
        Intent intent = new Intent(profile.this, login.class);
        startActivity(intent);
        finish();
    }
    public void goManagment(View view) {
        startActivity(new Intent(getApplicationContext(), show_user.class));
    }
}