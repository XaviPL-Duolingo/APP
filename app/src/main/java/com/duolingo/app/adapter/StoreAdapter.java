package com.duolingo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.model.Item;

import java.util.List;

public class StoreAdapter extends  RecyclerView.Adapter<com.duolingo.app.adapter.StoreAdapter.ViewHolder>{

    private StoreAdapter.OnNoteListener mOnNoteListener;
    private List<Item> mData;
    private LayoutInflater mInflater;
    private Context context;

    public StoreAdapter(List<Item>itemList, Context context, StoreAdapter.OnNoteListener onNoteListener){
        this.mOnNoteListener = onNoteListener;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    public com.duolingo.app.adapter.StoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_store, null);
        return new com.duolingo.app.adapter.StoreAdapter.ViewHolder(view, mOnNoteListener);

    }

    public void setItems(List<Item> items) {
        mData = items;
    }

    public void onBindViewHolder(final com.duolingo.app.adapter.StoreAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position), position);
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    public int getItemCount(){
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvItemName, tvItemDesc, tvItemPrice;
        StoreAdapter.OnNoteListener onNoteListener;

        ViewHolder(View itemView, StoreAdapter.OnNoteListener onNoteListener){
            super(itemView);
            tvItemName = itemView.findViewById(R.id.rTvItemName);
            tvItemDesc = itemView.findViewById(R.id.rTvItemDesc);
            tvItemPrice = itemView.findViewById(R.id.rTvItemPrice);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        void bindData(final Item item, int position){
            //ivPhoto.setImageBitmap(item.getImage());
            tvItemName.setText(item.getNameItem());
            tvItemPrice.setText(Integer.toString(item.getPriceItem()));
            tvItemDesc.setText(item.getDescription());

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }

}
