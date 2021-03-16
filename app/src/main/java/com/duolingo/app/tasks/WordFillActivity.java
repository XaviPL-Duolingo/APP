package com.duolingo.app.tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.R;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WordFillActivity extends AppCompatActivity {

    private int exTypeCoins = 15, exTypePoints = 15;
    private String phrToComplete, answer, selectedButtonText = "";
    private List<String> arrayAnswers;
    private Button btAnswer1, btAnswer2, btAnswer3, btCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_fill);

        getData();

        // Remplaza %WORD% en la phrToComplete por "______"

        TextView tvStatement = findViewById(R.id.tvStatement2);
        tvStatement.setText(phrToComplete.replace("%WORD%", "______"));

        btAnswer1 = findViewById(R.id.btAnswer);
        btAnswer1.setText(arrayAnswers.get(0));
        btAnswer1.setOnClickListener(v -> selectedButtonText = pressedButton(btAnswer1));

        btAnswer2 = findViewById(R.id.btAnswer4);
        btAnswer2.setText(arrayAnswers.get(1));
        btAnswer2.setOnClickListener(v -> selectedButtonText = pressedButton(btAnswer2));

        btAnswer3 = findViewById(R.id.btAnswer5);
        btAnswer3.setText(arrayAnswers.get(2));
        btAnswer3.setOnClickListener(v -> selectedButtonText = pressedButton(btAnswer3));


        btCheck = findViewById(R.id.btCheck2);
        btCheck.setOnClickListener(v -> checkAnswer(selectedButtonText, v));

    }

    public String pressedButton(Button pressedButton){

        // pressedButton()
        // Se activa al pulsar uno de los botones. Resetea todos los fondos y cambia el fondo
        // del boton presionado para diferenciarlo del resto. Devuevle una STRING con el texto
        // del boton seleccionado.

        // Fondo de los botones. Primero cambia todos los fondos al mismo
        btAnswer1.setBackgroundResource(R.drawable.answer_layout);
        btAnswer2.setBackgroundResource(R.drawable.answer_layout);
        btAnswer3.setBackgroundResource(R.drawable.answer_layout);

        btCheck.setEnabled(true);

        // Luego cambia el fondo al botón marcado para diferenciarlo.
        pressedButton.setBackgroundResource(R.drawable.next_layout);

        return (String) pressedButton.getText();

    }

    private void getData(){

        // getData()
        // Obtiene y parsea los datos del EXERCICE en JSON y asigna
        // sus valores donde corresponda.

        ProgressBar progressBar = findViewById(R.id.progressBar2);
        progressBar.setMax(ExerciceActivity.arrayExercices.size());
        progressBar.setProgress(ExerciceActivity.exIndex);

        if (getIntent().getExtras() != null){
            try {
                Exercice exerciceObj = (Exercice) getIntent().getSerializableExtra("data");
                JSONObject rawData = new JSONObject(exerciceObj.getContentExercice());
                String[] tempAnswer = new String[] {(String) rawData.get("correctAnswer"), (String) rawData.get("wrongAnswer1"), (String) rawData.get("wrongAnswer2")};
                phrToComplete = (String) rawData.get("phrToComplete");
                arrayAnswers = Arrays.asList(tempAnswer);
                answer = (String) rawData.get("correctAnswer");
                Collections.shuffle(arrayAnswers);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkAnswer(String selectedButtonText, final View v){

        // checkAnswer()
        // Comprueba que el texto del botón que ha sido presionado es el mismo que el valor de
        // answer. Si coincide habilita el botón btNext y muestra una SnackBar, donde al presionar ambas
        // avanza al siguiente nivel.

        btCheck.setEnabled(false);
        btAnswer1.setEnabled(false);
        btAnswer2.setEnabled(false);
        btAnswer3.setEnabled(false);

        String msg = "";
        if (selectedButtonText.equals(answer)){
            ExerciceActivity.totalMoney += exTypeCoins;
            ExerciceActivity.totalXP += exTypePoints;
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
        snackbar.setAction(R.string.snack_next, view -> {
            ExerciceActivity e = new ExerciceActivity();
            e.nextExercice(getApplicationContext());
            finish();
        });

        snackbar.setActionTextColor(Color.parseColor("#cb2cc6"));
        snackbar.show();
    }

    @Override
    public void onBackPressed() {

        // onBackPressed()
        // Al presionar el boton BACK, reemplaza su accion original por NO hacer nada

        return;
    }
}