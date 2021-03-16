package com.duolingo.app.tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.adapter.SpacesItemDecoration;
import com.duolingo.app.adapter.WordMatchAdapter;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class WordMatchActivity extends AppCompatActivity implements WordMatchAdapter.OnNoteListener{

    private int exTypeCoins = 5, exTypePoints = 5;
    private HashMap<String, String> wordPairs = new HashMap<>();
    private ArrayList<String> arrayWords = new ArrayList<>();
    private RecyclerView rvWordMatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_match);

        getData();

        rvWordMatches = findViewById(R.id.rvWordMatches);
        WordMatchAdapter listAdapter = new WordMatchAdapter(arrayWords, getApplicationContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvWordMatches.addItemDecoration(new SpacesItemDecoration(15));
        rvWordMatches.setLayoutManager(layoutManager);
        rvWordMatches.setAdapter(listAdapter);
        
        Button btnNext = findViewById(R.id.btNext2);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setEnabled(false);
                checkAnswers(v);
            }
        });

    }

    private void checkAnswers(View v) {

        // checkAnswers()
        // Obtiene todos los items de la RecyclerView y comprueba si el texto introducido
        // en los EditText de estos es igual a la palabra que debe ser la respuesta correcta
        // Segun el resultado, la CardView de cada item cambia de color a VERDE o ROJO.

        int correctAnswers = 0;

        for (int i = 0; i < arrayWords.size(); i++){
            try {
                View itemView = rvWordMatches.getLayoutManager().findViewByPosition(i);
                EditText rEtWordToMatch = itemView.findViewById(R.id.rEtWordToMatch);
                rEtWordToMatch.setEnabled(false);
                CardView rCvResult = itemView.findViewById(R.id.rCvResult);
                String playerAnswer = rEtWordToMatch.getText().toString();
                String correctAnswer = wordPairs.get(arrayWords.get(i));
                if (playerAnswer.toLowerCase().equals(correctAnswer.toLowerCase())){
                    rCvResult.setCardBackgroundColor(Color.GREEN);
                    ExerciceActivity.totalMoney += exTypeCoins;
                    ExerciceActivity.totalXP += exTypePoints;
                    correctAnswers++;
                }else {
                    ExerciceActivity.hasFailed = true;
                    rCvResult.setCardBackgroundColor(Color.RED);
                }

            }catch (Exception e){
                System.out.println("[DEBUG] - Hay algunas respuestas en blanco...");
            }
        }

        String msg = "Palabras emparejadas correctamente: [" + correctAnswers + "/"+ arrayWords.size() + "]";

        // Mostra SNACKBAR
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.snack_next, new View.OnClickListener(){
            public void onClick(View view) {
                ExerciceActivity e = new ExerciceActivity();
                e.nextExercice(getApplicationContext());
                finish();
            }
        });

        snackbar.setActionTextColor(Color.parseColor("#cb2cc6"));
        snackbar.show();
        
    }

    private void getData(){

        // getData()
        // Obtiene y parsea los datos del EXERCICE en JSON y asigna
        // sus valores donde corresponda.

        ProgressBar progressBar = findViewById(R.id.progressBar3);
        progressBar.setMax(ExerciceActivity.arrayExercices.size());
        progressBar.setProgress(ExerciceActivity.exIndex);

        if (getIntent().getExtras() != null){
            try {
                Exercice exerciceObj = (Exercice) getIntent().getSerializableExtra("data");
                JSONObject rawData = new JSONObject(exerciceObj.getContentExercice());
                for (int i = 1; i < 100; i++){
                    JSONObject rawPair = rawData.getJSONObject("wordMatch"+i);
                    arrayWords.add((String) rawPair.get("match"));
                    wordPairs.put( (String) rawPair.get("match"), (String) rawPair.get("word"));
                }
                
            } catch (JSONException e) {
                System.out.println("[DEBUG] - Todas las parejas obtenidas!");
            }
        }
    }

    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.TranslucentStatusBar));
        builder.setTitle("BuholingoAPP");
        builder.setMessage("¿Estas seguro que quieres abandonar el ejercicio? Perderas todas las recompensas obtenidas en este");
        builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("SI", (dialog, which) -> {
            try {
                ExerciceActivity.clearData();
                finish();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onNoteClick(int position) {

    }
}