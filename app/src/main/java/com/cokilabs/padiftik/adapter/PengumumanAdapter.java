package com.cokilabs.padiftik.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cokilabs.padiftik.R;
import com.cokilabs.padiftik.model.Pengumuman;

import java.util.ArrayList;
import java.util.List;

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.MyViewHolder> {
    Context context;
    ArrayList<Pengumuman> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView judul, isi, time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            isi = itemView.findViewById(R.id.isi);
            time = itemView.findViewById(R.id.ts);

        }
    }

    public PengumumanAdapter(Context context, ArrayList<Pengumuman> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PengumumanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_pengumuman, viewGroup, false);

        return new PengumumanAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PengumumanAdapter.MyViewHolder my, int i) {

        Pengumuman pengumuman = list.get(i);
        my.isi.setText(pengumuman.getPengumuman());
        my.time.setText(pengumuman.getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
