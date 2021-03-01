package com.duolingo.app.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.duolingo.app.R;
import com.duolingo.app.util.Data;

public class PerfilFragment extends Fragment {

    private TextView tvUsername, tvMail, tvRanking;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // readXML();


        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvUsername.setText(Data.serverIP);
        tvUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // writeXML();
                }
            }
        });

        tvMail = (TextView) view.findViewById(R.id.tvMail);
        tvMail.setText(Data.userName);
        tvMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // writeXML();
                }
            }
        });

        tvRanking = (TextView) view.findViewById(R.id.tvRanking);
        tvRanking.setText(Data.password);
        tvRanking.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // writeXML();
                }
            }
        });

        // writeXML();

        return view;
    }

     /*public void readXML(){

        // readXML()
        // Este metodo se encarga de leer el fichero XML "Config.XML" y obtener los valores
        // introducidos en este para después guardarlos en la APP.

        if (MainActivity.filename.exists()){
            try {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbFactory.newDocumentBuilder();
                Document dom  = db.parse(MainActivity.filename);

                Data.serverIP = dom.getElementsByTagName("ip").item(0).getTextContent();
                Data.userName =  dom.getElementsByTagName("username").item(0).getTextContent();

                String encryptedKey = dom.getElementsByTagName("password").item(0).getTextContent();
                Data.password = Encrypter.decrypt(encryptedKey, MainActivity.secretKey);

                Data.mkMoney = Integer.parseInt(dom.getElementsByTagName("money").item(0).getTextContent());
                Data.mkPoints = Integer.parseInt(dom.getElementsByTagName("points").item(0).getTextContent());

            }catch (Exception e){
                e.getCause();
            }
        }else {
            System.out.println("El fichero no existe o no se encuentra disponible...");
        }
    }

    public void writeXML(){

        // writeXML()
        // Este método se encarga de obtener los valores introducidos en las EditText y sustituir los valores
        // dentro del XML "Config.xml" por los nuevos valores de las EditText.

        String newServerIP = etServerIP.getText().toString();
        String newUserName = etUserName.getText().toString();
        String newPassword = etPassword.getText().toString();

        boolean newData = checkChanges(newServerIP, newUserName, newPassword);

        if (newData){

            // Si newData = true
            // Se crea el documento XML de nuevo con los datos actualizados

            System.out.println("Nuevos datos detectados.");

            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbFactory.newDocumentBuilder();
                Document doc = db.parse(MainActivity.filename);

                // TAG SERVERIP
                Node eIP = doc.getElementsByTagName("ip").item(0);
                eIP.setTextContent(newServerIP);

                // TAG USERNAME
                Node eUsername = doc.getElementsByTagName("username").item(0);
                eUsername.setTextContent(newUserName);

                // TAG PASSWORD
                Node ePassword = doc.getElementsByTagName("password").item(0);
                ePassword.setTextContent(Encrypter.encrypt(newPassword, MainActivity.secretKey));

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


        }else{

            // Si newData = false
            // No se hace nada.

            System.out.println("No hay cambios...");
        }


    }

    public boolean checkChanges(String etServerIP, String etUsername, String etPassword){

        // checkChanges()
        // Si algun de los datos es diferente a los originales, es decir, ha variado, se envia un boolean
        // comunicandolo.

        if (!etServerIP.equals(Data.serverIP)){
            return true;
        }

        if (!etUsername.equals(Data.userName)){
            return true;
        }

        if (!etPassword.equals(Data.password)){
            return true;
        }

        return false;

    } */

}