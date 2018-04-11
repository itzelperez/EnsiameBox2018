/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Sandratra
 */
public class Reponse_question {
    private StringProperty question = new SimpleStringProperty();
    private Double pourcentage_rep_a;
    private Double pourcentage_rep_b ;
    private Double pourcentage_rep_c;
    private Double pourcentage_rep_d ;
    private Double pourcentage;
    private String bonne_rep;
    
    private SimpleStringProperty str_pourcentage_rep_a = new SimpleStringProperty();
    private SimpleStringProperty str_pourcentage_rep_b = new SimpleStringProperty();
    private SimpleStringProperty str_pourcentage_rep_c = new SimpleStringProperty();
    private SimpleStringProperty str_pourcentage_rep_d = new SimpleStringProperty();
    private SimpleStringProperty str_pourcentage = new SimpleStringProperty();

    public String getStr_pourcentage_rep_a() {
        return str_pourcentage_rep_a.get();
    }

    public void setStr_pourcentage_rep_a(String str_pourcentage_rep_a) {
        this.str_pourcentage_rep_a.set(str_pourcentage_rep_a);
    }

    public String getStr_pourcentage_rep_b() {
        return str_pourcentage_rep_b.get();
    }

    public void setStr_pourcentage_rep_b(String str_pourcentage_rep_b) {
        this.str_pourcentage_rep_b.set(str_pourcentage_rep_b);
    }

    public String getStr_pourcentage_rep_c() {
        return str_pourcentage_rep_c.get();
    }

    public void setStr_pourcentage_rep_c(String str_pourcentage_rep_c) {
        this.str_pourcentage_rep_c.set(str_pourcentage_rep_c);
    }

    public String getStr_pourcentage_rep_d() {
        return str_pourcentage_rep_d.get();
    }

    public void setStr_pourcentage_rep_d(String str_pourcentage_rep_d) {
        this.str_pourcentage_rep_d.set(str_pourcentage_rep_d);
    }

    public String getStr_pourcentage() {
        return str_pourcentage.get();
    }

    public void setStr_pourcentage(String str_pourcentage) {
        this.str_pourcentage.set(str_pourcentage);
    }
    

    public String getQuestion() {
        return question.get();
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public Double getPourcentage_rep_a() {
        return pourcentage_rep_a;
    }

    public void setPourcentage_rep_a(Double pourcentage_rep_a) {
        this.pourcentage_rep_a = pourcentage_rep_a;
        this.setStr_pourcentage_rep_a(pourcentage_rep_a + " %");
    }

    public Double getPourcentage_rep_b() {
        return pourcentage_rep_b;
    }

    public void setPourcentage_rep_b(Double pourcentage_rep_b) {
        this.pourcentage_rep_b = pourcentage_rep_b;
        this.setStr_pourcentage_rep_b(pourcentage_rep_b + " %");
    }

    public Double getPourcentage_rep_c() {
        return pourcentage_rep_c;
    }

    public void setPourcentage_rep_c(Double pourcentage_rep_c) {
        this.pourcentage_rep_c = pourcentage_rep_c;
        this.setStr_pourcentage_rep_c(pourcentage_rep_c + " %");
    }

    public Double getPourcentage_rep_d() {
        return pourcentage_rep_d;
    }

    public void setPourcentage_rep_d(Double pourcentage_rep_d) {
        this.pourcentage_rep_d = pourcentage_rep_d;
        this.setStr_pourcentage_rep_d(pourcentage_rep_d + " %");
    }

    public Double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(Double pourcentage) {
        this.pourcentage = pourcentage;
        this.setStr_pourcentage("( "+ bonne_rep +" ) "+pourcentage + " %");
    }

    public String getBonne_rep() {
        return bonne_rep;
    }

    public void setBonne_rep(String bonne_rep) {
        this.bonne_rep = bonne_rep;
    }   
}
