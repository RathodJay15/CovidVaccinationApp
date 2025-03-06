package com.example.covidvaccine;

import static android.view.View.VISIBLE;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//new
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class bookCertificate extends AppCompatActivity {
    Spinner vaccineSpinner;
    Intent intent;
    String phone , centerID;
    databaseHelper myDB;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    Calendar today;
    String name ,dob ,age ,gender ,aadharNo ,vaccineName ,dose ,dateOfDose ,nextDueDate  ,area ,center ,ct ;
    TextView tv_nm , tv_dob , tv_age , tv_gndr ,tv_aadhar , tv_do , tv_cntr , tv_ct ,tv_area;
    Button download,confirm;
    private static final int STORAGE_PERMISSION_CODE = 101;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.book_certificate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        intent = getIntent();
        phone = intent.getStringExtra("phone");
        centerID = intent.getStringExtra("cid");

        ArrayList<String> vaccines = new ArrayList<>();
        vaccines.add("Covishield");
        vaccines.add("Covaxin");
        vaccines.add("Pfizer-BioNTech");

        tv_nm = findViewById(R.id.tv_name);
        tv_dob = findViewById(R.id.tv_dob);
        tv_age = findViewById(R.id.tv_age);
        tv_gndr = findViewById(R.id.tv_gender);
        tv_aadhar = findViewById(R.id.tv_aadharno);
        tv_do = findViewById(R.id.tv_status);
        tv_cntr= findViewById(R.id.tv_center);
        tv_ct = findViewById(R.id.tv_city);
        tv_area = findViewById(R.id.tv_area);

        confirm = findViewById(R.id.btn_Confirm);
        download = findViewById(R.id.btn_download);

        vaccineSpinner = findViewById(R.id.vaccine_spinner);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vaccines);
        vaccineSpinner.setAdapter(cityAdapter);

        vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vaccineName = vaccineSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        myDB = new databaseHelper(bookCertificate.this);
        Cursor cursor1 = myDB.getSelectedCenter(centerID);
        if (cursor1.moveToFirst()) {
            center = cursor1.getString(3);
            area = cursor1.getString(2);
            ct = cursor1.getString(1);
        }

        Cursor cursor2 = myDB.getUser(phone);
        if (cursor2.moveToFirst())
        {
            name = cursor2.getString(1);
            gender = cursor2.getString(4);
            dob = cursor2.getString(5);
            try {
                Date dobD = sdf.parse(cursor2.getString(5));
                Calendar dobCalendar = Calendar.getInstance();
                dobCalendar.setTime(dobD);

                today = Calendar.getInstance();

                int ageInt = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

                // Adjust age if birthday hasn't happened yet this year
                if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                    ageInt--;
                }
                age = Integer.toString(ageInt);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            aadharNo = cursor2.getString(6);
            dose = cursor2.getString(7);

        }


        tv_nm.setText(name);
        tv_dob.setText(dob);
        tv_age.setText(age);
        tv_gndr.setText(gender);
        tv_aadhar.setText(aadharNo);
        tv_do.setText(dose);
        tv_cntr.setText(center);
        tv_ct.setText(ct);
        tv_area.setText(area);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmed();
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editVaccinationCertificate();
            }
        });

    }
    public void confirmed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(bookCertificate.this);
        builder.setCancelable(true);
        builder.setTitle("Confirmed");
        builder.setMessage("Appointment made Successfully!!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.show();
        download.setVisibility(VISIBLE);
        if(dose.equals("Pending")) {
            myDB.updateStatus( "1st Dose" ,phone);
        } else if (dose.equals("1st Dose")) {
            myDB.updateStatus("2nd Dose",phone);
        }
    }
    public void editVaccinationCertificate() {
        try {
            // Load the image from drawable
            Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.vaccination_certificate_template);
            Bitmap mutableBitmap = original.copy(Bitmap.Config.ARGB_8888, true);

            // Create a Canvas to draw on the bitmap
            Canvas canvas = new Canvas(mutableBitmap);

            // Set up paint for text
            Paint paint = new Paint();
            paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
            paint.setTextSize(80);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);

            // Draw text at specific positions
            canvas.drawText(name, 1925, 1595, paint);
            canvas.drawText(dob, 1925, 1770, paint);
            canvas.drawText(age, 1925, 1945, paint);
            canvas.drawText(gender, 1925, 2120, paint);
            canvas.drawText(aadharNo, 1925, 2295, paint);

            canvas.drawText(vaccineName, 1925, 2825, paint);
            canvas.drawText(dose, 1925, 3000, paint);
            dateOfDose = sdf.format(today.getTime());
            canvas.drawText(dateOfDose, 1925, 3175, paint);
            today.add(Calendar.MONTH, 1);
            nextDueDate = sdf.format(today.getTime());
            canvas.drawText(nextDueDate, 1925, 3350, paint);
            String multilineText = center+",\n"+area+",\n"+ct;
            drawMultilineTextManually(canvas, multilineText, 1925, 3525, paint);

            // Save the modified image
            saveImageToDCIM(this, mutableBitmap, "vaccination_certificate.jpg");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveImageToDCIM(Context context, Bitmap bitmap, String fileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Certificates/");

        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream out = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            AlertDialog.Builder builder = new AlertDialog.Builder(bookCertificate.this);
            builder.setCancelable(true);
            builder.setTitle("Success");
            builder.setMessage("Vaccination certificate Downloaded successfully at DCIM/Certificates folder!!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save image!", Toast.LENGTH_SHORT).show();
        }
    }
    public void drawMultilineTextManually(Canvas canvas, String text, float x, float y, Paint paint) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            canvas.drawText(line, x, y, paint);
            y += 85; // Move down for the next line
        }
    }
}