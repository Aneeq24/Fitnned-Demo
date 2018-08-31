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

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.myHolder> {

    private List<Record> recordList;

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new myHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {
        holder.tvDay.setText(recordList.get(position).getDay());
        holder.tvTitle.setText("Day " + String.valueOf(recordList.get(position).getExDay()) + " - " + recordList.get(position).getType());
        holder.tvWeight.setText(String.valueOf(recordList.get(position).getKcal()));
        holder.tvDuration.setText(String.valueOf(recordList.get(position).getDuration()));
        holder.tvDate.setText(recordList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        if (recordList != null)
            return recordList.size();
        else return 0;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvWeight;
        TextView tvDay;
        TextView tvDate;
        TextView tvDuration;

        myHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvWeight = itemView.findViewById(R.id.tv_weight);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDuration = itemView.findViewById(R.id.tv_duration);
        }
    }

}
