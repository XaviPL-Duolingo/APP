package com.duolingo.app.util;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConn {

    // public static String clientOption;
    // public static int parameters;
    private static Socket socket;

    public ServerConn(String clientOption){

        try {
            socket = new Socket(Data.serverIP, 25012);
            System.out.println("[SERVER] - Conexión establecida!");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(clientOption);
            // returnObject();
        } catch (IOException e) {
            System.out.println("[SERVER] - Imposible conectar con el servidor...");
            e.printStackTrace();
        }
    }

    public ServerConn(String clientOption, int parameters){

        try {
            socket = new Socket(Data.serverIP, 25012);
            System.out.println("[SERVER] - Conexión establecida!");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(clientOption);
            dos.writeInt(parameters);
            // returnObject();
        } catch (IOException e) {
            System.out.println("[SERVER] - Imposible conectar con el servidor...");
            e.printStackTrace();
        }
    }

    public ServerConn(String clientOption, String parameters){

        try {
            socket = new Socket(Data.serverIP, 25012);
            System.out.println("[SERVER] - Conexión establecida!");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(clientOption);
            dos.writeUTF(parameters);
            // returnObject();
        } catch (IOException e) {
            System.out.println("[SERVER] - Imposible conectar con el servidor...");
            e.printStackTrace();
        }
    }

    public ServerConn(String clientOption, String param1, String param2, String param3, int param4){

        try {
            socket = new Socket(Data.serverIP, 25012);
            System.out.println("[SERVER] - Conexión establecida!");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(clientOption);
            dos.writeUTF(param1);
            dos.writeUTF(param2);
            dos.writeUTF(param3);
            dos.writeInt(param4);
            // returnObject();
        } catch (IOException e) {
            System.out.println("[SERVER] - Imposible conectar con el servidor...");
            e.printStackTrace();
        }
    }




    public ServerConn(String clientOption, String param1, String param2){

        try {
            socket = new Socket(Data.serverIP, 25012);
            System.out.println("[SERVER] - Conexión establecida!");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(clientOption);
            dos.writeUTF(param1);
            dos.writeUTF(param2);
            // returnObject();
        } catch (IOException e) {
            System.out.println("[SERVER] - Imposible conectar con el servidor...");
            e.printStackTrace();
        }
    }

    public ServerConn(String clientOption, int param1, int param2){

        try {
            socket = new Socket(Data.serverIP, 25012);
            System.out.println("[SERVER] - Conexión establecida!");
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(clientOption);
            dos.writeInt(param1);
            dos.writeInt(param2);
            // returnObject();
        } catch (IOException e) {
            System.out.println("[SERVER] - Imposible conectar con el servidor...");
            e.printStackTrace();
        }
    }



    public Object returnObject() throws IOException {

        try {
            // DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            ObjectInputStream os = new ObjectInputStream(socket.getInputStream());
            Object object = os.readObject();
            // socket.close();
            return object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }
}
