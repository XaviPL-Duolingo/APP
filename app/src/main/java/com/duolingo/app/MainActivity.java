package com.duolingo.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.duolingo.app.model.Course;
import com.duolingo.app.model.User;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ServerConn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static File folder, filename;
    public static final String secretKey = "ssshhhhhhhhhhh!!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getPreferences();

        // -- HACERLO CON ALGUN THREAD Y LUEGO JOIN PARA QUE ESPERE

        setTheme(R.style.TranslucentStatusBar);     // Fin Splash-Screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_curs, R.id.navigation_perfil, R.id.navigation_lliga, R.id.navigation_botiga)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void getPreferences(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        createConfigFile();
        firstReadJSON();

        SharedPreferences sharedPreferences = getSharedPreferences("userData", getApplicationContext().MODE_PRIVATE);
        Data.KEYID_USER = sharedPreferences.getString("KEYID_USER", "null");

        getUserData();

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("getAllCoursesByID", Data.idOriginLang);
            Data.listCourses = (List<Course>) serverConn.returnObject();
            Thread.sleep(1000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getUserData(){

        // getUserData()
        // A partir de la clave KEYID_USER se sabe si hay una sesión iniciada o no
        // Si la hay se conecta al servidor y se obtienen los datos del usuario.

        if (Data.KEYID_USER == null){
            Data.hasConnection = false;
        }else {
            Data.hasConnection = true;
            try {
                ServerConn serverConn = (ServerConn) new ServerConn("getUserData", Data.KEYID_USER);
                User userObj = (User) serverConn.returnObject();
                Data.idOriginLang = userObj.getIdOriginLang().getIdLanguage();
            }catch (Exception e){
                System.out.println("[SERVER] - Error al obtener datos del servidor...");
                e.printStackTrace();
            }
        }



    }

    private File jsonSingleton() {

        // xmlSingleton()
        // Singleton.

        if (filename == null) {
            filename = new File(folder, "config.json");
        }

        return filename;
    }

    private void createConfigFile(){

        // createConfigFile()
        // Método donde se crea la subcarpeta "config" en el directorio privado de la aplicación y una vez creada
        // y verificada de que existe, crea un fichero XML llamado "Config.xml" mediande el método
        // xmlSingleton()


        try {
            folder = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath(), "config");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            filename = jsonSingleton();
            if (!filename.exists()){
                filename.createNewFile();
                firstWriteJSON();
            }

        }catch (Exception e){
            e.getCause();
        }
    }

    private void firstWriteJSON(){

        // firstWriteJSON()
        // Este método crea el .JSON con los datos necesarios para la aplicación pero sin información.

        if (filename.exists()){
            try {
                JSONObject fileJSON = new JSONObject();
                fileJSON.put("server_ip", "192.168.1.212");
                fileJSON.put("user_language", "0");
                fileJSON.put("selected_course", "0");

                // Parsear a formato JSON y escribir en config.json

                Writer saveFile = new BufferedWriter(new FileWriter(filename));
                saveFile.write(fileJSON.toString(4));
                saveFile.close();
                System.out.println(fileJSON.toString(4));


            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("[DEBUG] - El fichero [config.json] NO existe...");
        }

    }

    private void firstReadJSON(){

        if (filename.exists()){
            try {

                FileReader fileReader = new FileReader(filename);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null){
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
                String fileParsed = stringBuilder.toString();

                JSONObject fileJSON = new JSONObject(fileParsed);
                Data.serverIP = (String) fileJSON.get("server_ip");
                Data.selectedLanguage = fileJSON.getInt("user_language");
                Data.selectedCourse = fileJSON.getInt("selected_course");


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }

    }

}