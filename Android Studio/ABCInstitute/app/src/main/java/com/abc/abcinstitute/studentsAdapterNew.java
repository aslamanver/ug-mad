package com.abc.abcinstitute;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class studentsAdapterNew extends RecyclerView.Adapter<studentsAdapterNew.ViewHolder> {

    private List<Student> students;
    private Context mContext;

    public studentsAdapterNew(List<Student> students, Context mContext) {
        this.students = students;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_students_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Student student = students.get(position);
        holder.stRvName.setText(student.getName());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView stRvName;

        public ViewHolder(View itemView) {
            super(itemView);
            stRvName = (TextView) itemView.findViewById(R.id.stRvName);


            stRvName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("Delete");
                    dialog.setMessage("Are you sure ?");
                    dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            AbcDatabase db = new AbcDatabase(mContext);
                            db.deleteUser("student", students.get(getAdapterPosition()).getId());
                            students.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), students.size());
                        }});
                    dialog.setNegativeButton(android.R.string.no, null);
                    dialog.show();
                    return false;
                }
            });

            stRvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, UpdateStudentActivity.class);
                    i.putExtra("sid", students.get(getAdapterPosition()).getId());
                    mContext.startActivity(i);
                }
            });

        }
    }
}
