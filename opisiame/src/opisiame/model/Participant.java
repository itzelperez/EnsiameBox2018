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
public class Participant {
    Integer Part_id;
    String Part_nom;
    String Part_prenom;
    Integer Filiere_id;

    public Integer getPart_id() {
        return Part_id;
    }

    public void setPart_id(Integer Part_id) {
        this.Part_id = Part_id;
    }

    public String getPart_nom() {
        return Part_nom;
    }

    public void setPart_nom(String Part_nom) {
        this.Part_nom = Part_nom;
    }

    public String getPart_prenom() {
        return Part_prenom;
    }

    public void setPart_prenom(String Part_prenom) {
        this.Part_prenom = Part_prenom;
    }

    public Integer getFiliere_id() {
        return Filiere_id;
    }

    public void setFiliere_id(Integer Filiere_id) {
        this.Filiere_id = Filiere_id;
    }
    
    
}
