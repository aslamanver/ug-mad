package com.abc.abcinstitute;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.nio.Buffer;

public class TeacherMenuActivity extends AppCompatActivity {

    Button newStudentBtn, mngStudentBtn, newTeacherBtn, mngTeacherBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);

        newStudentBtn = (Button) findViewById(R.id.newStudentBtn);
        mngStudentBtn = (Button) findViewById(R.id.mngStudentBtn);
        newTeacherBtn = (Button) findViewById(R.id.newTeacherBtn);
        mngTeacherBtn = (Button) findViewById(R.id.mngTeacherBtn);

        AbcDatabase db = new AbcDatabase(this);
        Teacher teacher = new Teacher();
        Cursor curTeacher = db.curTeacher();

        while (curTeacher.moveToNext()) {
            teacher.setInfo(
                    curTeacher.getInt(curTeacher.getColumnIndex("id")),
                    curTeacher.getString(curTeacher.getColumnIndex("name")),
                    curTeacher.getString(curTeacher.getColumnIndex("username")),
                    curTeacher.getString(curTeacher.getColumnIndex("password")));
        }
        setTitle("Teacher " + teacher.getName());

        newStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherMenuActivity.this, NewStudentActivity.class);
                startActivity(i);
            }
        });

        mngStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherMenuActivity.this, StudentListActivity.class);
                startActivity(i);
            }
        });

        newTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherMenuActivity.this, NewTeacherActivity.class);
                startActivity(i);
            }
        });

        mngTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherMenuActivity.this, TeacherListActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    public void logOut(MenuItem item) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure ?");
        dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AbcDatabase db = new AbcDatabase(getApplicationContext());
                db.recordLogin("none", "none");
                Intent i = new Intent(TeacherMenuActivity.this, MainActivity.class);
                startActivity(i);
                finishAffinity();
            }});
        dialog.setNegativeButton(android.R.string.no, null);
        dialog.show();

    }

    public void myAccount(MenuItem item) {

        AbcDatabase db = new AbcDatabase(TeacherMenuActivity.this);
        Cursor ct = db.curTeacher();

        if(ct.getCount() > 0) {
            while (ct.moveToNext()) {

                new AlertDialog.Builder(this)
                        .setTitle("My Account")
                        .setMessage(
                                "\nUser ID : " + ct.getString(0) + "\n\n" +
                                "Name : " + ct.getString(1) + "\n" +
                                "Username : " + ct.getString(2) + "\n" +
                                "Password : " + ct.getString(3)
                        )
                        .setIcon(R.drawable.ic_person_outline_black_24dp)
                        .setPositiveButton(android.R.string.ok, null).show();
            }
        }
    }


}
