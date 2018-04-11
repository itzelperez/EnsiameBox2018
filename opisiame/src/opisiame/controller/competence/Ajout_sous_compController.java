/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.competence;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import opisiame.database.Connection_db;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Ajout_sous_compController implements Initializable {

    //injection des elements graphiques
    @FXML
    private AnchorPane content;
    @FXML
    private TextField nom_comp;
    @FXML
    private TextField nom_sous_comp;
    @FXML
    private Label label_sous_comp;

    //récupère l'id de la compétence concernée
    private Integer id_comp;   

    public int getId_comp() {
        return id_comp;
    }

    public void setId_comp(int id_comp) {
        this.id_comp = id_comp;
        set_competence_name();
    }
    
    public void set_competence_name(){
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("SELECT Competence FROM competences WHERE Comp_id = ?");
            ps.setInt(1, id_comp);
            ResultSet rs = ps.executeQuery();

            //affichage des parametres de l'anim dans les textfield
            while (rs.next()) {
                nom_comp.setText(rs.getString(1));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
    }

    @FXML
    public void btn_valider() {

        label_sous_comp.setText("");
        String nom = this.nom_sous_comp.getText();
        Boolean champ_ok = true;
        int c1 = 0;

        //verif que le nom est bien rempli
        if (nom.equals("")) {
            label_sous_comp.setText("champ requis");
            champ_ok = false;
        }

        //verif que le nom de la sous compétence n'est pas deja utilise
        try {
            Connection connection = Connection_db.getDatabase();

            PreparedStatement ps1 = connection.prepareStatement("SELECT COUNT(*) AS total FROM souscompetence WHERE SousCompetence = ? AND Comp_id = ?");
            ps1.setString(1, nom);
            ps1.setInt(2, id_comp);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                c1 = rs1.getInt("total");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (c1 != 0) {
            label_sous_comp.setText("Nom déjà utilisé");
            champ_ok = false;
        }

        //si tous les champs sont ok, ajout de l'animateur dans la bdd
        if (champ_ok == true) {
            label_sous_comp.setText("");
            insert_new_sous_comp(nom);

            //ou ouvre la fenêtre liste_profs_admin
            //ouverture fenêtre menu_ens            
            Stage stage = (Stage) content.getScene().getWindow();
            stage.close();

        }

    }

    public void insert_new_sous_comp(String nom) {
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO souscompetence (SousCompetence, Comp_id) VALUES (?, ?)");
            ps.setString(1, nom);
            ps.setInt(2, id_comp);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    @FXML
    public void Annuler() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
       
    }

}
