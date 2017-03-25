package com.example.ariel.boodal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ariel.boodal.MapsActivity;
import com.example.ariel.boodal.Object.HomeObject;
import com.example.ariel.boodal.R;
import com.example.ariel.boodal.User_Drawer;

import java.util.ArrayList;

/**
 * Created by Ariel on 3/25/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private ArrayList<HomeObject> data;
    private Context context;

    public HomeAdapter(ArrayList<HomeObject> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public HomeAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_home, parent, false);
        return new HomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_home.setText(data.get(position).getService());
        holder.img_home.setImageResource(data.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_home;
        private ImageView img_home;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_home= (TextView) itemView.findViewById(R.id.tv_mapel);
            img_home = (ImageView) itemView.findViewById(R.id.img_mapel);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(),MapsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
