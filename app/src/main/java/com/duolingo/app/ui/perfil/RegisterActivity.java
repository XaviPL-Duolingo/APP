package com.duolingo.app.ui.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    private void gotoLogin(){

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    private void checkData(){

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

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("registerUser", username, email, password, Data.selectedLanguage);
            boolean success = (boolean) serverConn.returnObject();

            if (success){
                Toast.makeText(this, "Te has registrado correctamente!", Toast.LENGTH_SHORT).show();
                gotoLogin();
            }else {
                Toast.makeText(this, "Fallo al registrar usuario...", Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}