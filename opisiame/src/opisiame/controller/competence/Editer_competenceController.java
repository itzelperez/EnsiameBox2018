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
import javafx.scene.Parent;
import javafx.scene.Scene;
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
public class Editer_competenceController implements Initializable {

    @FXML
    private AnchorPane content;
    @FXML
    private TextField id;
    @FXML
    private TextField nom;
    @FXML
    private Label label_nom;

    private int comp_id;

    public void setComp_id(int compID) {
        this.comp_id = compID;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM competences WHERE Comp_id = ?");
            ps.setInt(1, comp_id);
            ResultSet rs = ps.executeQuery();

            //affichage des parametres de l'anim dans les textfield
            while (rs.next()) {
                id.setText(String.valueOf(rs.getInt(1)));
                nom.setText(rs.getString(2));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    public void btn_valider() throws IOException {

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

        if (c1 != 0) {
            label_nom.setText("nom non disponible");
            champ_ok = false;
        }

        //si tous les champs sont ok, ajout de l'animateur dans la bdd
        if (champ_ok == true) {
            label_nom.setText("");
            
            try {
                //met à jour la base de données
                Connection connection = Connection_db.getDatabase();
                PreparedStatement ps = connection.prepareStatement("UPDATE competences SET Competence = ? WHERE Comp_id = ?");
                ps.setString(1, nom);
                ps.setInt(2, comp_id);
                ps.executeUpdate();

                //ferme la fenêtre
                Stage stage = (Stage) content.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/competence/editer_competence.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }
    
    @FXML
    public void Annuler() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
       
    }

}
