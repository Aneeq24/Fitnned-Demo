package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MainMenuItemHolder> {

    private List<Record> recordList;

    public RecordAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public MainMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new MainMenuItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MainMenuItemHolder holder, final int position) {

        holder.tvTitle.setText("Day " + String.valueOf(recordList.get(position).getDay()));
        holder.tvWeight.setText(String.valueOf(recordList.get(position).getWeight()));

    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class MainMenuItemHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvWeight;

        MainMenuItemHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvWeight = itemView.findViewById(R.id.tv_weight);
        }
    }

}
