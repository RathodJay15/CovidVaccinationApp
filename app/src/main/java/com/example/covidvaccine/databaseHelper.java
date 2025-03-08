package com.example.covidvaccine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class databaseHelper extends SQLiteOpenHelper {
    private static databaseHelper instance;
    public databaseHelper(@Nullable Context context) {
        super(context, "covidVaccine.db", null, 1);
    }
    public static synchronized databaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new databaseHelper(context.getApplicationContext()); // Use application context
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(phone number primary key, name TEXT, password TEXT, email text, gender text, dob text, aadhar text not null, status text);");
        db.execSQL("CREATE TABLE VaccineCenter (id INTEGER PRIMARY KEY AUTOINCREMENT, city TEXT NOT NULL, area_name TEXT NOT NULL, vaccination_center_name TEXT NOT NULL, center_address TEXT NOT NULL, date text not null,time text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
    }

    public boolean insertData(String phone, String name, String password, String email, String gender, String dob, String aadhar, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("phone",phone);
        cv.put("name",name);
        cv.put("password",password);
        cv.put("email",email);
        cv.put("gender",gender);
        cv.put("dob",dob);
        cv.put("aadhar",aadhar);
        cv.put("status",status);
        long result = db.insert("users",null,cv);
        if (result != -1) return true;
        else return false;
    }

    public boolean insertCenter(String city, String area, String center, String address, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("city",city);
        cv.put("area_name",area);
        cv.put("vaccination_center_name",center);
        cv.put("center_address",address);
        cv.put("date",date);
        cv.put("time",time);
        long result = db.insert("VaccineCenter",null,cv);
        if (result != -1) return true;
        else return false;
    }

    public String loginUser(String phone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor checkUsername = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE phone = '" + phone + "' ", null);
        if (checkUsername.getCount() > 0) { // username exist
            Cursor checkUsernameAndPass = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE phone = '" + phone + "' and password = '" + password + "'", null);
            if (checkUsernameAndPass.getCount() > 0) { // check the password for the exist username
                // the username and password correct.
                sqLiteDatabase.close();
                return "Valid";
            } else {
                //password incorrect
                sqLiteDatabase.close();
                return "InValidPass";
            }
        } else {
            // username is not exist
            sqLiteDatabase.close();
            return "InValidPhone";
        }
    }

    public Cursor getUser(String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(String.format("select * from users where phone = %s", phone),null);
    }

    public Cursor getAllUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from users" ,null);
    }

    public void updateStatus(String val , String phone)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status",val);
        String whereClause = "phone = ?";
        String[] whereArgs = {phone};
        db.update("users", values, whereClause, whereArgs);
    }

    public Cursor getCity()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select distinct city from VaccineCenter" ,null);
    }
    public Cursor getAllCenter()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from VaccineCenter" ,null);
    }
    public Cursor getCenterOFCity(String ct) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from VaccineCenter where city = ?";
        Cursor cursor = db.rawQuery(query, new String[]{ct});
        return cursor;
    }
    public void deleteCenter(int cityId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(cityId)};
        db.delete("VaccineCenter", whereClause, whereArgs);
    }
    public Cursor getSelectedCenter(String cid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from VaccineCenter where id = " + cid ,null);
    }

    public void insertcnter(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM VaccineCenter");
        db.execSQL("INSERT INTO VaccineCenter (city, area_name, vaccination_center_name, center_address, date, time) VALUES ('Rajkot', 'Kalavad Road', 'Apollo Hospitals', 'Near Crystal Mall, Kalavad Road, Rajkot, Gujarat 360005', '12/2/2025', '10am to 4pm'),('Rajkot', 'University Road', 'Wockhardt Hospital', 'Near Hemu Gadhavi Hall, University Road, Rajkot, Gujarat 360005', '11/2/2025', '10am to 4pm'),('Rajkot', 'Mavdi', 'Mavdi Urban Health Center', 'Mavdi Main Road, Near Mavdi Chowk, Rajkot, Gujarat 360004', '13/2/2025', '10am to 4pm'),('Rajkot', 'Raiya Road', 'Raiya Road Urban health Center', 'Near Raiya Circle, Raiya Road, Rajkot, Gujarat 360007', '14/2/2025', '10am to 4pm'),('Rajkot', 'Yagnik Road', 'Yagnik Road Urban Health Center', 'Near Dr. Yagnik Road, Rajkot, ujarat 360001', '15/2/2025', '10am to 4pm'),('Ahmedabad', 'Navrangpura', 'Apollo Hospitals International Limited', 'Plot No. 1A, Bhat GIDC Estate, Gandhinagar-Ahmedabad Road, Ahmedabad, ujarat 382428', '11/2/2025', '10am to 4pm'),('Ahmedabad', 'Satellite', 'Sterling Hospital', 'Sterling Hospital Road, Memnagar, Ahmedabad, Gujarat 380052', '12/2/2025', '10am to 4pm'),('Ahmedabad', 'Vastrapur', 'Zydus Hospital', 'Nr. Sola Bridge, Sarkhej - Gandhinagar Hwy, Ahmedabad, Gujarat 380054', '13/2/2025', '10am to 4pm'),('Ahmedabad', 'Bopal', 'Bopal Urban health Center', 'Near Bopal Lake, Bopal, Ahmedabad, Gujarat 380058', '14/2/2025', '10am to 4pm'),('Ahmedabad', 'Maninagar', 'Maninagar Urban Health Center', 'Near Maninagar Railway Station, Maninagar, Ahmedabad, Gujarat 380008', '15/2/2025', '10am to 4pm'),('Surat', 'Adajan', 'Apollo Clinic', '101-102, SNS Square, Near Prime Shoppers, Adajan-Hazira Road, Surat, Gujarat 95009', '11/2/2025', '10am to 4pm'),('Surat', 'Vesu', 'Sunshine Global Hospital', 'VIP Road, Vesu, Surat, Gujarat 395007', '12/2/2025', '10am to 4pm'),('Surat', 'Pal', 'Pal Primary Health enter', 'Near Pal RTO, Pal, Surat, Gujarat 395009', '13/2/2025', '10am to 4pm'),('Surat', 'Piplod', 'Piplod Urban Health Center', 'Near Piplod Lake, Piplod, Surat, Gujarat 395007', '14/2/2025', '10am to 4pm'),('Surat', 'Athwa', 'Athwa Urban Health Center', 'Near Athwa Gate, Athwa, Surat, Gujarat 395001', '15/2/2025', '10am to 4pm'),('Vadodara', 'Alkapuri', 'Apollo ospitals', 'Near Mujmahuda Circle, Alkapuri, Vadodara, Gujarat 390007', '11/2/2025', '10am to 4pm'),('Vadodara', 'Akota', 'Akota Urban Health Center', 'Near Akota Stadium, Akota, Vadodara, ujarat 390020', '12/2/2025', '10am to 4pm'),('Vadodara', 'Gotri', 'Gotri Urban Health Center', 'Near Gotri Lake, Gotri, Vadodara, Gujarat 390021', '13/2/2025', '10am to 4pm'),('Vadodara', 'Manjalpur', 'Manjalpur Urban Health Center', 'Near Manjalpur Sports Complex, Manjalpur, Vadodara, Gujarat 390011', '14/2/2025', '10am to 4pm'),('Vadodara', 'Karelibaug', 'Karelibaug Urban ealth Center', 'Near Karelibaug Water Tank, Karelibaug, Vadodara, Gujarat 390018', '15/2/2025', '10am to 4pm'),('Gandhinagar', 'Sector 1', 'Civil Hospital', 'Sector 12, Gandhinagar, Gujarat 82012', '11/2/2025', '10am to 4pm'),('Gandhinagar', 'Sector 6', 'Sector 6 Urban Health Center', 'Near Sector 6 Market, Sector 6, Gandhinagar, Gujarat 382006', '12/2/2025', '10am to 4pm'),('Gandhinagar', 'Sector 11', 'Sector 11 Urban Health Center', 'Near Sector 11 Community Hall, Sector 11, Gandhinagar, Gujarat 382011', '13/2/2025', '10am to 4pm'),('Gandhinagar', 'Sector 21', 'Sector 21 Urban Health Center', 'Near Sector 21 Bus Stand, Sector 21, Gandhinagar, Gujarat 382021', '14/2/2025', '10am to 4pm'),('Gandhinagar', 'Sector 24', 'Sector 24 Urban health Center', 'Near Sector 24 Garden, Sector 24, Gandhinagar, Gujarat 382024', '15/2/2025', '10am to 4pm');");
    }
}
