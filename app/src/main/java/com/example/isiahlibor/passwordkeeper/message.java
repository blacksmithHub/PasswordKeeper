package com.example.isiahlibor.passwordkeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import java.util.Arrays;

public class message extends AppCompatActivity {

    Intent msg,map,userHome,insert;
    ListView lstview;
    public Integer cntr = 0;
    public int valueID[];
    ArrayAdapter adapterAccounts;

    private DataBaseHelper dbhelper = new DataBaseHelper(message.this, "keeperDatabase", 2);

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
        setContentView(R.layout.activity_message);

        lstview = (ListView)findViewById(R.id.lstview);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        msg = new Intent(message.this, message.class);
        insert = new Intent(message.this, insertList.class);
        map = new Intent(message.this, MapsActivity.class);
        userHome = new Intent(message.this, userAcc.class);

        reload();

    }

    private void reload(){
        try {

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            Cursor keep = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblkeeper'", null);
            keep.moveToNext();

            if (keep.getCount() == 0) {
                SQLite.FITCreateTable("keeperDatabase", this, "tblmessage",
                        "msgID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "emergencyNum VARCHAR(90), message VARCHAR(90)");


            }else{
                keep = db.rawQuery("SELECT *  FROM tblmessage order by msgID desc", null);
                String value[] = new String[keep.getCount()];
                int valueCurrentId[] = new int [keep.getCount()];

                int ctrl = 0;
                while (keep.moveToNext()) {
                    String strFor = "";
                    Integer strId;
                    strFor += "Emergency # : " + keep.getString(keep.getColumnIndex("emergencyNum"));
                    strFor += System.lineSeparator() + "Message : " + keep.getString(keep.getColumnIndex("message"));
                    strId = keep.getInt(keep.getColumnIndex("msgID"));
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
            Toast.makeText(message.this,""+e,Toast.LENGTH_LONG).show();
        }
    }


}
