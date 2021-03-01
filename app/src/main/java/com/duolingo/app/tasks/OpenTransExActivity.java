package com.duolingo.app.tasks;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.R;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;
import com.google.android.material.snackbar.Snackbar;

public class OpenTransExActivity extends AppCompatActivity {

    private final int exTypePoints = 20, exTypeCoins = 20;

    private String phrToTranslate;
    private String[] arraySolutions;
    private EditText etPlayerAnswer;
    private Button btCheck;
    private boolean isCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_trans_ex);

        getData();

        TextView tvPhrToTranslate = findViewById(R.id.tvPhrToTranslate);
        tvPhrToTranslate.setText(phrToTranslate);

        etPlayerAnswer = findViewById(R.id.etPlayerAnswer);
        etPlayerAnswer.setText("");

        btCheck = findViewById(R.id.btNext);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(etPlayerAnswer.getText().toString(), v);
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
            Exercice rawData = (Exercice) getIntent().getSerializableExtra("data");
            phrToTranslate = "Tomate"; //rawData.getExStatement();
            arraySolutions = "Alertr//frfr//ftr".split("//"); //rawData.getWord1().split("//");
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

        for (int i = 0; i < arraySolutions.length; i++){
            if (answer.equals(arraySolutions[i].toLowerCase())){
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