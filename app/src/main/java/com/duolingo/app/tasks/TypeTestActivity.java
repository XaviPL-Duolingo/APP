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

import java.util.Random;

public class TypeTestActivity extends AppCompatActivity {

    // TipusTestActivity
    // Activity para los EXERCICES con idTypeExercice = 7

    private final int exTypePoints = 15, exTypeCoins = 15;

    private Button btAnswer1, btAnswer2, btAnswer3, btCheck;
    private TextView tvStatement;
    private String selectedButtonText, answer;
    private String[] arrayAnswers;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        tvStatement = findViewById(R.id.tvStatement);
        btAnswer1 = findViewById(R.id.btAnswer1);
        btAnswer2 = findViewById(R.id.btAnswer2);
        btAnswer3 = findViewById(R.id.btAnswer3);

        getData();
        setAnswersText();

        btCheck = findViewById(R.id.btCheck);
        btCheck.setEnabled(false);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(selectedButtonText, v);
            }
        });


    }

    private void getData(){

        // getData()
        // Método (mockUp) donde obtiene la información necesaria para este ejercicio.

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(ExerciceActivity.arrayExercices.size());
        progressBar.setProgress(ExerciceActivity.exIndex);

        if (getIntent().getExtras() != null){
            try {
                Exercice exerciceObj = (Exercice) getIntent().getSerializableExtra("data");
                JSONObject rawData = new JSONObject(exerciceObj.getContentExercice());
                tvStatement.setText((String) rawData.get("phrToTranslate"));
                arrayAnswers = new String[] {(String) rawData.get("correctAnswer"), (String) rawData.get("wrongAnswer1"), (String) rawData.get("wrongAnswer2")};
                answer = (String) rawData.get("correctAnswer");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void setAnswersText(){

        // setAnswersText()
        // Método donde se introduce de manera aleatoria las diferentes opciones en los
        // diferentes botones y se añade sus listeners.

        boolean isUnique = false;

        // Establece el texto del primer botón con una de las posibles respuestas.

        btAnswer1.setText(arrayAnswers[random.nextInt(3)]);
        btAnswer1.setOnClickListener(v -> selectedButtonText = pressedButton(btAnswer1));

        // Establece el texto del segundo botón y comprueba que no tenga el mismo texto que el
        // btAnswer1

        while (!isUnique){
            btAnswer2.setText(arrayAnswers[random.nextInt(3)]);
            if (btAnswer2.getText().equals(btAnswer1.getText())){
                isUnique = false;
            }else{
                isUnique = true;
            }
        }

        btAnswer2.setOnClickListener(v -> selectedButtonText = pressedButton(btAnswer2));

        // Establece el texto del tercer botón y comprueba que su texto no coincida con niguno
        // de los 2 botones creados anteriormente.

        isUnique = false;
        while (!isUnique){
            btAnswer3.setText(arrayAnswers[random.nextInt(3)]);
            if (btAnswer3.getText().equals(btAnswer1.getText())){
                isUnique = false;
            }else if(btAnswer3.getText().equals(btAnswer2.getText())){
                isUnique = false;
            }else{
                isUnique = true;
            }
        }

        btAnswer3.setOnClickListener(v -> selectedButtonText = pressedButton(btAnswer3));

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

    @Override
    public void onBackPressed() {

        // onBackPressed()
        // Al presionar el boton BACK, reemplaza su accion original por NO hacer nada

        return;
    }

}