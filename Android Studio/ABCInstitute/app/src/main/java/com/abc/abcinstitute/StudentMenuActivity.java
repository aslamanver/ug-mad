package com.abc.abcinstitute;

import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

import static com.abc.abcinstitute.R.drawable.*;

public class StudentMenuActivity extends AppCompatActivity {

    TextView stName, stMobile, stLog, stJava, stPhp, stCpp, stPython, stGolang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);

        stName = (TextView) findViewById(R.id.stName);
        stMobile = (TextView) findViewById(R.id.stMobile);
        stLog = (TextView) findViewById(R.id.stLog);
        stJava = (TextView) findViewById(R.id.stJava);
        stPhp = (TextView) findViewById(R.id.stPhp);
        stCpp = (TextView) findViewById(R.id.stCpp);
        stPython = (TextView) findViewById(R.id.stPython);
        stGolang = (TextView) findViewById(R.id.stGolang);


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
        setTitle("Student #" + student.getId() + " - " + student.getName());

        stName.setText(student.getName());
        stMobile.setText(student.getMobile());
        stLog.setText("Username: "+student.getUsername() + " Password: "+student.getPassword());
        stJava.setText(student.getJava());
        stPhp.setText(student.getPhp());
        stCpp.setText(student.getCpp());
        stPython.setText(student.getPython());
        stGolang.setText(student.getGolang());

    }


}
