package com.example.isiahlibor.passwordkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

public class MainActivity extends AppCompatActivity {

    EditText password;
    Button btnLogin, btnSignup;
    Intent next,sign;
    String pass, name;

    private DataBaseHelper dbhelper = new DataBaseHelper(MainActivity.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = (EditText)findViewById(R.id.password);
        btnLogin = (Button)findViewById(R.id.btnlogin);
        btnSignup = (Button)findViewById(R.id.btnsignup);

        next = new Intent(MainActivity.this, user.class);
        sign = new Intent(MainActivity.this, sign.class);

        reload();

        password();
        login();
        signup();
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
            }
            String sqlStr1 = "select password from tblaccount where password = '"+password.getText().toString()+"'";
            Cursor cursor = db.rawQuery(sqlStr1, null);
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                pass = cursor.getString(0);
                btnSignup.setVisibility(View.INVISIBLE);
            }else{
                btnSignup.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            Toast.makeText(MainActivity.this,""+e,Toast.LENGTH_LONG).show();
        }
    }
    private void password(){
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.length() != 0) {
                    reload();
                } else {
                    if(password.getText().length() == 0) {
                        password.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void login(){

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SQLiteDatabase db = dbhelper.getWritableDatabase();
                if(!password.getText().toString().isEmpty() && password.getText().toString().equals(pass)) {
                    try{
                    String sqlStr1 = "select name from tblaccount where password = '" + password.getText().toString() + "'";
                    Cursor cursor = db.rawQuery(sqlStr1, null);
                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        name = cursor.getString(0);
                        next.putExtra("name", name);
                        startActivity(next);
                    }
                }catch(Exception e) {
                    Toast.makeText(MainActivity.this,""+e,Toast.LENGTH_LONG).show();
                }

                }else{
                    Toast.makeText(MainActivity.this,"Incorrect username or password",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void signup(){
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(sign);
            }
        });
    }
}
