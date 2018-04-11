/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Audrey
 */
public class Reponse_eleve_quiz {

    private StringProperty lib_ques = new SimpleStringProperty();
    private IntegerProperty num_ques = new SimpleIntegerProperty();
    private StringProperty rep_quiz = new SimpleStringProperty();
    private StringProperty rep_eleve = new SimpleStringProperty();

    public Reponse_eleve_quiz() {
    }

    public Reponse_eleve_quiz(StringProperty lib_ques, IntegerProperty num, StringProperty rep_q, StringProperty rep_e) {
        this.lib_ques = lib_ques;
        this.num_ques = num;
        this.rep_quiz = rep_q;
        this.rep_eleve = rep_e;
    }

    public Reponse_eleve_quiz(String lib, Integer num, String rep_q, String rep_e) {
        this.lib_ques.set(lib);
        this.num_ques.set(num);
        this.rep_quiz.set(rep_q);
        this.rep_eleve.set(rep_e);
    }

    public String getLib_ques() {
        return this.lib_ques.get();
    }

    public void setLib_ques(String lib_ques) {
        this.lib_ques.set(lib_ques);
    }

    public Integer getNum_ques() {
        return num_ques.get();
    }

    public void setNum_ques(Integer num_ques) {
        this.num_ques.set(num_ques);
    }

    public String getRep_quiz() {
        return rep_quiz.get();
    }

    public void setRep_quiz(String rep_quiz) {
        this.rep_quiz.set(rep_quiz);
    }

    public String getRep_eleve() {
        return rep_eleve.get();
    }

    public void setRep_eleve(String rep_eleve) {
        this.rep_eleve.set(rep_eleve);
    }

}
