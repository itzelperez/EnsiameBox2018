/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import java.sql.Date;
import javafx.beans.property.*;

/**
 *
 * @author Sandratra
 */
public class Quiz {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nom = new SimpleStringProperty();
    private StringProperty date_creation = new SimpleStringProperty();
    private IntegerProperty timer = new SimpleIntegerProperty();
    private IntegerProperty anim_id = new SimpleIntegerProperty();
    private String date_participation;

    public Integer getId() {
        return id.get();
    }

    public Quiz() {
    }

    public Quiz(IntegerProperty id, StringProperty nom, StringProperty date_creation, IntegerProperty timer, IntegerProperty anim_id) {
        this.id = id;
        this.nom = nom;
        this.date_creation = date_creation;
        this.timer = timer;
        this.anim_id = anim_id;
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getDate_creation() {
        return date_creation.get();
    }

    public void setDate_creation(String date_creation) {
        this.date_creation.set(date_creation);
    }

    public Integer getTimer() {
        return timer.get();
    }

    public void setTimer(Integer timer) {
        this.timer.set(timer);
    }

    public Integer getAnim_id() {
        return anim_id.get();
    }

    public void setAnim_id(Integer anim_id) {
        this.anim_id.set(anim_id);
    }
    

    public void setDate_participation(String date){
        date_participation=date;
    }
    
    public String getDate_participation(){
        return date_participation;
    }

    @Override
    public String toString() {
        return getNom();
    }
}
