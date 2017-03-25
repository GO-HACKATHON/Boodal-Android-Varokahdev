package com.example.ariel.boodal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ariel.boodal.Object.SaldoObject;
import com.example.ariel.boodal.R;

import java.util.ArrayList;

/**
 * Created by dickyeka on 3/26/17.
 */

public class SaldoAdapter extends RecyclerView.Adapter<SaldoAdapter.ViewHolder> {
    private ArrayList<SaldoObject> data;
    private Context context;

    public SaldoAdapter(ArrayList<SaldoObject> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public SaldoAdapter(ArrayList<SaldoObject> data) {
        this.data = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_topup, parent, false);
        return new SaldoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_jenis.setText(data.get(position).getJenis());
        holder.tv_tgl.setText(data.get(position).getTgl());
        holder.tv_jumlah.setText(data.get(position).getJumlah());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_jenis,tv_tgl,tv_jumlah;

        public ViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            tv_jenis= (TextView) itemView.findViewById(R.id.jenis);
            tv_tgl = (TextView) itemView.findViewById(R.id.tgltopup);
            tv_jumlah = (TextView) itemView.findViewById(R.id.jumlahtopup);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
