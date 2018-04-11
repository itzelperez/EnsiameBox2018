/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.animateur;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import opisiame.database.Connection_db;
import opisiame.model.Animateur;
import session.Session;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Edit_animController implements Initializable {

    @FXML
    private AnchorPane content;
    @FXML
    private TextField id;
    @FXML
    private TextField nom;
    @FXML
    private TextField lg;
    @FXML
    private Label label_nom;
    @FXML
    private Label label_login;

    
    private int anim_id;

    public void setAnim_id(int animID) {
        this.anim_id = animID;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM animateur WHERE Anim_id = ?");
            ps.setInt(1, anim_id);
            ResultSet rs = ps.executeQuery();

            //affichage des parametres de l'anim dans les textfield
            while (rs.next()) {
                id.setText(String.valueOf(rs.getInt(1)));
                nom.setText(rs.getString(2));
                lg.setText(rs.getString(3));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    @FXML
    public void btn_valider() throws IOException {

        try {
            
            label_nom.setText("");
            label_login.setText("");

            String nom = this.nom.getText();
            String lg = this.lg.getText();
            Boolean champ_ok = true;
            int c1 = 0;
            int c2 = 0;
            int c3=0;

            //verif que le nom est bien rempli
            if (nom.equals("")) {
                label_nom.setText("*");
                champ_ok = false;
            }
            

            //verif que le login est bien rempli
            if (lg.equals("")) {
                label_login.setText("*");
                champ_ok = false;
            }
            

            //verif que le login n'est pas deja utilise
            try {
                Connection connection = Connection_db.getDatabase();

                PreparedStatement ps1 = connection.prepareStatement("SELECT COUNT(*) AS total FROM enseignant WHERE Ens_login = ?");
                ps1.setString(1, lg);
                ResultSet rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    c1 = rs1.getInt("total");
                }

                PreparedStatement ps2 = connection.prepareStatement("SELECT COUNT(*) AS total FROM administrateur WHERE Admin_login = ?");
                ps2.setString(1, lg);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    c2 = rs2.getInt("total");
                }
                
                PreparedStatement ps3 = connection.prepareStatement("SELECT COUNT(*) AS total FROM animateur WHERE Anim_login = ?");
                ps3.setString(1, lg);
                ResultSet rs3 = ps3.executeQuery();
                while (rs3.next()) {
                    c3 = rs3.getInt("total");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            if (c1 != 0 || c2 != 0 || c3!=0) {
                label_login.setText("déjà pris");
                champ_ok = false;
            }
            
            
            
            //si tous les champs sont ok, ajout de l'animateur dans la bdd
        if (champ_ok == true) {
            label_nom.setText("");
            label_login.setText("");
            //met à jour la base de données
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("UPDATE animateur SET Anim_nom = ?, Anim_login = ? WHERE Anim_id = ?");
            ps.setString(1, nom);
            ps.setString(2, lg);
            ps.setInt(3, anim_id);
            ps.executeUpdate();

            //ferme la fenêtre
            Stage stage = (Stage) content.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/animateur/edit_anim.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.close();}

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
