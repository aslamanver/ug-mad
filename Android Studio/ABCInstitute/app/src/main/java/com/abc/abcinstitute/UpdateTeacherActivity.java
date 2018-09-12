package com.abc.abcinstitute;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class UpdateTeacherActivity extends AppCompatActivity {

    private int thID;
    EditText ntName, ntUsername, ntPassword;

    Button cameraBtn;
    ImageView profilePic;
    String imgPath;
    private static final int cameraCode = 1525;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == cameraCode && resultCode == Activity.RESULT_OK) {
            imgPath = DbBitmapUtility.getRealPathFromURI(this, data.getData());
            profilePic.setImageURI(Uri.fromFile(new File(imgPath)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        ntName = (EditText) findViewById(R.id.ntName);
        ntUsername = (EditText) findViewById(R.id.ntUsername);
        ntPassword = (EditText) findViewById(R.id.ntPassword);


        Intent i = getIntent();
        thID = i.getExtras().getInt("tid");

        AbcDatabase db = new AbcDatabase(this);
        final Teacher teacher = new Teacher();

        Cursor crTeacher = db.selectQuery("select * from teacher where id = " + thID);

        if(crTeacher.getCount() > 0){
            while (crTeacher.moveToNext()) {
                teacher.setInfo(
                        thID,
                        crTeacher.getString(crTeacher.getColumnIndex("name")),
                        crTeacher.getString(crTeacher.getColumnIndex("username")),
                        crTeacher.getString(crTeacher.getColumnIndex("password")));
                teacher.setProfile(crTeacher.getString(crTeacher.getColumnIndex("profile")));
            }
        }

        ntName.setText(teacher.getName());
        ntUsername.setText(teacher.getUsername());
        ntPassword.setText(teacher.getPassword());

        cameraBtn = (Button) findViewById(R.id.cameraBtn);
        profilePic = (ImageView) findViewById(R.id.profilePic);

        setTitle(teacher.getName());
        if(teacher.getProfile() != null)
        profilePic.setImageURI(Uri.fromFile(new File(teacher.getProfile())));

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), cameraCode);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + teacher.getProfile()), "image/*");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.update_teacher_menu, menu);
        return true;
    }

    public void updateData(MenuItem item) {

        boolean canSave = false;
        ArrayList<EditText> fields = new ArrayList<>();
        fields.add(ntName);
        fields.add(ntUsername);
        fields.add(ntPassword);

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
            Teacher teacher = new Teacher();
            teacher.setInfo(
                    thID,
                    ntName.getText().toString(),
                    ntUsername.getText().toString(),
                    ntPassword.getText().toString());
            teacher.setProfile(imgPath);

            AbcDatabase db = new AbcDatabase(this);
            if(db.updateTeacher(teacher)){
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
                AbcDatabase db = new AbcDatabase(UpdateTeacherActivity.this);
                db.deleteUser("teacher", thID);
                finish();
            }});
        dialog.setNegativeButton(android.R.string.no, null);
        dialog.show();

    }
}
