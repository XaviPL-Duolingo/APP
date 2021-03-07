package com.duolingo.app.ui.botiga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.adapter.RankingAdapter;
import com.duolingo.app.adapter.SpacesItemDecoration;
import com.duolingo.app.adapter.StoreAdapter;
import com.duolingo.app.model.Item;
import com.duolingo.app.model.User;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ServerConn;

import java.io.IOException;
import java.util.List;

public class BotigaFragment extends Fragment implements StoreAdapter.OnNoteListener{

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_botiga, container, false);

        if (Data.hasConnection){

            List<Item> itemList = getItems();
            RecyclerView rvStore = root.findViewById(R.id.rvStore);
            StoreAdapter listAdapter = new StoreAdapter(itemList, getActivity().getApplicationContext(), this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvStore.addItemDecoration(new SpacesItemDecoration(75));
            rvStore.setLayoutManager(layoutManager);
            rvStore.setAdapter(listAdapter);

        }

        return root;
    }

    private List<Item> getItems() {

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("getItems");
            List<Item> rankingList = (List<Item>) serverConn.returnObject();
            return  rankingList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void onNoteClick(int position) {

    }
}