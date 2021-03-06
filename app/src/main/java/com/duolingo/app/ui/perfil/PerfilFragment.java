package com.duolingo.app.ui.perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.duolingo.app.MainActivity;
import com.duolingo.app.R;
import com.duolingo.app.model.Language;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ServerConn;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class PerfilFragment extends Fragment {

    private TextView tvUsername, tvXP, tvRanking;
    private ArrayList<Language> languageArrayList = new ArrayList<>();
    private Button btnConnect, btnChangeAvatar, btnStats, btnDisconnect, btnDeleteAccount;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // TextView = userName
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvXP = (TextView) view.findViewById(R.id.tvXP);
        tvRanking = (TextView) view.findViewById(R.id.tvRanking);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userData", this.getActivity().getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        getAllLanguages();
        Spinner spnLanguages = (Spinner) view.findViewById(R.id.spnLanguages);
        ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, languageArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguages.setAdapter(adapter);
        spnLanguages.setSelection(Data.KEYID_LANG - 1);
        spnLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language language = (Language) parent.getSelectedItem();
                Data.KEYID_LANG = language.getIdLanguage();
                System.out.println(Data.KEYID_LANG);
                editor.putInt("KEYID_LANG", Data.KEYID_LANG);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // == SETTINGS == //

        btnConnect = view.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnChangeAvatar = view.findViewById(R.id.btnChangeAvatar);
        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnStats = view.findViewById(R.id.btnStats);
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDisconnect = view.findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.hasConnection = false;
                editor.remove("KEYID_USER");
                editor.apply();
                // checkConnection();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        checkConnection();


        return view;
    }

    private void checkConnection() {

        if (Data.hasConnection){
            btnConnect.setVisibility(View.GONE);
            btnChangeAvatar.setVisibility(View.VISIBLE);
            btnStats.setVisibility(View.VISIBLE);
            btnDisconnect.setVisibility(View.VISIBLE);
            btnDeleteAccount.setVisibility(View.VISIBLE);
            tvUsername.setText(Data.userData.getUsername());
            tvXP.setText(Data.userData.getXp() + "XP");
            tvRanking.setText(Data.userData.getIdRank().getNameRank().toUpperCase());
        }else {
            btnConnect.setVisibility(View.VISIBLE);
            btnChangeAvatar.setVisibility(View.GONE);
            btnStats.setVisibility(View.GONE);
            btnDisconnect.setVisibility(View.GONE);
            btnDeleteAccount.setVisibility(View.GONE);
            tvUsername.setText("");
            tvXP.setText("");
            tvRanking.setText("");
        }
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