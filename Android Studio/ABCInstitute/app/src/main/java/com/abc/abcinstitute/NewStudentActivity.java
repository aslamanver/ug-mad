package com.abc.abcinstitute;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewStudentActivity extends AppCompatActivity {

    MenuItem new_student_menu_save_btn;
    EditText nsName, nsMobile, nsUsername, nsPassword, nsJava, nsPhp, nsCpp, nsPython, nsGolang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_student);

        new_student_menu_save_btn = (MenuItem) findViewById(R.id.new_student_menu_save_btn);

        nsName = (EditText) findViewById(R.id.nsName);
        nsMobile = (EditText) findViewById(R.id.nsMobile);
        nsUsername = (EditText) findViewById(R.id.nsUsername);
        nsPassword = (EditText) findViewById(R.id.nsPassword);
        nsJava = (EditText) findViewById(R.id.nsJava);
        nsPhp = (EditText) findViewById(R.id.nsPhp);
        nsCpp = (EditText) findViewById(R.id.nsCpp);
        nsPython = (EditText) findViewById(R.id.nsPython);
        nsGolang = (EditText) findViewById(R.id.nsGolang);

        //new_student_menu_save_btn.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.new_student_menu, menu);
        return true;
    }

    public void saveData(MenuItem item) {

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
            if(db.newStudent(student)){

                try {
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(
                            nsMobile.getText().toString(),
                            null,"Dear Student, Your user account has been created please follow the details. " +
                                    "Username: "+ nsUsername.getText().toString() + " Password: "+ nsPassword.getText().toString(), null, null);
                }
                catch(Exception ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                notificationSt(nsUsername.getText().toString(), nsPassword.getText().toString());
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                finish();
            }
        }

    }

    public void notificationSt(String username, String password) {

        int sID = 0;
        AbcDatabase db = new AbcDatabase(getApplicationContext());
        Cursor c = db.selectQuery("select * from student where username = '"+username+"'");
        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                sID = c.getInt(c.getColumnIndex("id"));
            }
        }

        Intent intent = new Intent(this, UpdateStudentActivity.class);
        intent.putExtra("sid", sID);
        PendingIntent pIntent = PendingIntent.getActivity(this, sID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logo)
                // Set Ticker Message
                .setTicker("ticker")
                // Set Title
                .setContentTitle("Student account created")
                // Set Text
                .setContentText("The student account has been created successfully")
                // Add an Action Button below Notification
                //.addAction( , "OK", pIntent)
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
