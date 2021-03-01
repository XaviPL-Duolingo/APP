package com.duolingo.app.util;

import com.duolingo.app.model.Course;

import java.util.ArrayList;
import java.util.List;

public interface IServerRMI {

    // LipeRMI - Obtener la lista de CURSOS disponibles [APP]
    public List<Course> getAllCoursesByID(int idOriginLang);

}
