/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Audrey
 */
public class Resultat_eleve_comp {

    private StringProperty nom_comp = new SimpleStringProperty();
    private FloatProperty pourcentage = new SimpleFloatProperty();
    private IntegerProperty nbre_questions = new SimpleIntegerProperty();
    

    public Resultat_eleve_comp() {
    }
    
    public Resultat_eleve_comp(StringProperty nom_comp, FloatProperty pourcentage, IntegerProperty nbre_questions) {
        this.nom_comp = nom_comp;
        this.pourcentage = pourcentage;
        this.nbre_questions = nbre_questions;
    }
    
    public Resultat_eleve_comp(String nom_comp, float pourcentage, int nbre_questions) {
        this.nom_comp.set(nom_comp);
        this.pourcentage.set(pourcentage);
        this.nbre_questions.set(nbre_questions);
    }

    public Integer getNbre_questions() {
        return this.nbre_questions.get();
    }

    public void setNbre_questions(Integer nbre_questions) {
        this.nbre_questions.set(nbre_questions);
    }

    public String getNom_comp() {
        return this.nom_comp.get();
    }

    public void setNom_comp(String nom_comp) {
        this.nom_comp.set(nom_comp);
    }

    public Float getPourcentage() {
        return this.pourcentage.get();
    }

    public void setPourcentage(Float pourcentage) {
        this.pourcentage.set(pourcentage);
    }

}
