package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MainMenuItemHolder> {

    private String[] tilte;
    private int[] date;


    public DayAdapter(String[] tilte, int[] date) {
        this.tilte = tilte;
        this.date = date;
    }

    @NonNull
    @Override
    public MainMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new MainMenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuItemHolder holder, final int position) {

        holder.tvTitle.setText(tilte[position]);
        holder.tvDate.setText(String.valueOf(date[position]));

        holder.itemView.setOnClickListener(view -> {

        });

    }

    @Override
    public int getItemCount() {
        return tilte.length;
    }

    class MainMenuItemHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDate;


        MainMenuItemHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }

}
