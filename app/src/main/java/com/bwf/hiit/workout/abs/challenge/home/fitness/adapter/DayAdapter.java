package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.myHolder> {

    private String[] tilte;
    private int[] date;


    public DayAdapter(String[] tilte, int[] date) {
        this.tilte = tilte;
        this.date = date;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, final int position) {

        holder.tvTitle.setText(tilte[position]);
        holder.tvDate.setText(String.valueOf(date[position]));

        if (date[position] == getCurrentDate()) {
            holder.tvDate.setBackgroundResource(R.drawable.bg_item_day);
            holder.tvDate.setTextColor(Color.WHITE);
            holder.tvDate.setHeight(30);
            holder.tvDate.setWidth(30);
            holder.tvTitle.setTextColor(Color.parseColor("#08f514"));
        }
    }

    @Override
    public int getItemCount() {
        if (tilte != null)
            return tilte.length;
        else return 0;
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDate;

        myHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }

    private int getCurrentDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }

}
