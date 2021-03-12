package com.duolingo.app.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.duolingo.app.model.Category;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.model.Level;
import com.duolingo.app.model.TypeExercice;
import com.duolingo.app.tasks.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciceActivity extends AppCompatActivity {

    public static ArrayList<Exercice> arrayExercices = new ArrayList<>();
    public static boolean hasFailed = false;
    public static int totalMoney, totalPoints, exIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExercices();
        nextExercice(getApplicationContext());
        finish();
    }

    public void nextExercice(Context context){

        // nextExercice()
        // Método para pasar al siguiente ejercicio. Si es el primer ejercicio se usa su propio
        // Context. Si viene de otro ejercico recibe el Context de aquel ejercicio.
        // Dependiendo del ejercicio que toque "exIndex" compara su TypeExerciceID y abre uno u
        // otro ejercicio.

        if (exIndex < arrayExercices.size()){

            Intent intent;
            TypeExercice typeExercice = arrayExercices.get(exIndex).getIdTypeExercice();

            switch (typeExercice.getIdTypeExercice()){
                case 1:
                    intent = new Intent(context, OpenTransExActivity.class);
                    break;
                case 7:
                    intent = new Intent(context, TipusTestExActivity.class);
                    break;
                default:
                    System.out.println("[DEBUG] - idTypeExercice NO valida...");
                    throw new IllegalStateException("Unexpected value: " + typeExercice.getIdTypeExercice());
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("data", arrayExercices.get(exIndex));
            context.startActivity(intent);
            exIndex++;

        }else{
            finishExercice();
        }
    }

    public void finishExercice(){

        // finishExercice()
        // Método que se ejecuta una vez se completa el ultimo ejercicio. Si el usuario ha acertado
        // todos los ejercicios a la primera se le otorga una recompensa extra de 150, además de sumar
        // todas las monedas y puntos obtenidos en esta categoría a su cuenta.

        System.out.println("[DEBUG] - ADDED: "+totalMoney + " MONEY");
        System.out.println("[DEBUG] - ADDED: "+totalPoints + " POINTS");

        if (!hasFailed){
            // Data.userMoney += 150;
            System.out.println(hasFailed + " + 150");
        }


        // Reset de variables estaticas.
        arrayExercices.clear();
        exIndex = 0;
        hasFailed = false;
        totalPoints = 0;
        totalMoney = 0;

        finish();

    }

    private void updateStats(){

    }

    public Level getLevel(){

        // getLastLevel()
        // Conexion al servidor pasando la ID del USER y la CATEGORY seleccionada
        // Devuelve el nivel que el USER ha de hacer.

        Intent intent = getIntent();
        Category categoryObj = (Category) intent.getSerializableExtra("category");

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("getLevel", Data.userData.getIdUser(), categoryObj.getIdCategory());
            Level levelObj =  (Level) serverConn.returnObject();
            System.out.println(levelObj.getIdLevel());
            return levelObj;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void getExercices(){

        // getExercices()
        // A partir de la ID del LEVEL que toca hacer, de la CATEGORY seleccionada, se obtienen
        // todos los EXERCICES que pertenezcan a este nuevo LEVEL y se guardan en un ArrayList

        try {
            Level levelObj = getLevel();
            ServerConn serverConn = (ServerConn) new ServerConn("getExercices", levelObj.getIdLevel());
            List<Exercice> exerciceList = (List<Exercice>) serverConn.returnObject();
            arrayExercices.addAll(exerciceList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}