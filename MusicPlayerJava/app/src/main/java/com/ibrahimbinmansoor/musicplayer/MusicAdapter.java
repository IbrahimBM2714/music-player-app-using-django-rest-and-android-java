package com.ibrahimbinmansoor.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {


    private List<MusicItem> musicItems;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MusicAdapter(List<MusicItem> musicItems, OnItemClickListener onItemClickListener) {
        this.musicItems = musicItems;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, int position) {
        MusicItem musicItem = musicItems.get(position);
        holder.textViewName.setText(musicItem.getName());

    }

    @Override
    public int getItemCount() {
        return musicItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewName;
        private OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }

        }
    }
}
