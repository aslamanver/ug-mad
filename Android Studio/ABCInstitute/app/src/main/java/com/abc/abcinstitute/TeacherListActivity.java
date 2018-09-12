package com.abc.abcinstitute;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.abc.abcinstitute.R.id.rvStudents;

public class TeacherListActivity extends AppCompatActivity implements teachersAdapter.ItemClickListener {

    private String schTxt;
    teachersAdapter adapter;
    ImageView profilePic;

    private RecyclerView rvTeachers;
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        rvTeachers = (RecyclerView) findViewById(R.id.rvTeachers) ;
        profilePic = (ImageView) findViewById(R.id.profilePic);
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = rvTeachers.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable("state", listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(schTxt == null) {
            loadData();
        }
        else {
            loadData(schTxt);
        }

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable("state");
            rvTeachers.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.teacher_search_menu, menu);
        MenuItem item = menu.findItem(R.id.new_teacher_menu_search_btn);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                schTxt = newText;
                loadData(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent i = new Intent(TeacherListActivity.this, UpdateTeacherActivity.class);
        i.putExtra("tid", adapter.getItem(position).getId());
        startActivity(i);

        //Toast.makeText(this, "You clicked " + adapter.getItem(position).getUsername() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }


    public void loadData() {

        // data to populate the RecyclerView with
        ArrayList<Teacher> teacherNames = new ArrayList<>();

        AbcDatabase db = new AbcDatabase(this);
        Cursor stCursor = db.selectQuery("select * from teacher");
        if(stCursor.getCount() > 0) {
            while (stCursor.moveToNext()) {
                Teacher th = new Teacher();
                th.setInfo(
                        stCursor.getInt(stCursor.getColumnIndex("id")),
                        stCursor.getString(stCursor.getColumnIndex("name")),
                        stCursor.getString(stCursor.getColumnIndex("username")),
                        stCursor.getString(stCursor.getColumnIndex("password")));
                th.setProfile(stCursor.getString(stCursor.getColumnIndex("profile")));

                teacherNames.add(th);
            }
        }

        // set up the RecyclerView
        RecyclerView rvTeachers = (RecyclerView) findViewById(R.id.rvTeachers);
        rvTeachers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new teachersAdapter(this, teacherNames);
        adapter.setClickListener((teachersAdapter.ItemClickListener) this);
        rvTeachers.setAdapter(adapter);
    }

    public void loadData(String newText) {
        ArrayList<Teacher> teacherNamesSch = new ArrayList<>();
        AbcDatabase db = new AbcDatabase(TeacherListActivity.this);
        Cursor stCursor = db.selectQuery("select * from teacher where name like '%"+newText+"%'");

        if(newText.isEmpty()) {
            stCursor = db.selectQuery("select * from teacher");
        }

        if(stCursor.getCount() > 0) {
            while (stCursor.moveToNext()) {
                Teacher th = new Teacher();
                th.setInfo(
                        stCursor.getInt(stCursor.getColumnIndex("id")),
                        stCursor.getString(stCursor.getColumnIndex("name")),
                        stCursor.getString(stCursor.getColumnIndex("username")),
                        stCursor.getString(stCursor.getColumnIndex("password")));
                th.setProfile(stCursor.getString(stCursor.getColumnIndex("profile")));

                teacherNamesSch.add(th);
            }
        }
        // set up the RecyclerView
        RecyclerView rvTeachers = (RecyclerView) findViewById(R.id.rvTeachers);
        rvTeachers.setLayoutManager(new LinearLayoutManager(TeacherListActivity.this));
        adapter = new teachersAdapter(TeacherListActivity.this, teacherNamesSch);
        adapter.setClickListener((teachersAdapter.ItemClickListener) TeacherListActivity.this);
        rvTeachers.setAdapter(adapter);
    }
}
