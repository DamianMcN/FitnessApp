package com.example.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.util.List;

public class ResultsAdaptor2 extends RecyclerView.Adapter<ResultHolder> {

    Context context;
    List<ParseObject> list;

    public ResultsAdaptor2(Context context, List<ParseObject> list){
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

        // query for the intermediate object to get the username, workoutName and intermediateWorkout time to display in the recyclerview
        ParseObject interDetails = list.get(position);
        if (interDetails.getString("username") != null){
            holder.name.setText(interDetails.getString("workoutName"));
            holder.time.setText(interDetails.getString("intermediateWorkouts"));
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
