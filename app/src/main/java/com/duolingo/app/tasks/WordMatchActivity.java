package com.duolingo.app.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import com.duolingo.app.R;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;
import com.nex3z.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class WordMatchActivity extends AppCompatActivity {

    private HashMap<String, String> wordPairs = new HashMap<>();
    private ArrayList<String> arrayWords = new ArrayList<>();
    private ArrayList<String> arrayPairs = new ArrayList<>();
    private int totalPairs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_match);

        getData();

        Collections.shuffle(arrayWords);
        FlowLayout flWordsPairs = findViewById(R.id.flWordsPairs);
        for (String s : arrayWords) {
            Button button = new Button(this);
            button.setText(s);
            button.setBackgroundResource(R.drawable.next_layout);
            flWordsPairs.addView(button);
        }

        Collections.shuffle(arrayPairs);
        FlowLayout flMatchsPairs = findViewById(R.id.flMatchsPairs);
        for (String s : arrayPairs) {
            Button button = new Button(this);
            button.setText(s);
            button.setBackgroundResource(R.drawable.next_layout);
            flMatchsPairs.addView(button);
        }

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
                    arrayPairs.add((String) rawPair.get("word"));
                    wordPairs.put( (String) rawPair.get("match"), (String) rawPair.get("word"));
                    totalPairs++;
                }
                
            } catch (JSONException e) {
                System.out.println("[DEBUG] - Todas las parejas obtenidas!");
            }
        }
    }
}