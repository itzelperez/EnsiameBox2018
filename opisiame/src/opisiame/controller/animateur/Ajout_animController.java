/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.animateur;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.*;
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
public class Ajout_animController implements Initializable {

    //injection des elements graphiques
    @FXML
    private AnchorPane content;
    @FXML
    private TextField nom;
    @FXML
    private TextField lg;
    @FXML
    private TextField mdp;
    @FXML
    private Label label_nom;
    @FXML
    private Label label_lg;
    @FXML
    private Label label_mdp;

   public static String md5(String input) {

        String md5 = null;

        if (null == input) {
            return null;
        }

        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void btn_valider() {

        label_nom.setText("");
        label_lg.setText("");
        label_mdp.setText("");

        String nom = this.nom.getText();
        String lg = this.lg.getText();
        String mdp = this.mdp.getText();
        Boolean champ_ok = true;
        int c1 = 0;
        int c2 = 0;
        int c3 = 0;

        //verif que le nom est bien rempli
        if (nom.equals("")) {
            label_nom.setText("champ requis");
            champ_ok = false;
        }

        //verif que le login est bien rempli
        if (lg.equals("")) {
            label_lg.setText("champ requis");
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
        if (c1 != 0 || c2 != 0 || c3 !=0) {
            label_lg.setText("login non disponible");
            champ_ok = false;
        }

        //verif que le mdp est bien rempli
        if (mdp.equals("")) {
            label_mdp.setText("champ requis");
            champ_ok = false;
        }

        //si tous les champs sont ok, ajout de l'animateur dans la bdd
        if (champ_ok == true) {
            label_nom.setText("");
            label_lg.setText("");
            label_mdp.setText("");
            insert_new_anim(nom, lg, mdp);

            //ou ouvre la fenêtre liste_profs_admin
            //ouverture fenêtre menu_ens            
            Stage stage = (Stage) content.getScene().getWindow();
            stage.close();

        }

    }

    public void insert_new_anim(String nom, String lg, String mdp) {
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO animateur (Anim_nom,Anim_login, Anim_mdp) VALUES (?,?,?)");
            ps.setString(1, nom);
            ps.setString(2, lg);
            ps.setString(3, md5(mdp));
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
