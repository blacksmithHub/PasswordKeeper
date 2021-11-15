package com.example.isiahlibor.passwordkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

public class insert extends AppCompatActivity {

    String action, valApp, valUser, valPass,id;
    EditText app, username, password, confirm;
    Button btnAdd, btnBack;
    Intent back;

    private DataBaseHelper dbhelper = new DataBaseHelper(insert.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        app = (EditText)findViewById(R.id.txtapp);
        username = (EditText)findViewById(R.id.txtusername);
        password = (EditText)findViewById(R.id.txtpassword);
        confirm = (EditText)findViewById(R.id.txtconfirm);

        btnAdd = (Button) findViewById(R.id.btnaction);
        btnBack = (Button) findViewById(R.id.btnback);

        action = getIntent().getStringExtra("update");
        id = getIntent().getStringExtra("id");
        back = new Intent(insert.this, user.class);

        reload();
        add();
        back();

    }
    private void reload(){
        try {

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Cursor keep = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblkeeper'", null);
            keep.moveToNext();

            if (keep.getCount() == 0) {
                SQLite.FITCreateTable("keeperDatabase", this, "tblkeeper",
                        "keeperid INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "app VARCHAR(90), username VARCHAR(90), password INTEGER");
            }else{
                if(action == "true"){
                    String sqlStr1 = "select * from tblkeeper where keeperid = '"+id+"'";
                    Cursor cursor = db.rawQuery(sqlStr1, null);
                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        valApp = cursor.getString(1);
                        valUser = cursor.getString(2);
                        valPass = cursor.getString(3);
                        app.setText(valApp);
                        username.setText(valUser);
                        password.setText(valPass);
                        btnAdd.setText("Update");
                    }
                }
            }

        } catch (Exception e) {
            Toast.makeText(insert.this,""+e,Toast.LENGTH_LONG).show();
        }
    }
    private void add(){
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(action == "true"){
                    try {

                        String sqlStr = "UPDATE tblkeeper SET app = '" + app.getText().toString()
                                + "', username = '" + username.getText().toString() + "', password = '" + password.getText().toString()
                                + "' where keeperid = '" + id + "'";
                        db.execSQL(sqlStr);
                        Toast.makeText(insert.this,"Update successfully",Toast.LENGTH_LONG).show();
                        action = "false";
                        btnAdd.setText("Add");
                        app.setText("");
                        username.setText("");
                        password.setText("");
                        startActivity(back);
                    } catch (Exception e) {
                        Toast.makeText(insert.this,""+e,Toast.LENGTH_LONG).show();
                    }

                }else{
                    String sqlStr1 = "select * from tblkeeper where app = '"+app.getText().toString()+"' and username = '"+username.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(sqlStr1, null);
                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        Toast.makeText(insert.this,"Account already exist",Toast.LENGTH_LONG).show();
                    }else{
                        try {
                            String sqlStr = "INSERT INTO tblkeeper (app, username, password) VALUES ('"
                                    + app.getText().toString() + "', '" + username.getText().toString() + "', '" + password.getText().toString() + "')";
                            db.execSQL(sqlStr);
                            Toast.makeText(insert.this,"Account added successfully",Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(insert.this,""+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }
    private void back(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(back);
            }
        });
    }
}
