package com.example.finalyearproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.util.List;

public class ResultsAdaptor4 extends RecyclerView.Adapter<ResultHolder> {
    Context context;
    List<ParseObject> list;

    public ResultsAdaptor4(Context context, List<ParseObject> list){
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

        // query for the outDoorRun object to get the username, runName, runDistance and runTime to display in the recyclerview
        ParseObject outdoorRunDetails = list.get(position);
        if (outdoorRunDetails.getString("username") != null){
            holder.name.setText(outdoorRunDetails.getString("runName"));
            holder.time.setText(outdoorRunDetails.getString("outdoorRunTime"));
            holder.distance.setText(outdoorRunDetails.getString("outdoorRunDistance"));
        } else {
            holder.name.setText(null);
            holder.time.setText(null);
            holder.distance.setText(null);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
