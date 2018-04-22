package com.example.lenovo.healthmax;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class noteViewHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView textTitle, textTime;

    public noteViewHolder(View itemView){
        super(itemView);

        mView = itemView;

        textTitle = mView.findViewById(R.id.note_title);
        textTime = mView.findViewById(R.id.note_time);
    }

    public void setNoteTitle(String title){
        textTitle.setText(title);
    }

    public void setNoteTime(String time){
        textTime.setText(time);
    }
}
