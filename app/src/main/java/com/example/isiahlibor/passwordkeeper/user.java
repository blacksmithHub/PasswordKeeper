package com.example.isiahlibor.passwordkeeper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sqlitelib.DataBaseHelper;
import com.sqlitelib.SQLite;

import java.util.Arrays;

public class user extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton add;

    ListView keeper;
    Intent insert;
    public int valueID[];
    ArrayAdapter adapterAccounts;
    public Integer cntr = 0;
    String update;
    TextView username;

    private DataBaseHelper dbhelper = new DataBaseHelper(user.this, "keeperDatabase", 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        keeper = (ListView)findViewById(R.id.lstview);
        add = (FloatingActionButton)findViewById(R.id.fab);
        username = (TextView)findViewById(R.id.username);
        username.setText(getIntent().getStringExtra("name"));
        reload();
        listview();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this, insert.class);
                startActivity(i);
            }
        });

    }

    private void listview(){
        final SQLiteDatabase db = dbhelper.getWritableDatabase();
        keeper.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    cntr=valueID[i];
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(user.this);
                alertDialog.setTitle("WARNING!");
                alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        update = "true";
                        insert.putExtra("update", update);
                        insert.putExtra("id", cntr);
                        startActivity(insert);

                    }
                });
                alertDialog.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String sqlStr = "DELETE from tblkeeper where keeperid = '" + cntr + "'";
                        db.execSQL(sqlStr);
                        reload();
                    }
                }); alertDialog.show();

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
                    keeper.setAdapter(adapterAccounts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            Toast.makeText(user.this,""+e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();
        } else if (id == R.id.nav_add) {
            Intent i = new Intent(user.this, messaging.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
