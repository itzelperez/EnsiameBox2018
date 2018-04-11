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
public class Ajout_compController implements Initializable {
    
    //injection des elements graphiques
    @FXML
    private AnchorPane content;
    @FXML
    private TextField nom;
    @FXML
    private Label label_nom;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    public void btn_valider() {        
        
        label_nom.setText("");
        String nom = this.nom.getText();
        Boolean champ_ok = true;
        int c1 = 0;

        //verif que le nom est bien rempli
        if (nom.equals("")) {
            label_nom.setText("champ requis");
            champ_ok = false;
        }

        //verif que le nom n'est pas deja utilise
        try {
            Connection connection = Connection_db.getDatabase();

            PreparedStatement ps1 = connection.prepareStatement("SELECT COUNT(*) AS total FROM competences WHERE Competence = ?");
            ps1.setString(1, nom);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                c1 = rs1.getInt("total");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (c1 != 0 ) {
            label_nom.setText("Nom déjà utilisé");
            champ_ok = false;
        }


        //si tous les champs sont ok, ajout de l'animateur dans la bdd
        if (champ_ok == true) {
            label_nom.setText("");
            insert_new_comp(nom);

            //ou ouvre la fenêtre liste_profs_admin
            //ouverture fenêtre menu_ens            
            Stage stage = (Stage) content.getScene().getWindow();
            stage.close();
            

        }

    }

    public void insert_new_comp(String nom) {
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO competences (Competence) VALUES (?)");
            ps.setString(1, nom);
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
