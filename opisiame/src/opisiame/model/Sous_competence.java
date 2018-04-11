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
public class Sous_competence {
    Integer id;
    String libelle;
    Integer comp_id;

    public Sous_competence() {
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

    public Integer getComp_id() {
        return comp_id;
    }

    public void setComp_id(Integer comp_id) {
        this.comp_id = comp_id;
    }
    
    
    @Override
    public String toString() {
        return getLibelle();
    }
}
