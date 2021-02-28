package com.duolingo.app.util;

import java.util.ArrayList;

public interface ITestService {

    // LipeRMI - Obtener la lista de CURSOS disponibles [APP]
    public ArrayList<String> getResponse(short originLang);

    public ArrayList<String> getAllLanguages();


}
