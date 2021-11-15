package com.example.isiahlibor.passwordkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import java.util.Arrays;

public class userAcc extends AppCompatActivity {

    Intent msg,map,userHome,insert;
    ListView lstview;
    FloatingActionButton add;
    public int valueID[];
    ArrayAdapter adapterAccounts;
    public Integer cntr = 0;

    private DataBaseHelper dbhelper = new DataBaseHelper(userAcc.this, "keeperDatabase", 2);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    startActivity(userHome);
                    finish();
                    return true;
                case R.id.nav_msg:
                    startActivity(msg);
                    finish();
                    return true;
                case R.id.nav_map:
                    map.putExtra("status", "login");
                    startActivity(map);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acc);

        lstview = (ListView)findViewById(R.id.lstview);
        add = (FloatingActionButton)findViewById(R.id.add);

        msg = new Intent(userAcc.this, message.class);
        insert = new Intent(userAcc.this, insertList.class);
        map = new Intent(userAcc.this, MapsActivity.class);
        userHome = new Intent(userAcc.this, userAcc.class);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        reload();
        btnAdd();
        listview();

    }

    private  void listview(){

        final SQLiteDatabase db = dbhelper.getWritableDatabase();

        lstview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    cntr=valueID[position];
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

    }

    private void reload(){

        try {

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Cursor keep = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblkeeper'", null);
            keep.moveToNext();

            if (keep.getCount() == 0) {
                SQLite.FITCreateTable("keeperDatabase", this, "tblkeeper",
                        "keeperid INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "app VARCHAR(90),username VARCHAR(90), password INTEGER");
            }else{
                keep = db.rawQuery("SELECT *  FROM tblkeeper order by keeperid desc", null);
                String value[] = new String[keep.getCount()];
                int valueCurrentId[] = new int [keep.getCount()];

                int ctrl = 0;
                while (keep.moveToNext()) {
                    String strFor = "";
                    Integer strId;
                    strFor += "App : " + keep.getString(keep.getColumnIndex("app"));
                    strFor += System.lineSeparator() + "username : " + keep.getString(keep.getColumnIndex("username"));
                    strFor += System.lineSeparator() + "password : " + keep.getString(keep.getColumnIndex("password"));
                    strId = keep.getInt(keep.getColumnIndex("keeperid"));
                    value[ctrl] = strFor;
                    valueCurrentId[ctrl] = strId;

                    ctrl++;
                }
                valueID = Arrays.copyOf(valueCurrentId,keep.getCount());
                adapterAccounts = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, value);
                try {
                    lstview.setAdapter(adapterAccounts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            Toast.makeText(userAcc.this,""+e,Toast.LENGTH_LONG).show();
        }

    }

    private void btnAdd(){

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(insert);
                finish();
            }
        });

    }

}
