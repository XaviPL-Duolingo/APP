package com.duolingo.app.util;

import com.duolingo.app.model.Course;
import com.duolingo.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class Data {

    // Variables estaticas que se comparten entre toda la APP

    public static String serverIP = "192.168.1.212";
    public static boolean hasConnection = false;

    public static String KEYID_USER = "";
    public static int KEYID_LANG;
    public static int KEYID_COURSE;

    public static User userData = null;

    public static List<Course> listCourses;

}
