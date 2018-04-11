/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import opisiame.dao.Sous_comp_dao;

/**
 *
 * @author Audrey
 */
public class Competence {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nom = new SimpleStringProperty();
    private List<Sous_competence> list_sous_comp = new ArrayList<>();
    
    Sous_comp_dao sous_competence_dao = new Sous_comp_dao();

    //Constructeurs
    public Competence() {
    }

    public Competence(IntegerProperty ID, StringProperty NOM) {
        this.id = ID;
        this.nom = NOM;
    }

    //accesseurs
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
        list_sous_comp = sous_competence_dao.get_all_sous_competence(id);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public List<Sous_competence> getList_sous_comp() {
        return list_sous_comp;
    }

    public void setList_sous_comp(List<Sous_competence> list_sous_comp) {
        this.list_sous_comp = list_sous_comp;
    }

    @Override
    public String toString() {
        return getNom();
    }

}
