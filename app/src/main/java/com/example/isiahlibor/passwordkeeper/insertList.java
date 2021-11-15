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

public class insertList extends AppCompatActivity {

    EditText app,username,password;
    Button cancel, add;

    Intent back;

    private DataBaseHelper dbhelper = new DataBaseHelper(insertList.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_list);

        app = (EditText)findViewById(R.id.txtApp);
        username = (EditText)findViewById(R.id.txtUser);
        password = (EditText)findViewById(R.id.txtPass);
        add = (Button)findViewById(R.id.btnAdd);
        cancel = (Button)findViewById(R.id.btnCancel);

        back = new Intent(insertList.this, userAcc.class);

        btnAdd();
        btnCancel();
        getApp();
        getUser();
        getPass();
        reload();

    }

    private void  reload(){

        try {

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Cursor keep = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblkeeper'", null);
            keep.moveToNext();

            if (keep.getCount() == 0) {
                SQLite.FITCreateTable("keeperDatabase", this, "tblkeeper",
                        "keeperid INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "app VARCHAR(90), username VARCHAR(90), password INTEGER");
            }else{

            }

        } catch (Exception e) {
            Toast.makeText(insertList.this,""+e,Toast.LENGTH_LONG).show();
        }

    }

    private  void getPass(){

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

    private void getUser(){

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(username.length() != 0) {
                    reload();
                } else {
                    if(username.getText().length() == 0) {
                        username.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getApp(){

        app.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(app.length() != 0) {
                    reload();
                } else {
                    if(app.getText().length() == 0) {
                        app.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void btnCancel() {

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(back);
            }
        });

    }

    private void btnAdd(){

        final SQLiteDatabase db = dbhelper.getWritableDatabase();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.getText().toString() == "" || password.getText().toString() == "" || username.getText().toString() == ""){
                    Toast.makeText(insertList.this,"Account added unsuccessful",Toast.LENGTH_LONG).show();
                }else{
                    String sqlStr1 = "select * from tblkeeper where app = '"+app.getText().toString()+"' and username = '"+username.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(sqlStr1, null);
                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        startActivity(back);
                        finish();
                        Toast.makeText(insertList.this,"Account already exist",Toast.LENGTH_LONG).show();
                    }else{
                        try {
                            String sqlStr = "INSERT INTO tblkeeper (app, username, password) VALUES ('"
                                    + app.getText().toString() + "', '" + username.getText().toString() + "', '" + password.getText().toString() + "')";
                            db.execSQL(sqlStr);
                            startActivity(back);
                            finish();
                            Toast.makeText(insertList.this,"Account added successfully",Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(insertList.this,""+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }

}
