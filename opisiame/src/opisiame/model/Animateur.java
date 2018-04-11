/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import javafx.beans.property.*;

/**
 *
 * @author Audrey
 */
public class Animateur {
    
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nom = new SimpleStringProperty();
    private StringProperty lg = new SimpleStringProperty();
    private StringProperty passwd = new SimpleStringProperty();

    //Constructeurs
    public Animateur() {
    }

    public Animateur(IntegerProperty ID, StringProperty NOM, StringProperty LOGIN, StringProperty PASSWD) {
        this.id = ID;
        this.nom = NOM;
        this.lg = LOGIN;
        this.passwd = PASSWD;
    }

    //accesseurs
    public Integer getId() {
        return id.get();
    }    
    public void setId(Integer id) {
        this.id.set(id);
    }
    
    public String getNom(){
        return nom.get();
    }    
    public void setNom(String nom) {
        this.nom.set(nom);
    }
    
    public String getLg(){
        return lg.get();
    }
    public void setLg(String lg) {
        this.lg.set(lg);
    }
    
    public String getPasswd(){
        return passwd.get();
    }
    public void setPasswd(String Passwd) {
        this.passwd.set(Passwd);
    }

    
    
}
