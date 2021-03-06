package com.duolingo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.model.User;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<com.duolingo.app.adapter.RankingAdapter.ViewHolder>{

    private RankingAdapter.OnNoteListener mOnNoteListener;
    private List<User> mData;
    private LayoutInflater mInflater;
    private Context context;

    public RankingAdapter(List<User>rankingList, Context context, RankingAdapter.OnNoteListener onNoteListener){
        this.mOnNoteListener = onNoteListener;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = rankingList;
    }

    public com.duolingo.app.adapter.RankingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_ranking, null);
        return new com.duolingo.app.adapter.RankingAdapter.ViewHolder(view, mOnNoteListener);

    }

    public void setItems(List<User> items) {
        mData = items;
    }

    public void onBindViewHolder(final com.duolingo.app.adapter.RankingAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position), position);
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    public int getItemCount(){
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvUsername, tvElo, tvPos;
        RankingAdapter.OnNoteListener onNoteListener;

        ViewHolder(View itemView, RankingAdapter.OnNoteListener onNoteListener){
            super(itemView);
            tvUsername = itemView.findViewById(R.id.rTvUsername);
            tvElo = itemView.findViewById(R.id.rTvElo);
            tvPos = itemView.findViewById(R.id.rTvPos);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        void bindData(final User item, int position){
            //ivPhoto.setImageBitmap(item.getImage());
            tvUsername.setText(item.getUsername());
            tvElo.setText(Integer.toString(item.getElo()));
            tvPos.setText("#" + (position+1));
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
}
