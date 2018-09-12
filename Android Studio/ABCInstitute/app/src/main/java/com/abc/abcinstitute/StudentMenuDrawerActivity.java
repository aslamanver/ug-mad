package com.abc.abcinstitute;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class StudentMenuDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView stName, stMobile, stLog, stJava, stPhp, stCpp, stPython, stGolang, navName, navMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        stName = (TextView) findViewById(R.id.stName);
        stMobile = (TextView) findViewById(R.id.stMobile);
        stLog = (TextView) findViewById(R.id.stLog);
        stJava = (TextView) findViewById(R.id.stJava);
        stPhp = (TextView) findViewById(R.id.stPhp);
        stCpp = (TextView) findViewById(R.id.stCpp);
        stPython = (TextView) findViewById(R.id.stPython);
        stGolang = (TextView) findViewById(R.id.stGolang);

        View headerview = navigationView.getHeaderView(0);
        navName = (TextView) headerview.findViewById(R.id.navName);
        navMobile = (TextView) headerview.findViewById(R.id.navMobile);

        AbcDatabase db = new AbcDatabase(this);
        Student student = new Student();
        Cursor curStudent = db.curStudent();

        while (curStudent.moveToNext()) {
            student.setInfo(
                    curStudent.getInt(curStudent.getColumnIndex("id")),
                    curStudent.getString(curStudent.getColumnIndex("name")),
                    curStudent.getString(curStudent.getColumnIndex("mobile")),
                    curStudent.getString(curStudent.getColumnIndex("username")),
                    curStudent.getString(curStudent.getColumnIndex("password")));
            student.setMarks(
                    curStudent.getString(curStudent.getColumnIndex("java")),
                    curStudent.getString(curStudent.getColumnIndex("php")),
                    curStudent.getString(curStudent.getColumnIndex("cpp")),
                    curStudent.getString(curStudent.getColumnIndex("python")),
                    curStudent.getString(curStudent.getColumnIndex("golang"))
            );
        }
        setTitle(student.getId() + " - " + student.getName());

        stName.setText(student.getName());
        stMobile.setText(student.getMobile());
        stLog.setText("Username: "+student.getUsername() + " Password: "+student.getPassword());
        stJava.setText(student.getJava());
        stPhp.setText(student.getPhp());
        stCpp.setText(student.getCpp());
        stPython.setText(student.getPython());
        stGolang.setText(student.getGolang());

        navName.setText(student.getName());
        navMobile.setText(student.getMobile());
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Logout");
            dialog.setMessage("Are you sure ?");
            dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    AbcDatabase db = new AbcDatabase(getApplicationContext());
                    db.recordLogin("none", "none");
                    Intent i = new Intent(StudentMenuDrawerActivity.this, MainActivity.class);
                    startActivity(i);
                    finishAffinity();
                }});
            dialog.setNegativeButton(android.R.string.no, null);
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
