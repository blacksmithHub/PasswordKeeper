package com.example.isiahlibor.passwordkeeper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;

public class messaging extends AppCompatActivity {

    Button back;
    ListView lstview;

    private DataBaseHelper dbhelper = new DataBaseHelper(messaging.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        back = (Button)findViewById(R.id.btnback);
        lstview = (ListView)findViewById(R.id.msg);
//your close friend alisha has been locked. details:#close friend#alisha#coordinates
        Intent sms_intent = getIntent();
        Bundle b = sms_intent.getExtras();
        if (b!=null) {
            SMSClass smsObj = (SMSClass) b.getSerializable("sms_obj");
            String message = smsObj.getMessage().toString();
            String[] separated = message.split("#");
            String role = separated[1];
            String name = separated[2];
            String location = separated[3];
        }

    }
}
