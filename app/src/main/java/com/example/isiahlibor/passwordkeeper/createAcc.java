package com.example.isiahlibor.passwordkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

public class createAcc extends AppCompatActivity {

    EditText fullName, pass, confirm, emrgNum;
    Spinner role;
    Button signin, cancel;
    Intent back;
    String select;

    private DataBaseHelper dbhelper = new DataBaseHelper(createAcc.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        fullName = (EditText)findViewById(R.id.txtName);
        pass = (EditText)findViewById(R.id.txtPass);
        confirm = (EditText)findViewById(R.id.txtConfirm);
        emrgNum = (EditText)findViewById(R.id.txtEmrgNum);
        role = (Spinner) findViewById(R.id.spinrole);
        signin = (Button) findViewById(R.id.btnSign);
        cancel = (Button) findViewById(R.id.btnCancel);

        back = new Intent(createAcc.this, LoginActivity.class);

        reload();
        btnCancel();
        btnSignIn();
        getName();
        getPassword();
        getConfirm();
        getEmergencyNum();
        getRole();

    }

    private void getRole(){

        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select = role.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getEmergencyNum(){

        emrgNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emrgNum.length() != 0) {
                } else {
                    if(emrgNum.getText().length() == 0) {
                        emrgNum.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getConfirm(){

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(confirm.length() != 0) {
                } else {
                    if(confirm.getText().length() == 0) {
                        confirm.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getPassword(){

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pass.length() != 0) {
                } else {
                    if(pass.getText().length() == 0) {
                        pass.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getName(){

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(fullName.length() != 0) {
                } else {
                    if(fullName.getText().length() == 0) {
                        fullName.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void btnSignIn(){

        final SQLiteDatabase db = dbhelper.getWritableDatabase();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullName.getText().toString() == "" && pass.getText().toString() == "" && confirm.getText().toString() == "" && emrgNum.getText().toString() == ""
                        && pass.getText().toString() != confirm.getText().toString()){
                    Toast.makeText(createAcc.this,"Sign up failed",Toast.LENGTH_LONG).show();
                }else{
                    try {
                        String sqlStr = "INSERT INTO tblaccount (name, password, emergencyNum, role) VALUES ('"
                                + fullName.getText().toString() + "', '" + pass.getText().toString() + "', '" + emrgNum.getText().toString() + "', '" + select + "')";
                        db.execSQL(sqlStr);
                        startActivity(back);
                        Toast.makeText(createAcc.this,"Account added successfully",Toast.LENGTH_LONG).show();
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(createAcc.this,""+e,Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    private void btnCancel(){

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(back);
                finish();
            }
        });

    }

    private  void reload(){

        try {

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Cursor keep = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblaccount'", null);
            keep.moveToNext();

            if (keep.getCount() == 0) {
                SQLite.FITCreateTable("keeperDatabase", this, "tblaccount",
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "name VARCHAR(90), password INTEGER, emergencyNum VARCHAR(90), role VARCHAR(90)");
            }

        } catch (Exception e) {
            Toast.makeText(createAcc.this,""+e,Toast.LENGTH_LONG).show();
        }

    }

}
