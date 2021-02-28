package com.duolingo.app.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.MainActivity;
import com.duolingo.app.model.Exercice;
import com.duolingo.app.tasks.OpenTransExActivity;
import com.duolingo.app.tasks.TipusTestExActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ExerciceActivity extends AppCompatActivity {

    public static ArrayList<Exercice> arrayExercices = new ArrayList<>();
    public static boolean hasFailed = false;
    public static int totalMoney, totalPoints;
    public static int exIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mkExercices();
        nextExercice(getApplicationContext());
        finish();
    }

    public void nextExercice(Context context){

        // nextExercice()
        // Método para pasar al siguiente ejercicio. Si es la primera vez que se usa de una
        // categoría obtiene su propio Context. Si viene de otro ejercico recibe el Context de
        // aquel ejercicio.

        // Dependiendo del ejercicio que toque "exIndex" compara su TypeExerciceID y abre uno u
        // otro ejercicio.

        if (exIndex < arrayExercices.size()){

            int exerciceTypeID = arrayExercices.get(exIndex).getTypeExercice();
            Intent intent;

            switch (exerciceTypeID){
                case 1:
                    intent = new Intent(context, OpenTransExActivity.class);
                    break;
                case 7:
                    intent = new Intent(context, TipusTestExActivity.class);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + exerciceTypeID);
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

        System.out.println("MONEY: "+totalMoney);
        System.out.println("POINTS: "+totalPoints);

        Data.mkMoney += totalMoney;
        Data.mkPoints += totalPoints;

        if (!hasFailed){
            Data.mkMoney += 150;
            System.out.println(hasFailed + " + 150");
        }

        updateXML();

        // Reset de variables estaticas.
        arrayExercices.clear();
        exIndex = 0;
        hasFailed = false;
        totalPoints = 0;
        totalMoney = 0;

        finish();

    }

    private void updateXML(){

        // updateXML()
        // Al acabar una categoría, añade las monedas ganadas al fichero XML y asi tenerlas
        // persistentes.

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbFactory.newDocumentBuilder();
            Document doc = db.parse(MainActivity.filename);

            Node eMoney = doc.getElementsByTagName("money").item(0);
            eMoney.setTextContent(Integer.toString(Data.mkMoney));

            // TAG POINTS
            Node ePoints = doc.getElementsByTagName("points").item(0);
            ePoints.setTextContent(Integer.toString(Data.mkPoints));

            // Transforma los Element i el Document a un fichero XML y lo guarda
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(MainActivity.filename);

            transformer.transform(source, result);
            System.out.println("Fichero actualizado correctamente: ["+ MainActivity.filename.getName()+"]");
            System.out.println("Guardando fichero en: ["+ MainActivity.filename.getAbsolutePath()+"]");

        }catch (Exception e){
            System.out.println("ERROR - No se ha podido actualizar: ["+ MainActivity.filename.getName()+"]");
            e.printStackTrace();
        }

    }

    public void mkExercices(){

        // mkExercices()
        // Aqui se crean los 5 ejercicos que se ejecutarán al clicar una categoria
        // Son mockUp.

        arrayExercices.add(new Exercice(1, 7, "Mi gato es negro", "My cat is black", "Black is the cat", "The cat as black"));
        arrayExercices.add(new Exercice(1, 1, "El esta durmiendo", "He is sleeping//He's sleeping"));
        arrayExercices.add(new Exercice(1, 7, "¡Eso fue increible!", "That was amazing", "This is incredible", "It is amazed"));
        arrayExercices.add(new Exercice(1, 7, "Se cayó y se hizo daño", "He fell and hurt himself", "Went down and died", "Auuuuuuu"));
        arrayExercices.add(new Exercice(1, 1, "Ella tiene un gato", "She have a cat//She owns a cat//She has a cat"));
    }
}