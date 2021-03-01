package com.duolingo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.model.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<com.duolingo.app.adapter.CategoriesAdapter.ViewHolder> {

    private ArrayList<Category> mkCategories = new ArrayList<Category>();
    private OnNoteListener mOnNoteListener;
    private List<Category> mData;
    private LayoutInflater mInflater;
    private Context context;

    public CategoriesAdapter(List<Category> itemList, Context context, OnNoteListener onNoteListener){
        this.mOnNoteListener = onNoteListener;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;

    }

    public com.duolingo.app.adapter.CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_category, null);
        return new com.duolingo.app.adapter.CategoriesAdapter.ViewHolder(view, mOnNoteListener);

    }

    public void setItems(List<Category> items) {
        mData = items;

    }

    public void onBindViewHolder(final com.duolingo.app.adapter.CategoriesAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    public int getItemCount(){
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivPhoto;
        TextView tvTitle, tvLevel;
        ProgressBar progressBar;
        OnNoteListener onNoteListener;

        ViewHolder(View itemView, OnNoteListener onNoteListener){
            super(itemView);
            //ivPhoto = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.rTvTitle);
            tvLevel = itemView.findViewById(R.id.rTvLevel);
            progressBar = itemView.findViewById(R.id.progressBar);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        void bindData(final Category item){
            //ivPhoto.setImageBitmap(item.getImage());
            tvTitle.setText(item.getCategoryName());
            tvLevel.setText(item.getIdCategory());
            progressBar.setProgress(75);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }

}
