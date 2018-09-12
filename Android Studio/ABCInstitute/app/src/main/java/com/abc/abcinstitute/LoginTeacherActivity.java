package com.abc.abcinstitute;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginTeacherActivity extends AppCompatActivity {

    TextView tvStudent;
    EditText edUser, edPassword;
    LinearLayout downLayout;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_teacher);

        tvStudent = (TextView) findViewById(R.id.tvStudent);
        edUser = (EditText) findViewById(R.id.edUser);
        edPassword = (EditText) findViewById(R.id.edPassword);
        downLayout = (LinearLayout) findViewById(R.id.downLayout);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        animation();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edUser.getText().toString().trim().isEmpty()){
                    edUser.setError("Username is wrong");
                }
                else if(edPassword.getText().toString().trim().isEmpty()){
                    edPassword.setError("Password is wrong");
                }
                else {

                    try {

                        AbcDatabase db = new AbcDatabase(getApplicationContext());
                        Teacher teacher = new Teacher();
                        teacher.setLogin(edUser.getText().toString().trim(), edPassword.getText().toString().trim());
                        Cursor res = db.isTeacher(teacher);

                        if(res.getCount() == 0) {
                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                        }
                        else {
                            while(res.moveToNext()) {
                                db.recordLogin(teacher.getUsername(), "teacher");
                                Intent i = new Intent(LoginTeacherActivity.this, TeacherMenuActivity.class);
                                startActivity(i);
                                finishAffinity();
                            }
                        }
                    }
                    catch (SQLException ex) {
                        Log.d("ASLAM_DB_ERROR", ex.getMessage());
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


    }


    public void animation() {

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(tvStudent, "translationY", -800, 0).setDuration(700);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(edUser, "translationX", -1500, 0).setDuration(700);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(edPassword, "translationX", 1500, 0).setDuration(700);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(downLayout, "translationY", 1500, 0).setDuration(1700);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(anim1, anim2, anim3, anim4);
        animSet.start();
    }

    public void goStudentMenu(View v) {
        Intent i = new Intent(LoginTeacherActivity.this, LoginStudentActivity.class);
        startActivity(i);
    }

    public void goHome(View v) {
        Intent i = new Intent(LoginTeacherActivity.this, MainActivity.class);
        startActivity(i);
    }

}
