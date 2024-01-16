package com.ibrahimbinmansoor.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemViewHolder>  {

    private List<DataItem> dataItems;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int itemId);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    public DataItemAdapter(List<DataItem> dataItems){
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public DataItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new DataItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataItemViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);
        holder.bindData(dataItem);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(itemClickListener != null){
                    itemClickListener.onItemClick((dataItem.getId()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

}
