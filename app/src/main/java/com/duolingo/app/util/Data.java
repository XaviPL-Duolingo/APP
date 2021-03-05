package com.duolingo.app.util;

import com.duolingo.app.model.Course;
import com.duolingo.app.model.Language;

import java.util.ArrayList;
import java.util.List;

public class Data {

    // Variables estaticas que se comparten entre toda la APP

    public static String serverIP = "192.168.1.212";
    public static int selectedCourse;

    public static String userName = "Xavi"; // Borrar
    public static int mkMoney = 0;      // Borrar
    public static int mkPoints = 0;     // Borrar

    public static int KEYID_LANG;
    public static String KEYID_USER = "";
    public static boolean hasConnection = false;

    public static List<Course> listCourses;

}
