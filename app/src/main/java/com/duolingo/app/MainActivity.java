package com.duolingo.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.duolingo.app.model.User;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ServerConn;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String secretKey = "ssshhhhhhhhhhh!!!!";
    public static TextView tvMoney, tvElo;
    public static ImageView ivELO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getPreferences();

        // -- HACERLO CON ALGUN THREAD Y LUEGO JOIN PARA QUE ESPERE

        setTheme(R.style.TranslucentStatusBar);     // Fin Splash-Screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMoney = findViewById(R.id.tvMoney);
        tvElo = findViewById(R.id.tvElo);
        ivELO = findViewById(R.id.iconPoints);

        if (Data.hasConnection){
            updateData();
        }else {
            tvMoney.setText("0");
            tvElo.setText("0");
        }


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_curs, R.id.navigation_perfil, R.id.navigation_lliga, R.id.navigation_botiga)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void getPreferences(){

        // getPreferences()
        // Este metodo establece las primeras conexiones y los valores de las variables estaticas
        // principales del programa.

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", getApplicationContext().MODE_PRIVATE);
        Data.KEYID_USER = sharedPreferences.getString("KEYID_USER", "null");
        Data.KEYID_LANG = sharedPreferences.getInt("KEYID_LANG", 1);
        Data.KEYID_COURSE = sharedPreferences.getInt("KEYID_COURSE", 0);

        getUserData();

    }

    private void getUserData(){

        // getUserData()
        // A partir de la clave KEYID_USER se sabe si hay una sesión iniciada o no
        // Si la hay se conecta al servidor y se obtienen los datos del usuario.

        if (Data.KEYID_USER.equals("null")){
            Data.hasConnection = false;
        }else {
            Data.hasConnection = true;
            try {
                ServerConn serverConn = (ServerConn) new ServerConn("getUserData", Data.KEYID_USER);
                Data.userData = (User) serverConn.returnObject();

            }catch (Exception e){
                System.out.println("[SERVER] - Error al obtener datos del servidor...");
                e.printStackTrace();
            }
        }
    }

    public static void updateData(){

        tvMoney.setText(Integer.toString(Data.userData.getMoney()));
        tvElo.setText(Integer.toString(Data.userData.getElo()));

        switch (Data.userData.getIdRank().getIdRank()){
            case 1:
              ivELO.setImageResource(R.drawable.rank_silver);
              break;
            case 2:
                ivELO.setImageResource(R.drawable.rank_gold);
                break;
            case 3:
                ivELO.setImageResource(R.drawable.rank_diamond);
                break;
            case 4:
                ivELO.setImageResource(R.drawable.rank_emerald);
                break;
            case 5:
                ivELO.setImageResource(R.drawable.rank_ruby);
                break;
            case 6:
                ivELO.setImageResource(R.drawable.rank_obsidian);
                break;
        }

    }

}