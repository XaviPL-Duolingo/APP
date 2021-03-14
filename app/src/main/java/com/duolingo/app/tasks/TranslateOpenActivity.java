package com.duolingo.app.tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class TranslateOpenActivity extends AppCompatActivity {

    private final int exTypePoints = 20, exTypeCoins = 20;

    private String phrToTranslate;
    private ArrayList<String> arraySolutions = new ArrayList<>();
    private EditText etPlayerAnswer;
    private Button btCheck;
    private boolean isCorrect = false, enableTTS = false;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_trans_ex);

        getData();

        TextView tvPhrToTranslate = findViewById(R.id.tvPhrToTranslate);
        tvPhrToTranslate.setText(phrToTranslate);

        ImageView ivTTS = findViewById(R.id.ivTTS2);
        ivTTS.setOnClickListener(v -> initTTS(phrToTranslate));
        if (!enableTTS){
            ivTTS.setVisibility(View.GONE);
        }

        etPlayerAnswer = findViewById(R.id.etPlayerAnswer);
        etPlayerAnswer.setText("");

        btCheck = findViewById(R.id.btNext);
        btCheck.setOnClickListener(v -> checkAnswer(etPlayerAnswer.getText().toString(), v));
    }

    private void initTTS(String text) {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS){
                tts.setLanguage(new Locale("spa", "SPA"));
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

            }
        });
    }

    public void getData(){

        // getData()
        // Método (mockUp) donde obtiene la información necesaria para este ejercicio.

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(ExerciceActivity.arrayExercices.size());
        progressBar.setProgress(ExerciceActivity.exIndex);

        if (getIntent().getExtras() != null){
            try {
                Exercice exerciceObj = (Exercice) getIntent().getSerializableExtra("data");

                if (exerciceObj.getIdTypeExercice().getIdTypeExercice() == 4)
                    enableTTS = true;

                JSONObject rawData = new JSONObject(exerciceObj.getContentExercice());
                phrToTranslate = (String) rawData.get("phrToTranslate");
                for (int i = 1; i < 100; i++){
                    arraySolutions.add((String) rawData.get("answer"+ i));
                }
            } catch (JSONException e) {
                System.out.println("[DEBUG] - Obtenidas todas las respuestas posibles!");
            }
        }
    }

    public void checkAnswer(String answer, View v){

        // checkAnswer()
        // Comprueba que el texto introducido en "etPlayerAnswer" coincida con alguna de las
        // String en "arraySolutions". Una vez comprobado lo notifica al jugador con una SnackBar
        // y al clicar en ella te permite acceder al siguiente nivel.

        answer = fixInput(answer);

        etPlayerAnswer.setEnabled(false);
        btCheck.setEnabled(false);

        for (int i = 0; i < arraySolutions.size(); i++){
            if (answer.equals(arraySolutions.get(i).toLowerCase())){
                isCorrect = true;
                break;
            }
        }

        String msg = "";

        // Si esta OK o dona ERROR.
        if (isCorrect){
            ExerciceActivity.totalPoints += exTypePoints;
            ExerciceActivity.totalMoney += exTypeCoins;
            msg = "OK! ";

        }else{
            ExerciceActivity.hasFailed = true;
            msg = "ERROR... ";
        }

        // Si es el ultimo nivel
        if (ExerciceActivity.exIndex == ExerciceActivity.arrayExercices.size()){
            msg = msg + "Puntos obtenidos : ["+ExerciceActivity.totalPoints+"] -- Monedas obtenidas: ["+ExerciceActivity.totalPoints+"]";
            // Si hasFailed es FALSE
            if (!ExerciceActivity.hasFailed){
                msg = msg + " [+150 BONUS]";
            }
        }

        // Mostra SNACKBAR
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.snack_next, view -> {
            ExerciceActivity e = new ExerciceActivity();
            e.nextExercice(getApplicationContext());
            finish();
        });
        snackbar.setActionTextColor(Color.parseColor("#cb2cc6"));
        snackbar.show();

    }

    public static String fixInput(String answer) {

        // fixInput()
        // Filtro de caracteres especiales para "answer"

        return answer.replaceAll("[.:,;!\"·$%&/()=?¿¡]", "").replaceAll("[\\s]+", " ").toLowerCase();
    }

    @Override
    public void onBackPressed() {

        // onBackPressed()
        // Al presionar el boton BACK, reemplaza su accion original por NO hacer nada

        return;
    }

}