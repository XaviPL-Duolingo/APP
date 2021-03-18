package com.duolingo.app.ui.curs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.duolingo.app.util.ServerConn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CursFragment extends Fragment implements CategoriesAdapter.OnNoteListener{

    private static ArrayList<String> listSelectedCourses = new ArrayList<String>();
    private List<Category> mkCategories = new ArrayList<Category>();
    private Spinner spnSelectedCourses;
    private RecyclerView recyclerView;
    private boolean isEmpty = false;

    public int idCourse;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){

        View view;
        view = inflater.inflate(R.layout.fragment_curs, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        if (!Data.hasConnection){
            tvTitle.setText("Buholingo!");
        }else{
            tvTitle.setText("Hola, "+Data.userData.getUsername());

        }

        try {
            ServerConn serverConn = (ServerConn) new ServerConn("getAllCoursesByID", Data.KEYID_LANG);
            Data.listCourses = (List<Course>) serverConn.returnObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        checkCourses();

        // Spinner con todos los cursos disponibles (Cuando haya que utilizar la BBDD en vez de
        // usar ArrayAdapter, habra que usar ClickAdapater [Esta en la guía oficial])

        spnSelectedCourses  = (Spinner) view.findViewById(R.id.spnSelectedCourses);
        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, Data.listCourses){
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextAppearance(R.style.SpinnerStyle);

                return v;

            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectedCourses.setAdapter(adapter);
        updateCategories();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userData", this.getActivity().getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        spnSelectedCourses.setSelection(Data.KEYID_COURSE);
        spnSelectedCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Data.KEYID_COURSE = parent.getSelectedItemPosition();
                editor.putInt("KEYID_LANG", Data.KEYID_LANG);
                editor.apply();
                updateCategories();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // RecyclerView
        // Se crea e instancia la RecyclerView, luego se crea su respectivo adapter
        recyclerView = view.findViewById(R.id.recyclerView);
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

        // onNoteClick()
        // Al pulsar una CATEGORY se ejecuta un Intent para ir a los EXERCICES de esa CATEGORY

        Intent intent = new Intent(getContext(), ExerciceActivity.class);
        intent.putExtra("category", mkCategories.get(position));
        startActivity(intent);
    }

    private void checkCourses(){

        // checkCourses()
        // En caso de que la conexión con el servidor LipeRMI falle, este metodo instanciaría el
        // ArrayList pero sin valores. Permitiendo asi abrir la app en "MODO OFFLINE"

        if (Data.listCourses == null || Data.listCourses.size() == 0){
            System.out.println("ARRAY NULL");
            Data.listCourses = new ArrayList<>();
            isEmpty = true;
        }else {
            isEmpty = false;
        }

    }

    private void updateCategories(){

        // updateCategories()
        // Limpia la RecyclerView y el ArrayList de CATEGORY, una vez limpios llama al servidor
        // para obtener todas las CATEGORY que hayan en el COURSE seleccionado y las guarda en el
        // ArrayList, luego se actualiza la RecyclerView.

        if (recyclerView != null)
            recyclerView.removeAllViews();

        mkCategories.clear();

        if (!isEmpty){
            Course courseSelected = (Course) spnSelectedCourses.getSelectedItem();
            idCourse = courseSelected.getIdCourse();

            try {
                ServerConn serverConn = (ServerConn) new ServerConn("getAllCategoriesByID", idCourse);
                List<Category> categoryList = (List<Category>) serverConn.returnObject();
                mkCategories.addAll(categoryList);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
