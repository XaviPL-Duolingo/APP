package com.duolingo.app.ui.botiga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
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

    private List<Item> itemList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_botiga, container, false);

        if (Data.hasConnection){

            itemList = getItems();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("BuholingoAPP");
        builder.setMessage("¿Deseas aquirir [" + itemList.get(position).getNameItem() + "] por: " + itemList.get(position).getPriceItem() + "$?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Data.userData.getMoney() > itemList.get(position).getPriceItem()){
                    try {
                        ServerConn serverConn = (ServerConn) new ServerConn("buyItem", Data.userData.getIdUser(), itemList.get(position).getIdItem());
                        boolean success = (boolean) serverConn.returnObject();
                        if (success){
                            Toast.makeText(getContext(), "Compra realizada con éxito!...", Toast.LENGTH_SHORT).show();
                            getActivity().recreate();
                        }
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Ha surgido un error al realizar la compra... Abortando...", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), "No tienes suficientes monedas...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}