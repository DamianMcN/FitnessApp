package com.example.finalyearproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultHolder> {
    Context context;
    List<ParseObject> list;

    public ResultsAdapter(Context context, List<ParseObject> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_cell, parent, false);
        return new ResultHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultHolder holder, int position) {

        // query for the beginner object to get the username, workoutName and beginnerWorkout time to display in the recyclerview
        ParseObject beginnerDetails = list.get(position);
        if (beginnerDetails.getString("username") != null){
            holder.name.setText(beginnerDetails.getString("workoutName"));
            holder.time.setText(beginnerDetails.getString("beginnerWorkouts"));
            holder.distance.setText(null);
        } else {
            holder.name.setText(null);
            holder.time.setText(null);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
