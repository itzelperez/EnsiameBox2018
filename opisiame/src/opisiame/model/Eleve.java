/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import javafx.beans.property.*;

/**
 *
 * @author clement
 */
public class Eleve {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty prenom = new SimpleStringProperty();
    private StringProperty nom = new SimpleStringProperty();
    private StringProperty filiere = new SimpleStringProperty();
    private IntegerProperty annee = new SimpleIntegerProperty();
    private StringProperty Adresse_mac_tel = new SimpleStringProperty();    
    private StringProperty str_Adresse_mac_tel = new SimpleStringProperty();
    private Integer part_id;

    public Integer getPart_id() {
        return part_id;
    }

    public void setPart_id(Integer part_id) {
        this.part_id = part_id;
    }

    //Constructeur
    public Eleve(IntegerProperty ID, StringProperty PRENOM, StringProperty NOM, StringProperty FILIERE, IntegerProperty ANNEE) {
        id = ID;
        prenom = PRENOM;
        nom = NOM;
        filiere = FILIERE;
        annee = ANNEE;
    }

    public Eleve() {
    }

    public String getAdresse_mac_tel() {
        return Adresse_mac_tel.get();
    }

    public void setAdresse_mac_tel(String Adresse_mac_tel) {
        this.Adresse_mac_tel.set(Adresse_mac_tel);
    }

    public String getStr_Adresse_mac_tel() {
        return str_Adresse_mac_tel.get();
    }

    public void setStr_Adresse_mac_tel(String str_Adresse_mac_tel) {
        this.str_Adresse_mac_tel.set(str_Adresse_mac_tel);
    }
    

    // Pour retourner les attributs
    public Integer getId() {
        return id.get();
    }

    public String getPrenom() {
        return prenom.get();
    }

    public String getNom() {
        return nom.get();
    }

    public String getFiliere() {
        return filiere.get();
    }

    public Integer getAnnee() {
        return annee.get();
    }

    //Pour écrire dans les attributs
    public void setId(Integer ID) {
        this.id.set(ID);
    }

    public void setPrenom(String PRENOM) {
        this.prenom.set(PRENOM);
    }

    public void setNom(String NOM) {
        this.nom.set(NOM);
    }
    
    public void setFiliere(String FILIERE){
        this.filiere.set(FILIERE);
    }
    
    public void setAnnee (Integer ANNEE){
        this.annee.set(ANNEE);
    }
    
    @Override
    public String toString() {
        return getNom()+" "+getPrenom()+" "+getId();
    }
}
