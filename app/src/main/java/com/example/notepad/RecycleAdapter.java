package com.example.notepad;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecycleAdapter extends RecyclerView.ViewHolder{
    TextView title;
    View mView;

    public RecycleAdapter(View view) {
        super(view);
        mView = view;
        title = view.findViewById(R.id.note_title);
    }
}
