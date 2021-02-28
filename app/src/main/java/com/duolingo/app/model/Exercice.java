package com.duolingo.app.model;

import java.io.Serializable;

public class Exercice implements Serializable {

    int categoryID, typeExercice;
    String exStatement;
    String word1, word2, word3, word4, word5, word6;

    // Constructor TIPUS_TEST
    public Exercice(int categoryID, int typeExercice, String exStatement, String word1, String word2, String word3) {
        this.categoryID = categoryID;
        this.typeExercice = typeExercice;
        this.exStatement = exStatement;
        this.word1 = word1;
        this.word2 = word2;
        this.word3 = word3;
    }

    // Constructor OPEN_ANSWER_TRANSLATE
    public Exercice(int categoryID, int typeExercice, String exStatement, String word1) {
        this.categoryID = categoryID;
        this.typeExercice = typeExercice;
        this.exStatement = exStatement;
        this.word1 = word1;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getTypeExercice() {
        return typeExercice;
    }

    public void setTypeExercice(int typeExercice) {
        this.typeExercice = typeExercice;
    }

    public String getExStatement() {
        return exStatement;
    }

    public void setExStatement(String exStatement) {
        this.exStatement = exStatement;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWord3() {
        return word3;
    }

    public void setWord3(String word3) {
        this.word3 = word3;
    }

    public String getWord4() {
        return word4;
    }

    public void setWord4(String word4) {
        this.word4 = word4;
    }

    public String getWord5() {
        return word5;
    }

    public void setWord5(String word5) {
        this.word5 = word5;
    }

    public String getWord6() {
        return word6;
    }

    public void setWord6(String word6) {
        this.word6 = word6;
    }
}
