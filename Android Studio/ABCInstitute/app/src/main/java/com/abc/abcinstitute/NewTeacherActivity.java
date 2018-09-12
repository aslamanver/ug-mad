package com.abc.abcinstitute;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NewTeacherActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_new_teacher);

        ntName = (EditText) findViewById(R.id.ntName);
        ntUsername = (EditText) findViewById(R.id.ntUsername);
        ntPassword = (EditText) findViewById(R.id.ntPassword);

        cameraBtn = (Button) findViewById(R.id.cameraBtn);
        profilePic = (ImageView) findViewById(R.id.profilePic);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), cameraCode);
                /*Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, cameraCode);*/
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.new_teacher_menu, menu);
        return true;
    }

    public void saveData(MenuItem item) {

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
                    ntName.getText().toString(),
                    ntUsername.getText().toString(),
                    ntPassword.getText().toString()
            );
            teacher.setProfile(imgPath);


            AbcDatabase db = new AbcDatabase(this);
            if(db.newTeacher(teacher)){
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }
}
