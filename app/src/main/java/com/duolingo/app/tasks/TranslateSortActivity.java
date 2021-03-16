package com.duolingo.app.tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;
import com.google.android.material.snackbar.Snackbar;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TranslateSortActivity extends AppCompatActivity {

    private int exTypePoints = 15, exTypeCoins = 15;

    private FlowLayout flAnswer, flWords;
    private String phrToTranslate, answer;
    private List<String> arrayWords;
    private Button btCheck;
    private boolean enableTTS = false;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_sort);

        getData();

        TextView tvPhrToTranslate = findViewById(R.id.tvPhrToTranslate);
        tvPhrToTranslate.setText(phrToTranslate);

        // Si idTypeExercice no es == 4 no se habilita la opcion para activar TTS
        // Esto se hace ya que son el mismo tipo de ejercicio, solo que uno tiene TTS habilitado
        // y el otro no.

        ImageView ivTTS = findViewById(R.id.ivTTS);
        ivTTS.setOnClickListener(v -> initTTS(phrToTranslate));
        if (!enableTTS){
            ivTTS.setVisibility(View.GONE);
        }

        flAnswer = findViewById(R.id.flAnswer);
        flWords = findViewById(R.id.flWords);

        for (int i = 0; i < arrayWords.size(); i++){
            Button btnWord = new Button(this);
            btnWord.setBackgroundResource(R.drawable.next_layout);
            btnWord.setText(arrayWords.get(i));

            // Dependiendo del FlowLayout donde se encuentre, varia de layout al pulsar

            btnWord.setOnClickListener(v -> {
                if (v.getParent() == flWords){
                    flWords.removeView(v);
                    flAnswer.addView(v);
                }else {
                    flAnswer.removeView(v);
                    flWords.addView(v);
                }
            });

            flWords.addView(btnWord);

        }

        btCheck = findViewById(R.id.btNext);
        btCheck.setOnClickListener(v -> checkAnswer(v));
        
    }

    private void initTTS(String text) {

        // initTTS()
        // Crea un nuevo motor TTS con idioma CASTELLANO y lee el valor de "phrToTranslate"

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS){
                tts.setLanguage(new Locale("spa", "SPA"));
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

            }
        });
    }

    private void checkAnswer(View v) {

        // checkAnswer()
        // Recorre entero "flAnswer" y junta todas las palabras en una sola
        //  linea y comprueba si es igual a "answer"

        btCheck.setEnabled(false);

        String playerAnswer = "";
        for (int i = 0; i < flAnswer.getChildCount(); i++){
            Button btnAnswer = (Button) flAnswer.getChildAt(i);
            btnAnswer.setEnabled(false);
            playerAnswer = playerAnswer.concat(btnAnswer.getText() + " ");
        }

        if (playerAnswer.toLowerCase().equals(answer.concat(" ").toLowerCase())){
            showResult(true, v);
        }else {
            showResult(false, v);
        }


    }

    private void showResult(boolean isCorrect, View v) {

        // showResult()
        // Dependiendo del booleano isCorrect muestra un mensaje u otro y añade o no monedas
        // al total de este nivel.

        String msg = "";

        // Si esta OK o dona ERROR.
        if (isCorrect){
            ExerciceActivity.totalXP += exTypePoints;
            ExerciceActivity.totalMoney += exTypeCoins;
            msg = "OK! ";

        }else{
            ExerciceActivity.hasFailed = true;
            msg = "ERROR... ";
        }

        // Si es el ultimo nivel
        if (ExerciceActivity.exIndex == ExerciceActivity.arrayExercices.size()){
            msg = msg + "Puntos obtenidos : ["+ExerciceActivity.totalXP +"] -- Monedas obtenidas: ["+ExerciceActivity.totalXP +"]";
            // Si hasFailed es FALSE
            if (!ExerciceActivity.hasFailed){
                msg = msg + " [+150 BONUS]";
            }
        }

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

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(ExerciceActivity.arrayExercices.size());
        progressBar.setProgress(ExerciceActivity.exIndex);

        if (getIntent().getExtras() != null){
            try {
                Exercice exerciceObj = (Exercice) getIntent().getSerializableExtra("data");

                if (exerciceObj.getIdTypeExercice().getIdTypeExercice() == 3)
                    enableTTS = true;

                JSONObject rawData = new JSONObject(exerciceObj.getContentExercice());
                phrToTranslate = (String) rawData.get("phrToTranslate");
                answer = (String) rawData.get("answer1");
                arrayWords = Arrays.asList(answer.split(" "));
                Collections.shuffle(arrayWords);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

        // onBackPressed()
        // Al presionar el boton BACK, reemplaza su accion original por NO hacer nada

        return;
    }
}