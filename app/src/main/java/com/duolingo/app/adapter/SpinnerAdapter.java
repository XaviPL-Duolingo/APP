package com.duolingo.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.duolingo.app.R;
import com.duolingo.app.model.Language;
import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<Language> {

    public SpinnerAdapter (Context context, ArrayList<Language> arrayLanguages){
        super(context, 0, arrayLanguages);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_row, parent, false
            );
        }

        // ImageView ivFlag = convertView.findViewById(R.id.ivFlag);
        TextView tvLangName = convertView.findViewById(R.id.tvLangName);

        Language currentItem = (Language) getItem(position);

        if (currentItem != null){
            // ivFlag.setImageBitmap(currentItem.getFlag());
            tvLangName.setText(currentItem.getLamguageName());
        }

        return convertView;

    }
}
