package com.example.isiahlibor.passwordkeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

public class LoginActivity extends AppCompatActivity {

    Button signin, login;
    EditText password;
    TextView textView;

    String pass, name;
    String securitykey = "VC98C";

    double latti,longi;

    int tries = 0;
    String emergencyno;

    Intent createAcc, userAcc, map;

        LocationManager locationManager;

    private DataBaseHelper dbhelper = new DataBaseHelper(LoginActivity.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            signin = (Button) findViewById(R.id.btnsignin);
            login = (Button) findViewById(R.id.btnlogin);
            textView = (TextView) findViewById(R.id.textView);

            password = (EditText) findViewById(R.id.txtPassword);

            createAcc = new Intent(LoginActivity.this, createAcc.class);
            userAcc = new Intent(LoginActivity.this, userAcc.class);
            map = new Intent(LoginActivity.this, blank.class);

            try{

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //check the network provider is enable
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        //get latitude
                        latti = location.getLatitude();
                        //get longitude
                        longi = location.getLongitude();
                        //instantiate the class, LatLng
                        //LatLng latlng = new LatLng(latitude,longtitude);


                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        //get latitude
                        latti = location.getLatitude();
                        //get longitude
                        longi = location.getLongitude();
                        //instantiate the class, LatLng
                        //LatLng latlng = new LatLng(latitude,longtitude);

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            }

        }catch(Exception e){
            Toast.makeText(LoginActivity.this,""+e,Toast.LENGTH_LONG).show();
        }

        reload();
        btnLogin();
        btnSignup();
        getPassword();
        emergency();

    }

    private void emergency(){

    }

    private void reload(){

        try {

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Cursor keep = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblaccount'", null);
            keep.moveToNext();

            if (keep.getCount() == 0) {
                SQLite.FITCreateTable("keeperDatabase", this, "tblaccount",
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "name VARCHAR(90), password INTEGER, emergencyNum VARCHAR(90), role VARCHAR(90)");

                SQLite.FITCreateTable("keeperDatabase", this, "tblmessage",
                        "msgID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "emergencyNum VARCHAR(90), message VARCHAR(90)");

            }
            String sqlStr1 = "select * from tblaccount";
            Cursor cursor = db.rawQuery(sqlStr1, null);
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                pass = cursor.getString(2);
                name = cursor.getString(1);
                emergencyno = cursor.getString(3);
                signin.setVisibility(View.INVISIBLE);
            }else{
                signin.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this,""+e,Toast.LENGTH_LONG).show();
        }

    }

    private void btnLogin(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!password.getText().toString().equals(pass)) {
                    Toast.makeText(LoginActivity.this,"Incorrect username or password",Toast.LENGTH_LONG).show();
                    tries++;
                    if(tries >= 1){

                        String phoneNo = emergencyno;
                        String msg = latti + "#" + longi+ "#" + securitykey;
                        try {
                            SmsManager smsMngr = SmsManager.getDefault();
                            smsMngr.sendTextMessage(phoneNo, null, msg, null, null);
                            textView.setText(latti + " - " + longi);
                            Toast.makeText(getApplicationContext(), "Intruder alert! Location sent", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }

                }else{
                    startActivity(userAcc);
                    finish();
                }

            }
        });

        Intent sms_intent = getIntent();
        Bundle b = sms_intent.getExtras();

        try{
            if (b!=null) {
                SMSClass smsObj = (SMSClass)b.getSerializable("sms_obj");
                final SQLiteDatabase dbAdd = dbhelper.getWritableDatabase();
                String message = smsObj.getMessage().toString();
                String[] separated = message.split("#");
                if(separated[2] == "VC98C"){
                    String sqlStr = "INSERT INTO tblmessage (emergencyNum, message) VALUES ('" + smsObj.getNumber().toString() + "','"  + message + "')";
                    dbAdd.execSQL(sqlStr);
                    map.putExtra("status", "logout");
                    map.putExtra("latitude", separated[0]);
                    map.putExtra("longitude", separated[1]);
                    map.putExtra("latti", latti);
                    map.putExtra("longi", longi);
                    startActivity(map);
                    finish();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void btnSignup(){

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(createAcc);
                finish();

            }
        });

    }

    private void getPassword(){

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(password.length() != 0) {
                    reload();
                } else {
                    if(password.getText().length() == 0) {
                        password.setError("This field cannot be blank");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
