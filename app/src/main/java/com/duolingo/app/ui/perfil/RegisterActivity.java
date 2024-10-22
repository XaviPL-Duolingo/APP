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

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        final Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> checkData());

        final Button btnLogin = findViewById(R.id.btnGotoLogin);
        btnLogin.setOnClickListener(v -> {
            gotoLogin();
            finish();
        });

    }

    private void gotoLogin(){

        // gotoLogin()
        // Si se pulsa btnGotoLogin se intenta abrir su activity correspondiente.

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    private void checkData(){

        // checkData()
        // Métodod de verificacion de los datos introducidos por el usuario, si hay algun problema
        // avisa al usuario de ello, si todos son validos entonces ejecuta registerUser()

        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || !email.contains("@") || password.isEmpty()){
            Toast.makeText(this, "Datos introducidos no validos...", Toast.LENGTH_SHORT).show();
        }else {
            registerUser(username, email, Encrypter.getMD5(password));
        }

    }

    private void registerUser(String username, String email, String password){

        // registerUser()
        // Llama al servidor para realizar una peticion de registro con los datos del usuario
        // Si el registro es correcto se guarda su KEYID_USER y se habilita el modo online

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("registerUser", username, email, password, Data.KEYID_LANG);
            boolean success = (boolean) serverConn.returnObject();

            if (success){
                Toast.makeText(this, "Te has registrado correctamente!", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("userData", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("KEYID_USER", username);
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, "Fallo al registrar usuario // El usuario YA existe...", Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}