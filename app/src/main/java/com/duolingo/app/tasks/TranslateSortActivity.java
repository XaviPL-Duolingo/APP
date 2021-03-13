package com.duolingo.app.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duolingo.app.R;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TranslateSortActivity extends AppCompatActivity {

    private String phrToTranslate, answer;
    private String[] arrayWords;
    private Button btCheck;
    private boolean isCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_sort);

        getData();

        TextView tvPhrToTranslate = findViewById(R.id.tvPhrToTranslate);
        tvPhrToTranslate.setText(phrToTranslate);

        FlowLayout flWords = findViewById(R.id.flWords);
        for (int i = 0; i < arrayWords.length; i++){
            Button btnWord = new Button(this);
            btnWord.setText(arrayWords[i]);
            btnWord.setBackgroundResource(R.drawable.next_layout);
            btnWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            flWords.addView(btnWord);
        }

        btCheck = findViewById(R.id.btNext);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        
    }

    private void checkAnswer() {

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
                JSONObject rawData = new JSONObject(exerciceObj.getContentExercice());
                phrToTranslate = (String) rawData.get("phrToTranslate");
                answer = (String) rawData.get("answer1");
                arrayWords = answer.split(" ");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}