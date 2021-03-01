package com.duolingo.app.ui.curs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.adapter.CategoriesAdapter;
import com.duolingo.app.model.Category;
import com.duolingo.app.model.Course;
import com.duolingo.app.util.Data;
import com.duolingo.app.util.ExerciceActivity;

import java.util.ArrayList;

public class CursFragment extends Fragment implements CategoriesAdapter.OnNoteListener{

    private static ArrayList<String> listSelectedCourses = new ArrayList<String>();
    private ArrayList<Category> mkCategories = new ArrayList<Category>();

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){

        View view;
        view = inflater.inflate(R.layout.fragment_curs, container, false);

        checkCourses();

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        if (Data.userName.isEmpty()){
            tvTitle.setText("Buholingo!");
        }else{
            tvTitle.setText("Hola, "+Data.userName);

        }


        // Spinner con todos los cursos disponibles (Cuando haya que utilizar la BBDD en vez de
        // usar ArrayAdapter, habra que usar ClickAdapater [Esta en la guía oficial])

        final Spinner spnSelectedCourses  = (Spinner) view.findViewById(R.id.spnSelectedCourses);
        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, Data.listCourses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectedCourses .setAdapter(adapter);

        // Listener al seleccionar un ITEM en el Spinner spnTotalCourses
        spnSelectedCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Para conseguir el texto del item seleccionado
                TextView tv = (TextView)spnSelectedCourses.getSelectedView();
                String item = tv.getText().toString();

                // Checkea si el item se ha repetido
                boolean repeated = false;
                for (String s: listSelectedCourses){
                    if (s.equals(item)){
                        repeated = true;
                    }
                }

                if (!repeated){
                    // Si es la primera vez que se selecciona el curso, este se añade al ArrayList
                    // donde se guardan los cursos donde se ha inscrito el usuario
                    listSelectedCourses.add(item);
                    spnSelectedCourses.setAdapter(updateAdapter());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // RecyclerView
        // Se crea e instancia la RecyclerView, luego se crea su respectivo adapter
        initCategories();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        CategoriesAdapter listAdapter = new CategoriesAdapter(mkCategories, getActivity().getApplicationContext(), this);

        // Se encarga de establecer el layout de la RecyclerView de forma que vaya alternando entre 1
        // y 2 por fila con un GridLayout y un LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            public int getSpanSize(int position){
                return (position%3==0? 2:1);
            }

        });

        // Se le aplica el LayoutManager y su adapter al RecyclerView
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(listAdapter);

        return view;
    };

    @Override
    public void onNoteClick(int position) {
        mkCategories.get(position);
        Intent intent = new Intent(getContext(), ExerciceActivity.class);
        startActivity(intent);
    }

    private void initCategories(){

    }

    private ArrayAdapter<String> updateAdapter(){

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_spinner_item, listSelectedCourses);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter2;
    }

    private void checkCourses(){

        // checkCourses()
        // En caso de que la conexión con el servidor LipeRMI falle, este metodo instanciaría el
        // ArrayList pero sin valores. Permitiendo asi abrir la app en "MODO OFFLINE"

        if (Data.listCourses == null){
            System.out.println("ARRAY NULL");
            Data.listCourses = new ArrayList<>();
        }

    }

}
