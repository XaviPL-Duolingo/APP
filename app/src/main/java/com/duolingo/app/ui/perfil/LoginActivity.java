package com.duolingo.app.ui.perfil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.duolingo.app.MainActivity;
import com.duolingo.app.R;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.Encrypter;
import com.duolingo.app.util.ServerConn;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etEmail);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        Button btnRegister = findViewById(R.id.btnGotoRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
                finish();
            }
        });

    }



    private void gotoRegister() {

        // gotoRegister()
        // Metodo al pulsar sobre "btnRegister", este cambia de activity
        // hacia RegisterActivity

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

    private void checkData() {

        // checkData()
        // Metodo previo al mandar la solicitud de inicio de sesión al servidor, este
        // comprueba que los datos introducidos son correctos y los prepara para la conexion
        // al servidor.

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Datos introducidos no validos...", Toast.LENGTH_SHORT).show();
        } else {
            loginUser(username, Encrypter.getMD5(password));
        }
    }

    private void loginUser(String username, String pass) {

        // loginUser
        // Manda la solicitud al servidor y cominica al usuario si es correcta o no.

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("loginUser", username, pass);
            boolean success = (boolean) serverConn.returnObject();

            if (success){

                // Guarda el identificador del usuario en SharedPreferences
                Toast.makeText(this, "Inicio de sesion correcto!", Toast.LENGTH_SHORT).show();
                Data.hasConnection = true;
                SharedPreferences sharedPreferences = getSharedPreferences("userData", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("KEYID_USER", username);
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(this, "Inicio de sesion incorrecto...", Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}