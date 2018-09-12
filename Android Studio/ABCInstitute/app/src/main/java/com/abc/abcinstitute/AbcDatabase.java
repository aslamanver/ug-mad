package com.abc.abcinstitute;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AbcDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "abc.db";
    private static final int DATABASE_VER = 62;
    private Context context;

    public AbcDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        try {
            database.execSQL("CREATE TABLE student (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobile TEXT, java TEXT, php TEXT, cpp TEXT, python TEXT, golang TEXT, username TEXT UNIQUE, password TEXT);");
            database.execSQL("CREATE TABLE teacher (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, username TEXT UNIQUE, password TEXT, profile TEXT);");
            database.execSQL("CREATE TABLE login (id INTEGER PRIMARY KEY, user TEXT, type TEXT);");

            database.execSQL("INSERT INTO teacher (id, name, username, password) VALUES(1, 'Admin', 'admin', '1234');");
            database.execSQL("INSERT INTO login (id, user, type) VALUES(1, 'none', 'none');");

            database.execSQL("INSERT INTO student (id, name, mobile, username, password) VALUES(1, 'Aslam', '145263', 'aslam', '1234');");
            database.execSQL("INSERT INTO student (id, name, mobile, username, password) VALUES(2, 'Asiq', '0145263', 'asiq', '1234');");
        }
        catch(SQLiteException ex) {
            Log.d("ASLAM_DB_ERROR", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS student;");
            db.execSQL("DROP TABLE IF EXISTS teacher;");
            db.execSQL("DROP TABLE IF EXISTS login;");
            onCreate(db);
        }
        catch(SQLiteException ex) {
            Log.d("ASLAM_DB_ERROR", ex.getMessage());
        }
    }

    public boolean newStudent(Student student) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", student.getName());
        contentValues.put("mobile", student.getMobile());
        contentValues.put("username", student.getUsername());
        contentValues.put("password", student.getPassword());

        contentValues.put("java", student.getJava());
        contentValues.put("php", student.getPhp());
        contentValues.put("cpp", student.getCpp());
        contentValues.put("python", student.getPython());
        contentValues.put("golang", student.getGolang());

        long result = db.insert("student", null, contentValues);

        return ((result == -1) ? false : true);
    }

    public boolean newTeacher(Teacher teacher) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        //contentValues.put("id", teacher.getId());
        contentValues.put("name", teacher.getName());
        contentValues.put("username", teacher.getUsername());
        contentValues.put("password", teacher.getPassword());
        contentValues.put("profile", teacher.getProfile());

        long result = db.insert("teacher", null, contentValues);
        return ((result == -1) ? false : true);
    }

    public boolean updateStudent(Student student) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", student.getName());
        contentValues.put("mobile", student.getMobile());
        contentValues.put("username", student.getUsername());
        contentValues.put("password", student.getPassword());
        contentValues.put("java", student.getJava());
        contentValues.put("php", student.getPhp());
        contentValues.put("cpp", student.getCpp());
        contentValues.put("python", student.getPython());
        contentValues.put("golang", student.getGolang());

        long result = -1;
        try {
            result = db.update("student", contentValues, "id=" + student.getId(), null);
        }
        catch (SQLiteException e) {
            Log.d("ASLAM_DB_ERROR", e.getMessage());
        }

        return ((result == -1) ? false : true);
    }

    public boolean updateTeacher(Teacher teacher) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", teacher.getName());
        contentValues.put("username", teacher.getUsername());
        contentValues.put("password", teacher.getPassword());
        contentValues.put("profile", teacher.getProfile());

        long result = -1;
        try {
            result = db.update("teacher", contentValues, "id=" + teacher.getId(), null);
        }
        catch (SQLiteException e) {
            Log.d("ASLAM_DB_ERROR", e.getMessage());
        }

        return ((result == -1) ? false : true);
    }

    public boolean recordLogin(String user, String type) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("user", user);
        contentValues.put("type", type);

        long result = db.update("login", contentValues, "id=1", null);
        return ((result == -1) ? false : true);
    }

    public boolean deleteUser(String type, int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(type,"id="+id, null);
        return ((result > 0) ? false : true);
    }


    public Cursor selectQuery(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor isTeacher(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from teacher where username = '"+teacher.getUsername()+"' and password = '"+teacher.getPassword()+"'", null);
        return c;
    }

    public Cursor isStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from student where username = '"+student.getUsername()+"' and password = '"+student.getPassword()+"'", null);
        return c;
    }

    public Cursor isLogged() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from login where id = 1", null);
        return c;
    }

    public Cursor curTeacher() {

        SQLiteDatabase db = this.getWritableDatabase();
        String user = "";
        Cursor c = this.isLogged();

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                user = c.getString(c.getColumnIndex("user"));
            }
        }

        Cursor cs = db.rawQuery("select * from teacher where username = '"+ user +"'", null);
        return cs;
    }

    public Cursor curStudent() {

        SQLiteDatabase db = this.getWritableDatabase();
        String user = "";
        Cursor c = this.isLogged();

        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                user = c.getString(c.getColumnIndex("user"));
            }
        }

        Cursor cs = db.rawQuery("select * from student where username = '"+ user +"'", null);
        return cs;
    }

    public List<Student> allStudentsArrayList(){
        ArrayList<Student> listItems = new ArrayList<>();
        Cursor stCursor = this.selectQuery("select * from student");
        if(stCursor.getCount() > 0) {
            while (stCursor.moveToNext()) {
                Student st = new Student();
                st.setInfo(
                        stCursor.getInt(stCursor.getColumnIndex("id")),
                        stCursor.getString(stCursor.getColumnIndex("name")),
                        stCursor.getString(stCursor.getColumnIndex("mobile")),
                        stCursor.getString(stCursor.getColumnIndex("username")),
                        stCursor.getString(stCursor.getColumnIndex("password")));

                listItems.add(st);
            }
        }
        return listItems;
    }

    public List<Student> allStudentsArrayList(String text){
        ArrayList<Student> listItems = new ArrayList<>();
        Cursor stCursor = this.selectQuery("select * from student where name like '%"+text+"%'");
        if(stCursor.getCount() > 0) {
            while (stCursor.moveToNext()) {
                Student st = new Student();
                st.setInfo(
                        stCursor.getInt(stCursor.getColumnIndex("id")),
                        stCursor.getString(stCursor.getColumnIndex("name")),
                        stCursor.getString(stCursor.getColumnIndex("mobile")),
                        stCursor.getString(stCursor.getColumnIndex("username")),
                        stCursor.getString(stCursor.getColumnIndex("password")));

                listItems.add(st);
            }
        }
        return listItems;
    }

}
