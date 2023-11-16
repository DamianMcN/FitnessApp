package com.example.finalyearproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultHolder extends RecyclerView.ViewHolder {
    TextView name, time, distance;

    public ResultHolder(@NonNull  View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textViewName);
        time = itemView.findViewById(R.id.textViewName2);
        distance = itemView.findViewById(R.id.textViewName3);

    }
}
