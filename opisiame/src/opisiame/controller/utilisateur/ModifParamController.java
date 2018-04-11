/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.utilisateur;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import opisiame.database.Connection_db;
import session.Session;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class ModifParamController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    //injection des éléments graphiques
    @FXML
    private AnchorPane content;
    @FXML
    private TextField AncienMDP;
    @FXML
    private PasswordField NouveauMDP;
    @FXML
    private PasswordField ConfirmMDP;
    @FXML
    private Label Msg;

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

    //verification correspondance AncienMDP / login
    private int lecture_admin() throws SQLException {
        final String log = session.Session.getLog();
        final String pass = md5(AncienMDP.getText());
        final String type = session.Session.getType();
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int ok = 0;

        //vérification du login
        //connection avec la base de donnée
        Connection database = Connection_db.getDatabase();
        PreparedStatement pslog = database.prepareStatement("SELECT COUNT(*) AS total1 FROM administrateur WHERE Admin_login = ? and Admin_mdp = ?");
        pslog.setString(1, log);
        pslog.setString(2, pass);
        ResultSet logres = pslog.executeQuery();
        while (logres.next()) {
            count1 = logres.getInt("total1");
        }
        if (count1 == 1) {
            ok = 1;
        }

        PreparedStatement pslog2 = database.prepareStatement("SELECT COUNT(*) AS total2 FROM enseignant WHERE Ens_login = ? and Ens_mdp = ?");
        pslog2.setString(1, log);
        pslog2.setString(2, pass);
        ResultSet logres2 = pslog2.executeQuery();
        while (logres2.next()) {
            count2 = logres2.getInt("total2");
        }
        if (count2 == 1) {
            ok = 2;
        }

        PreparedStatement pslog3 = database.prepareStatement("SELECT COUNT(*) AS total3 FROM animateur WHERE Anim_login = ? and Anim_mdp = ?");
        pslog3.setString(1, log);
        pslog3.setString(2, pass);
        ResultSet logres3 = pslog3.executeQuery();
        while (logres3.next()) {
            count3 = logres3.getInt("total3");
        }
        if (count3 == 1) {
            ok = 3;
        }

        return ok;
    }

    @FXML
    public void Modifier_MDP() throws IOException, SQLException {

        if (lecture_admin() == 1) {

            if (NouveauMDP.getText().equals(ConfirmMDP.getText())) {
                //Msg.setText("erhjtfj,");
                //modification du mdp dans la base de données
                final String log = session.Session.getLog();
                final String pass = NouveauMDP.getText();
                Connection database = Connection_db.getDatabase();
                PreparedStatement pslog = database.prepareStatement("UPDATE administrateur SET Admin_mdp = ? WHERE Admin_login = ?;");
                pslog.setString(1, md5(pass));
                pslog.setString(2, log);
                pslog.execute();

                //ouverture de la fenêtre menu_anim
                Session login = new Session(log, "admin");
                Stage stage = (Stage) content.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_admin.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.show();

            } else {
                Msg.setText("Le nouveau mot de passe et sa confirmation ne correspondent pas.");
            }

        } else if (lecture_admin() == 2) {

            if (NouveauMDP.getText().equals(ConfirmMDP.getText())) {
                //Msg.setText("erhjtfj,");
                //modification du mdp dans la base de données
                final String log = session.Session.getLog();
                final String pass = NouveauMDP.getText();
                Connection database = Connection_db.getDatabase();
                PreparedStatement pslog = database.prepareStatement("UPDATE enseignant SET Ens_mdp = ? WHERE Ens_login = ?;");
                pslog.setString(1, md5(pass));
                pslog.setString(2, log);
                pslog.execute();

                //ouverture de la fenêtre menu_anim
                Session login = new Session(log, "ens");
                Stage stage = (Stage) content.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_ens.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.show();

            } else {
                Msg.setText("Le nouveau mot de passe et sa confirmation ne correspondent pas.");
            }

        }

        if (lecture_admin() == 3) {

            if (NouveauMDP.getText().equals(ConfirmMDP.getText())) {
                //Msg.setText("erhjtfj,");
                //modification du mdp dans la base de données
                final String log = session.Session.getLog();
                final String pass = NouveauMDP.getText();
                Connection database = Connection_db.getDatabase();
                PreparedStatement pslog = database.prepareStatement("UPDATE animateur SET Anim_mdp = ? WHERE Anim_login = ?;");
                pslog.setString(1, md5(pass));
                pslog.setString(2, log);
                pslog.execute();

                //ouverture de la fenêtre menu_anim
                Session login = new Session(log, "anim");
                Stage stage = (Stage) content.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_anim.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.show();

            } else {
                Msg.setText("Le nouveau mot de passe et sa confirmation ne correspondent pas.");
            }

        } else {
            Msg.setText("L'ancien mot de passe ne correspond pas.");
        }
    }

    @FXML
    public void ClicImageOnOff() throws IOException {
        //remise à zéro des variables d'identification (login + mdp)
        session.Session.Logout();
        //ouverture fenêtre interface_authentification
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonRetour() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();

        String type = Session.getType();
        if (type.equals("anim")) {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_anim.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } else if (type.equals("admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_admin.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } else if (type.equals("ens")) {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_ens.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        }
    }

    @FXML
    public void Annuler() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();

        String type = Session.getType();
        if (type.equals("anim")) {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_anim.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } else if (type.equals("admin")) {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_admin.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } else if (type.equals("ens")) {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_ens.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        }
    }

}
