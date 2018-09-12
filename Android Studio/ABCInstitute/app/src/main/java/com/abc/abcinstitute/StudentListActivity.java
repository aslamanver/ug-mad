package com.abc.abcinstitute;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.abc.abcinstitute.R.id.rvStudents;

public class StudentListActivity extends AppCompatActivity {

    private String schTxt;
    private RecyclerView rvStudents;
    private studentsAdapterNew adapterNew;
    private List<Student> listItems;

    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        loadData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = rvStudents.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable("state", listState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(schTxt == null) { loadData(); } else { loadData(schTxt); }
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable("state");
            rvStudents.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.student_search_menu, menu);
        MenuItem item = menu.findItem(R.id.new_student_menu_search_btn);
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

    public void loadData(){

        AbcDatabase db = new AbcDatabase(this);
        rvStudents = (RecyclerView) findViewById(R.id.rvStudents) ;
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        adapterNew = new studentsAdapterNew(db.allStudentsArrayList(), this);
        rvStudents.setAdapter(adapterNew);
    }

    public void loadData(String text){

        AbcDatabase db = new AbcDatabase(this);
        rvStudents = (RecyclerView) findViewById(R.id.rvStudents) ;
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        adapterNew = new studentsAdapterNew(db.allStudentsArrayList(text), this);
        rvStudents.setAdapter(adapterNew);
    }

}
