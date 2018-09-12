package com.abc.abcinstitute;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // public vars
    Button btnStLogin, btnThLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // construct method by default
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Logging in...");
        pd.setTitle("Please wait");
        pd.setCancelable(false);
        pd.show();

        AbcDatabase db = new AbcDatabase(this);

        try {
            Cursor res = db.isLogged();
            if(res.getCount() > 0) {
                while(res.moveToNext()) {
                    if(!res.getString(res.getColumnIndex("user")).equals("none")) {

                        if(res.getString(res.getColumnIndex("type")).equals("teacher")) {
                            pd.dismiss();
                            //Toast.makeText(getApplicationContext(), "Welcome " + res.getString(res.getColumnIndex("user")), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, TeacherMenuActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else if(res.getString(res.getColumnIndex("type")).equals("student")) {
                            pd.dismiss();
                            //Toast.makeText(getApplicationContext(), "Welcome " + res.getString(res.getColumnIndex("user")), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, StudentMenuDrawerActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }
        }
        catch (SQLException ex) {
            Log.d("ASLAM_DB_ERROR", ex.getMessage());
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        // vars declare
        btnStLogin = (Button)findViewById(R.id.btnStLogin);
        btnThLogin = (Button)findViewById(R.id.btnThLogin);


        pd.dismiss();

        // student button function
        btnStLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, LoginStudentActivity.class);
                //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.btnStLogin), "secondTr");
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

            }
        });

        // teacher button function
        btnThLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, LoginTeacherActivity.class);
                //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, findViewById(R.id.btnThLogin), "firstTr");
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

            }
        });
    }

    public void Notification() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", "Welcome");
        intent.putExtra("text", "I'm Aslam");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logo)
                // Set Ticker Message
                .setTicker("ticker")
                // Set Title
                .setContentTitle("ABC")
                // Set Text
                .setContentText("content")
                // Add an Action Button below Notification
                .addAction(R.drawable.logo, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

}
