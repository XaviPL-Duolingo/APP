package com.duolingo.app.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.duolingo.app.R;
import com.duolingo.app.model.Category;
import com.duolingo.app.model.Course;
import com.duolingo.app.model.Language;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ServerConn;

import java.util.ArrayList;
import java.util.List;

public class PerfilFragment extends Fragment {

    private TextView tvUsername, tvMail, tvRanking;
    private ArrayList<Language> languageArrayList = new ArrayList<>();
    private String userName, email, ranking;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // TextView = userName
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvUsername.setText(userName);

        // TextView = email
        tvMail = (TextView) view.findViewById(R.id.tvMail);
        tvMail.setText(email);

        // TextView = ranking
        tvRanking = (TextView) view.findViewById(R.id.tvRanking);
        tvRanking.setText(ranking);

        getAllLanguages();
        Spinner spnLanguages = (Spinner) view.findViewById(R.id.spnLanguages);
        ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, languageArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguages.setAdapter(adapter);
        spnLanguages.setSelection(Data.selectedLanguage);
        spnLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language language = (Language) parent.getSelectedItem();
                Data.idOriginLang = language.getIdLanguage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        return view;
    }

    private void getAllLanguages(){

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("getAllLanguages");
            List<Language> languageList = (List<Language>) serverConn.returnObject();
            languageArrayList.addAll(languageList);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}