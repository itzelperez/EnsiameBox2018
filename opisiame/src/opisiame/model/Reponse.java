/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

/**
 *
 * @author Sandratra
 */
public class Reponse {
    private Integer id;
    private String libelle;
    private Integer is_bonne_reponse;
    private Integer quest_id;

    public Reponse() {
    }

    public Reponse(String libelle, Integer is_bonne_reponse, Integer quest_id) {
        this.libelle = libelle;
        this.is_bonne_reponse = is_bonne_reponse;
        this.quest_id = quest_id;
    }

    public Reponse(Integer id, String libelle, Integer is_bonne_reponse, Integer quest_id) {
        this.id = id;
        this.libelle = libelle;
        this.is_bonne_reponse = is_bonne_reponse;
        this.quest_id = quest_id;
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

    public Integer getIs_bonne_reponse() {
        return is_bonne_reponse;
    }

    public void setIs_bonne_reponse(Integer is_bonne_reponse) {
        this.is_bonne_reponse = is_bonne_reponse;
    }

    public Integer getQuest_id() {
        return quest_id;
    }

    public void setQuest_id(Integer quest_id) {
        this.quest_id = quest_id;
    }

//    public void setLibelle(char nom_question) {
//        this.libelle=nom_question.;
//    }
    
    
}
