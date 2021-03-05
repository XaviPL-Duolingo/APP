package com.duolingo.app.util;

import com.duolingo.app.model.Course;
import com.duolingo.app.model.Language;
import com.duolingo.app.model.Rank;

import java.util.ArrayList;
import java.util.List;

public class Data {

    // Variables estaticas que se comparten entre toda la APP

    public static boolean hasConnection = false;

    public static String serverIP = "192.168.1.212";
    public static String KEYID_USER = "";
    public static int KEYID_LANG;
    public static int selectedCourse;

    public static String userName = "";
    public static int userXP = 0;
    public static int userMoney = 0;
    public static int userElo = 0;
    public static Rank userRank = null;

    public static List<Course> listCourses;

}
