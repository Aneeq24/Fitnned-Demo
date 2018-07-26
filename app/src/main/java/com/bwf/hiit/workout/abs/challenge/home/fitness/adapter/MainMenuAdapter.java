package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AnalyticsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.view.ScrollingActivity;


public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.MainMenuItemHolder>
{
    String[] tilte;
    Bitmap[] images;
    String[] explanation;

    public MainMenuAdapter(String[] tilte, Bitmap[] images , String[] explanation)
    {
        this.tilte = tilte;
        this.images = images;
        this.explanation = explanation;
    }

    @NonNull
    @Override
    public MainMenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =  layoutInflater.inflate(R.layout.menu_item_layout, parent , false);
        return new MainMenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainMenuItemHolder holder, final int position)
    {

       String t = tilte[position];
       String d = explanation[position];
       Bitmap img = images[position];
       holder.ti.setText(t);
    //   holder.e.setText(d);
       holder.pic.setImageBitmap(img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });

        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AppStateManager.mainCategory = position +1;
//
                Intent i = new Intent(view.getContext() ,ScrollingActivity.class);
                i.putExtra(view.getContext().getString(R.string.plan) ,(position+1));
                view.getContext().startActivity(i);
                AnalyticsManager.getInstance().sendAnalytics("Plan", "Selected :" + (position+1));
            }
        });

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

             //   AppStateManager.mainCategory = position +1;

                Intent i = new Intent(view.getContext() ,ScrollingActivity.class);
                int tempValue = position+1;
                i.putExtra(view.getContext().getString(R.string.plan) ,tempValue);
                AnalyticsManager.getInstance().sendAnalytics("Plan", "Selected :" + (position+1));
                view.getContext().startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return tilte.length;
    }

    public  class  MainMenuItemHolder extends RecyclerView.ViewHolder {

        ImageView pic;
        TextView ti;
        TextView e;
        private View v;
        private ImageView start;



        public MainMenuItemHolder(View itemView)
        {
            super(itemView);
            pic = itemView.findViewById(R.id.taskImageId);
            ti = itemView.findViewById(R.id.tileId);
         //   e = itemView.findViewById(R.id.);
            start= itemView.findViewById(R.id.start);

        }

    }

}
