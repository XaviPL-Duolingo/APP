package com.duolingo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;

import java.util.ArrayList;

public class WordMatchAdapter extends RecyclerView.Adapter<com.duolingo.app.adapter.WordMatchAdapter.ViewHolder>{

    private WordMatchAdapter.OnNoteListener mOnNoteListener;
    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private Context context;

    public WordMatchAdapter(ArrayList<String> itemList, Context context, WordMatchAdapter.OnNoteListener onNoteListener){
        this.mOnNoteListener = onNoteListener;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    public WordMatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_word_match, null);
        return new WordMatchAdapter.ViewHolder(view, mOnNoteListener);

    }

    public void setItems(ArrayList<String> items) {
        mData = items;
    }

    public void onBindViewHolder(final WordMatchAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position), position);
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    public int getItemCount(){
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvWordToMatch;
        EditText etWordToMatch;
        CardView cvResult;
        WordMatchAdapter.OnNoteListener onNoteListener;

        ViewHolder(View itemView, WordMatchAdapter.OnNoteListener onNoteListener){
            super(itemView);
            tvWordToMatch = itemView.findViewById(R.id.rTvWordToMatch);
            etWordToMatch = itemView.findViewById(R.id.rEtWordToMatch);
            cvResult = itemView.findViewById(R.id.rCvResult);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        void bindData(String item, int position){
            tvWordToMatch.setText(item);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }



}
