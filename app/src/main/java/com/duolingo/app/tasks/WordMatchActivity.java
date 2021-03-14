package com.duolingo.app.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import com.duolingo.app.R;
import com.duolingo.app.adapter.RankingAdapter;
import com.duolingo.app.adapter.SpacesItemDecoration;
import com.duolingo.app.adapter.WordMatchAdapter;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.util.ExerciceActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordMatchActivity extends AppCompatActivity implements WordMatchAdapter.OnNoteListener{

    private HashMap<String, String> wordPairs = new HashMap<>();
    private ArrayList<String> arrayWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_match);

        getData();

        RecyclerView rvWordMatches = findViewById(R.id.rvWordMatches);
        WordMatchAdapter listAdapter = new WordMatchAdapter(arrayWords, getApplicationContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvWordMatches.addItemDecoration(new SpacesItemDecoration(15));
        rvWordMatches.setLayoutManager(layoutManager);
        rvWordMatches.setAdapter(listAdapter);

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
    public void onNoteClick(int position) {

    }
}