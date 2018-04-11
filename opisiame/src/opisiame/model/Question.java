/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;

/**
 *
 * @author Sandratra
 */
public class Question {
    private Integer id;
    private String libelle;
    private Integer timer;
    private Integer quiz_id;
    private Integer sous_comp_id;
    private String sous_comp;
    private String url_img;
    private ArrayList<Reponse> reponses;
    private InputStream img_blob;
    
    public Question() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getTimer() {
        return timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }

    public String getSous_comp() {
        return sous_comp;
    }

    public void setSous_comp(String sous_comp) {
        this.sous_comp = sous_comp;
    }

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public Integer getSous_comp_id() {
        return sous_comp_id;
    }

    public void setSous_comp_id(Integer sous_comp_id) {
        this.sous_comp_id = sous_comp_id;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }
    
    public ArrayList<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(ArrayList<Reponse> reponses) {
        this.reponses = reponses;
    }

    public InputStream getImg_blob() {
        return img_blob;
    }

    public void setImg_blob(InputStream img_blob) {
        this.img_blob = img_blob;
    }
    
    @Override
    public String toString() {
        return getLibelle();
    }
}
