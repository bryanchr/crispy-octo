package com.example.lenovo.healthmax;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.v4.content.ContextCompat.startActivity;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    View mView;

    TextView textTitle, textTime;
    CardView noteCard;
    String id;


    public NoteViewHolder(final View itemView){
        super(itemView);

        mView = itemView;

        textTitle = mView.findViewById(R.id.note_title);
        textTime = mView.findViewById(R.id.note_time);
        noteCard = mView.findViewById(R.id.note_card);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(),"klik",Toast.LENGTH_SHORT).show();

                Intent newIntent = new Intent(itemView.getContext(), formHistory.class);
                newIntent.putExtra("noteId", id);
                startActivity(itemView.getContext(), newIntent, null);
            }
        });
    }

    public void setNoteTitle(String title){
        textTitle.setText(title);
    }

    public void setNoteTime(String time){
        textTime.setText(time);
    }

}
