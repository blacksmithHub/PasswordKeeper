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

public class sign extends AppCompatActivity {

    Spinner role;
    EditText name, pass, emerg, confirm;
    Button add, back;
    Intent previous;
    String select;
    private DataBaseHelper dbhelper = new DataBaseHelper(sign.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        role = (Spinner)findViewById(R.id.spinRole);

        name = (EditText) findViewById(R.id.txtname);
        pass = (EditText) findViewById(R.id.txtpassword);
        confirm = (EditText) findViewById(R.id.txtconfirm);
        emerg = (EditText) findViewById(R.id.txtemergency);

        add = (Button) findViewById(R.id.btnsign);
        back = (Button) findViewById(R.id.btnback);

        previous = new Intent(sign.this, MainActivity.class);

        role();
        name();
        pass();
        confirm();
        emerg();
        add();
        back();
        reload();

    }
    private void role(){
        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select = role.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void name(){
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(name.length() != 0) {
                } else {
                    if(name.getText().length() == 0) {
                        name.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void pass(){
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(pass.length() != 0) {
                } else {
                    if(pass.getText().length() == 0) {
                        pass.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void confirm(){
        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(confirm.length() != 0) {
                } else {
                    if(confirm.getText().length() == 0) {
                        confirm.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private  void emerg(){
        emerg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(emerg.length() != 0) {
                } else {
                    if(emerg.getText().length() == 0) {
                        emerg.setError("This field cannot be blank");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void add(){
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString() == "" && pass.getText().toString() == "" && confirm.getText().toString() == "" && emerg.getText().toString() == ""
                        && pass.getText().toString() != confirm.getText().toString()){
                    Toast.makeText(sign.this,"Sign up failed",Toast.LENGTH_LONG).show();
                }else{
                    try {
                        String sqlStr = "INSERT INTO tblaccount (name, password, emergencyNum, role) VALUES ('"
                                + name.getText().toString() + "', '" + pass.getText().toString() + "', '" + emerg.getText().toString() + "', '" + select + "')";
                        db.execSQL(sqlStr);
                        startActivity(previous);
                        Toast.makeText(sign.this,"Account added successfully",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(sign.this,""+e,Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }
    private void back(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(previous);
            }
        });
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

        } catch (Exception e) {
            Toast.makeText(sign.this,""+e,Toast.LENGTH_LONG).show();
        }
    }
}
