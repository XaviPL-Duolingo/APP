package com.duolingo.app.ui.perfil;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.duolingo.app.MainActivity;
import com.duolingo.app.R;
import com.duolingo.app.adapter.SpinnerAdapter;
import com.duolingo.app.model.Language;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ServerConn;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PerfilFragment extends Fragment {

    private TextView tvUsername, tvXP, tvRanking;
    private ImageView ivAvatar;
    private ArrayList<Language> languageArrayList = new ArrayList<>();
    private Button btnConnect, btnChangeAvatar, btnDisconnect, btnDeleteAccount;
    public static ArrayList<Bitmap> arrayFlags;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvXP = (TextView) view.findViewById(R.id.tvXP);
        tvRanking = (TextView) view.findViewById(R.id.tvRanking);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userData", this.getActivity().getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (Data.hasConnection)
            getAllLanguages();

        Spinner spnLanguages = (Spinner) view.findViewById(R.id.spnLanguages);
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), languageArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLanguages.setAdapter(adapter);
        spnLanguages.setSelection(Data.KEYID_LANG - 1);
        spnLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language language = (Language) parent.getSelectedItem();

                if (Data.KEYID_LANG != language.getIdLanguage()){
                    Data.KEYID_LANG = language.getIdLanguage();
                    Data.KEYID_COURSE = 0;
                    System.out.println(Data.KEYID_LANG);
                    editor.putInt("KEYID_LANG", Data.KEYID_LANG);
                    editor.putInt("KEYID_COURSE", Data.KEYID_COURSE);
                    editor.apply();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // == SETTINGS == //

        btnConnect = view.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        btnChangeAvatar = view.findViewById(R.id.btnChangeAvatar);
        btnChangeAvatar.setOnClickListener(v -> {

        });

        btnDisconnect = view.findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(v -> {
            Data.userData = null;
            Data.hasConnection = false;
            editor.remove("KEYID_USER");
            editor.apply();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("BuholingoAPP");
            builder.setMessage("¿Estas seguro?");
            builder.setPositiveButton("SI", (dialog, which) -> {
               try {
                   ServerConn serverConn = (ServerConn) new ServerConn("deleteUser", Data.userData.getIdUser());
                   boolean isDeleted = (boolean) serverConn.returnObject();
                   if (isDeleted){
                       Toast.makeText(getContext(), "Cuenta eliminada correctamente!", Toast.LENGTH_LONG).show();
                       Data.userData = null;
                       Data.hasConnection = false;
                       editor.remove("KEYID_USER");
                       editor.apply();
                       Intent intent = new Intent(getContext(), MainActivity.class);
                       startActivity(intent);
                   }else {
                       Snackbar.make(getView(), "Error al eliminar tu cuenta, prueba más tarde...", Snackbar.LENGTH_LONG).show();
                   }
               } catch (IOException e) {
                   Snackbar.make(getView(), "Error al eliminar tu cuenta, prueba más tarde...", Snackbar.LENGTH_LONG).show();
                   e.printStackTrace();
               }
            });
            builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        checkConnection();

        return view;
    }

    private void checkConnection() {

        if (Data.hasConnection){
            btnConnect.setVisibility(View.GONE);
            btnChangeAvatar.setVisibility(View.VISIBLE);
            btnDisconnect.setVisibility(View.VISIBLE);
            btnDeleteAccount.setVisibility(View.VISIBLE);
            tvUsername.setText(Data.userData.getUsername());
            tvXP.setText(Data.userData.getXp() + "XP");
            tvRanking.setText(Data.userData.getIdRank().getNameRank().toUpperCase());

            try {
                URL newurl = new URL(Data.userData.getAvatar());
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                ivAvatar.setImageBitmap(mIcon_val);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            btnConnect.setVisibility(View.VISIBLE);
            btnChangeAvatar.setVisibility(View.GONE);
            btnDisconnect.setVisibility(View.GONE);
            btnDeleteAccount.setVisibility(View.GONE);
            tvUsername.setText("");
            tvXP.setText("");
            tvRanking.setText("");
            ivAvatar.setImageBitmap(null);
        }
    }

    private void getAllLanguages(){

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("getAllLanguages");
            List<Language> languageList = (List<Language>) serverConn.returnObject();
            languageArrayList.addAll(languageList);
            if (arrayFlags == null){
                arrayFlags = getFlags();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ArrayList<Bitmap> getFlags(){

        arrayFlags = new ArrayList<>();
        for (Language l : languageArrayList) {
            try{
                URL newurl = new URL(l.getFlagLanguage());
                Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                arrayFlags.add(mIcon_val);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arrayFlags;

    }

}