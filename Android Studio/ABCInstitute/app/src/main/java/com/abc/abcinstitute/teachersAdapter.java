package com.abc.abcinstitute;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class teachersAdapter extends RecyclerView.Adapter<teachersAdapter.ViewHolder> {

    private List<Teacher> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mcontext;

    // data is passed into the constructor
    public teachersAdapter(Context context, List<Teacher> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mcontext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_teachers_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String teachers = mData.get(position).getName();
        holder.thRvName.setText(teachers);

        if(mData.get(position).getProfile() != null) {
            File imFile = new File(mData.get(position).getProfile());
            if(imFile.exists()) {
                holder.profilePic.setImageURI(Uri.fromFile(imFile));
            }
        }
        //Picasso.with(mcontext).load(R.drawable.ic_person_outline_black_24dp).into(holder.profilePic);

        holder.profilePic.setOnClickListener(new View.OnClickListener() {

            int pos = position;

            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(mcontext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.teachers_dialog);
                ImageView iv = (ImageView) dialog.findViewById(R.id.imgDialog);
                iv.setImageURI(Uri.fromFile(new File(mData.get(pos).getProfile())));
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                dialog.show();

                Toast.makeText(mcontext, "W", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView thRvName;
        public ImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);
            thRvName = (TextView) itemView.findViewById(R.id.thRvName);
            itemView.setOnClickListener(this);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Teacher getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
