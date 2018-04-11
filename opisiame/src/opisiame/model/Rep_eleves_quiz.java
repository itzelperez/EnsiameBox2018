/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import javafx.beans.property.IntegerProperty;

/**
 *
 * @author Audrey
 */
public class Rep_eleves_quiz {

    private int num_eleve;
    private Double note_eleve;
    private Double Pourcent_eleve;
    private String nom_eleve;
    private String prenom_eleve;

    public String getNom_eleve() {
        return nom_eleve;
    }

    public void setNom_eleve(String nom_eleve) {
        this.nom_eleve = nom_eleve;
    }

    public String getPrenom_eleve() {
        return prenom_eleve;
    }

    public void setPrenom_eleve(String prenom_eleve) {
        this.prenom_eleve = prenom_eleve;
    }

//    public Rep_eleves_quiz(IntegerProperty nume_eleve, Double note_eleve, Double Pourcent_eleve) {
//        this.num_eleve = nume_eleve;
//        this.note_eleve = note_eleve;
//        this.Pourcent_eleve = Pourcent_eleve;
//    }
    
    public Rep_eleves_quiz(int num_eleve, Double note_eleve, Double Pourcent_eleve) {
        this.num_eleve = num_eleve;
        this.note_eleve = note_eleve;
        this.Pourcent_eleve = Pourcent_eleve;
    }

    public Rep_eleves_quiz() {
    }

    public Integer getNum_eleve() {
        return this.num_eleve;
    }

    public void setNum_eleve(int num_eleve) {
        this.num_eleve = num_eleve;
    }

    public Double getNote_eleve() {
        return note_eleve;
    }

    public void setNote_eleve(Double note_eleve) {
        this.note_eleve = note_eleve;
    }

    public Double getPourcent_eleve() {
        return Pourcent_eleve;
    }

    public void setPourcent_eleve(Double Pourcent_eleve) {
        this.Pourcent_eleve = Pourcent_eleve;
    }

}
