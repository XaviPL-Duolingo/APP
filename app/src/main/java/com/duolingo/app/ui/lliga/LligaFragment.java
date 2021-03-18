package com.duolingo.app.ui.lliga;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.adapter.RankingAdapter;
import com.duolingo.app.adapter.SpacesItemDecoration;
import com.duolingo.app.model.User;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ServerConn;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class LligaFragment extends Fragment implements RankingAdapter.OnNoteListener{

    private TextView tvRankName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lliga, container, false);

        if (Data.hasConnection){
            List<User> rankingList = getRanking();

            final TextView tvUserPos = root.findViewById(R.id.tvUserPos);
            tvUserPos.setText("#" + (findUsingIterator(Data.userData.getUsername(), rankingList) + 1));

            final TextView tvUsername = root.findViewById(R.id.tvUserRank);
            tvUsername.setText(Data.userData.getUsername());

            final TextView tvUserElo = root.findViewById(R.id.tvUserElo);
            tvUserElo.setText(Integer.toString(Data.userData.getElo()));

            tvRankName = root.findViewById(R.id.tvRankName);
            tvRankName.setText(Data.userData.getIdRank().getNameRank().toUpperCase());

            final CardView cvRank = root.findViewById(R.id.cvRank);
            cvUserBackground(cvRank);

            RecyclerView rvRanking = root.findViewById(R.id.rvRanking);
            RankingAdapter listAdapter = new RankingAdapter(rankingList, getActivity().getApplicationContext(), this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvRanking.addItemDecoration(new SpacesItemDecoration(15));
            rvRanking.setLayoutManager(layoutManager);
            rvRanking.setAdapter(listAdapter);
        }

        return root;
    }

    private List<User> getRanking() {

        // getRanking()
        // Obtiene todos los usuarios que pertenezcan a la misma categoria que el usurio cliente
        // y muestra descendientemente su posición, sus datos y su puntuación.

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("getRanking", Data.userData.getIdRank().getIdRank());
            List<User> rankingList = (List<User>) serverConn.returnObject();
            return  rankingList;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private void cvUserBackground(CardView cardView){

        // cvUserBackground()
        // Dependiendo del "idRank", el color de la CardView cambia segun su valor.

        String rank = (String) tvRankName.getText();

        switch (rank){
            case "SILVER":
                cardView.setCardBackgroundColor(Color.GRAY);
                break;

            case "GOLD":
                cardView.setCardBackgroundColor(Color.YELLOW);
                break;

            case "DIAMOND":
                cardView.setCardBackgroundColor(Color.CYAN);
                break;

            case "EMERALD":
                cardView.setCardBackgroundColor(Color.GREEN);
                break;

            case "RUBY":
                cardView.setCardBackgroundColor(Color.RED);
                break;

            case "OBSIDIAN":
                cardView.setCardBackgroundColor(Color.BLACK);
                tvRankName.setTextColor(Color.WHITE);
                break;
        }

    }

    public int findUsingIterator(String name, List<User> rankingList) {
        Iterator<User> iterator = rankingList.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if (user.getUsername().equals(name)) {
                return rankingList.indexOf(user);
            }
        }
        return 0;
    }


    @Override
    public void onNoteClick(int position) {

    }
}

