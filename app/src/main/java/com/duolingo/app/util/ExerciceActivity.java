package com.duolingo.app.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.MainActivity;
import com.duolingo.app.model.Category;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.model.Level;
import com.duolingo.app.model.TypeExercice;
import com.duolingo.app.model.User;
import com.duolingo.app.tasks.*;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciceActivity extends AppCompatActivity {

    public static ArrayList<Exercice> arrayExercices = new ArrayList<>();
    public static boolean hasFailed = false;
    public static int totalMoney, totalXP, exIndex = 0;

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
                    intent = new Intent(context, TranslateOpenActivity.class);
                    break;
                case 2:
                    intent = new Intent(context, TranslateSortActivity.class);
                    break;
                case 3:
                    intent = new Intent(context, TranslateSortActivity.class);
                    break;
                case 4:
                    intent = new Intent(context, TranslateOpenActivity.class);
                    break;
                case 5:
                    intent = new Intent(context, WordMatchActivity.class);
                    break;
                case 6:
                    intent = new Intent(context, WordFillActivity.class);
                    break;
                case 7:
                    intent = new Intent(context, TypeTestActivity.class);
                    break;
                default:
                    System.out.println("[DEBUG] - idTypeExercice NO valida...");
                    throw new IllegalStateException("Unexpected value: " + typeExercice.getIdTypeExercice());
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("data", arrayExercices.get(exIndex));
            context.startActivity(intent);
            exIndex++;

        }else {
            finishExercice();
        }
    }


    public void finishExercice(){

        // finishExercice()
        // Método que se ejecuta una vez se completa el ultimo ejercicio. Si el usuario ha acertado
        // todos los ejercicios a la primera se le otorga una recompensa extra de 150, además de sumar
        // todas las monedas y puntos obtenidos en esta categoría a su cuenta.

        System.out.println("[DEBUG] - ADDED: "+totalMoney + " MONEY");
        System.out.println("[DEBUG] - ADDED: "+ totalXP + " POINTS");

        if (!hasFailed){
            totalMoney += 150;
            System.out.println(hasFailed + " + 150");
        }

        updateStats();

        // Reset de variables estaticas.
        arrayExercices.clear();
        exIndex = 0;
        hasFailed = false;
        totalXP = 0;
        totalMoney = 0;

        MainActivity.updateData();
        finish();

    }

    private void updateStats(){

        // Añadir el dinero al usuario obtenido
        int userMoney = Data.userData.getMoney();
        Data.userData.setMoney(userMoney + totalMoney);

        // Añadir la XP al usuario obtenida
        int userPoints = Data.userData.getXp();
        Data.userData.setXp((userPoints + totalXP));

        // Añadir ELO al usuario, este será la mitad de la XP
        int userELO = Data.userData.getElo();
        Data.userData.setElo(userELO + (totalXP / 2));

        try {
            JSONObject parsedJSON = Data.userData.toJSON();
            ServerConn serverConn = (ServerConn) new ServerConn("updateUser", parsedJSON.toString());
            User backupUser = Data.userData;
            Data.userData = (User) serverConn.returnObject();
            if (Data.userData != null){
                System.out.println("[DEBUG] - Usuario actualizado en la DB correctamente!");
            }else {
                System.out.println("[DEBUG] - Error al actualizar en la DB...");
                Data.userData = backupUser;
                Data.userData.setMoney(userMoney);
                Data.userData.setXp((userPoints));
                Data.userData.setElo(userELO);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
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
            if (levelObj != null){
                ServerConn serverConn = (ServerConn) new ServerConn("getExercices", levelObj.getIdLevel());
                List<Exercice> exerciceList = (List<Exercice>) serverConn.returnObject();
                arrayExercices.addAll(exerciceList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}