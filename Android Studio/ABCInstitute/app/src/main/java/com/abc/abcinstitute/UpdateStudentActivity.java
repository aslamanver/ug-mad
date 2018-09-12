package com.abc.abcinstitute;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateStudentActivity extends AppCompatActivity {

    private int stID;
    EditText nsName, nsMobile, nsUsername, nsPassword, nsJava, nsPhp, nsCpp, nsPython, nsGolang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        nsName = (EditText) findViewById(R.id.nsName);
        nsMobile = (EditText) findViewById(R.id.nsMobile);
        nsUsername = (EditText) findViewById(R.id.nsUsername);
        nsPassword = (EditText) findViewById(R.id.nsPassword);
        nsJava = (EditText) findViewById(R.id.nsJava);
        nsPhp = (EditText) findViewById(R.id.nsPhp);
        nsCpp = (EditText) findViewById(R.id.nsCpp);
        nsPython = (EditText) findViewById(R.id.nsPython);
        nsGolang = (EditText) findViewById(R.id.nsGolang);


        Intent i = getIntent();
        stID = i.getExtras().getInt("sid");

        AbcDatabase db = new AbcDatabase(this);
        Student student = new Student();

        Cursor crStudent = db.selectQuery("select * from student where id = " + stID);

        if(crStudent.getCount() > 0){
            while (crStudent.moveToNext()) {
                student.setInfo(
                        stID,
                        crStudent.getString(crStudent.getColumnIndex("name")),
                        crStudent.getString(crStudent.getColumnIndex("mobile")),
                        crStudent.getString(crStudent.getColumnIndex("username")),
                        crStudent.getString(crStudent.getColumnIndex("password")));
                student.setMarks(
                        crStudent.getString(crStudent.getColumnIndex("java")),
                        crStudent.getString(crStudent.getColumnIndex("php")),
                        crStudent.getString(crStudent.getColumnIndex("cpp")),
                        crStudent.getString(crStudent.getColumnIndex("python")),
                        crStudent.getString(crStudent.getColumnIndex("golang")));
            }
        }

        nsName.setText(student.getName());
        nsMobile.setText(student.getMobile());
        nsUsername.setText(student.getUsername());
        nsPassword.setText(student.getPassword());
        nsJava.setText(student.getJava());
        nsPhp.setText(student.getPhp());
        nsCpp.setText(student.getCpp());
        nsPython.setText(student.getPython());
        nsGolang.setText(student.getGolang());

        setTitle(student.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.update_student_menu, menu);
        return true;
    }

    public void updateData(MenuItem item) {

        boolean canSave = false;
        ArrayList<EditText> fields = new ArrayList<>();
        fields.add(nsName);
        fields.add(nsMobile);
        fields.add(nsUsername);
        fields.add(nsPassword);
        fields.add(nsJava);
        fields.add(nsPhp);
        fields.add(nsCpp);
        fields.add(nsPython);
        fields.add(nsGolang);

        for(int i = 0; i < fields.size(); i++) {

            if(fields.get(i).getText().toString().isEmpty()) {

                fields.get(i).setError("This is required");
                canSave = false;
                break;
            }
            else {
                canSave = true;
            }
        }

        if(canSave) {
            Student student = new Student();
            student.setInfo(
                    stID,
                    nsName.getText().toString(),
                    nsMobile.getText().toString(),
                    nsUsername.getText().toString(),
                    nsPassword.getText().toString());

            student.setMarks(
                    nsJava.getText().toString(),
                    nsPhp.getText().toString(),
                    nsCpp.getText().toString(),
                    nsPython.getText().toString(),
                    nsGolang.getText().toString());

            AbcDatabase db = new AbcDatabase(this);
            if(db.updateStudent(student)){
                finish();
            }
        }
    }

    public void deleteData(MenuItem item) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete");
        dialog.setMessage("Do you really want to delete ?");
        dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AbcDatabase db = new AbcDatabase(UpdateStudentActivity.this);
                db.deleteUser("student", stID);
                finish();
            }});
        dialog.setNegativeButton(android.R.string.no, null);
        dialog.show();

    }
}
