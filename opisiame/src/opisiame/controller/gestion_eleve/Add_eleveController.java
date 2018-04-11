/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_eleve;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import opisiame.database.*;

/**
 * FXML Controller class
 *
 * @author clement
 */
public class Add_eleveController implements Initializable {

    @FXML
    private AnchorPane content;
    @FXML
    private TextField Edit_Nom;
    @FXML
    private TextField Edit_Prenom;
    @FXML
    private ComboBox Choix_Flilere;
    @FXML
    private ComboBox Choix_annee;
    @FXML
    private TextField Edit_Id;
    @FXML
    private Label PasOkNom;
    @FXML
    private Label PasOkFiliere;
    @FXML
    private Label PasOkAnnee;
    @FXML
    private Label PasOkPrenom;
    @FXML
    private Label PasokId;
    @FXML
    private Label cle_multiple;

    private List<String> liste_Filiere = new ArrayList<>();//contient les champs filiere pour les combobox
    private List<Integer> liste_Annee = new ArrayList<>(); //contient les champs Année pour les combobox

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        remplissage_filiere();
        remplissage_annee();

    }

    //remplissage combobox filiere
    void remplissage_filiere() {
        Connection database = Connection_db.getDatabase();
        PreparedStatement req;
        try {
            req = database.prepareStatement("SELECT Filiere From filiere");
            ResultSet res = req.executeQuery();
            while (res.next()) {
                String fil = res.getString(1);
                liste_Filiere.add(fil);
                for (int i = 0; i < liste_Filiere.size(); ++i) {
                    //remplissage du combobox
                    if (!Choix_Flilere.getItems().contains(liste_Filiere.get(i))) {
                        Choix_Flilere.getItems().add(liste_Filiere.get(i));
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Add_eleveController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //remplissage combobox annee
    void remplissage_annee() {
        Connection database = Connection_db.getDatabase();
        PreparedStatement req;
        try {
            req = database.prepareStatement("SELECT Annee From filiere");
            ResultSet res = req.executeQuery();
            while (res.next()) {
                Integer ann = res.getInt(1);
                liste_Annee.add(ann);
                for (int i = 0; i < liste_Annee.size(); ++i) {
                    //remplissage du combobox
                    if (!Choix_annee.getItems().contains(liste_Annee.get(i))) {
                        Choix_annee.getItems().add(liste_Annee.get(i));
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(Add_eleveController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void Clic_Valider() throws IOException {
        PasokId.setText("");
        cle_multiple.setText("");
        PasOkNom.setText("");
        PasOkFiliere.setText("");
        PasOkAnnee.setText("");
        PasOkPrenom.setText("");

        int ok = 1;
        Integer ID = 0;
        if (Edit_Id.getText().equals("")) {
            PasokId.setText("*");
            ok = 0;
        } else {
            try {
                int count = 0;
                Connection database = Connection_db.getDatabase();
                PreparedStatement req;
                ID = Integer.valueOf(Edit_Id.getText());
                PreparedStatement pslog = database.prepareStatement("SELECT COUNT(*) AS total FROM participant WHERE Part_id = ?");
                pslog.setInt(1, ID);
                ResultSet logres = pslog.executeQuery();
                while (logres.next()) {
                    count = logres.getInt("total");
                }
                if (count != 0) {
                    ok = 0;
                    cle_multiple.setText("Le n° éudiant n'est pas unique");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Add_eleveController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Edit_Nom.getText().equals("")) {
            PasOkNom.setText("*");
            ok = 0;
        }

        if (Choix_Flilere.getSelectionModel().isEmpty()) {
            PasOkFiliere.setText("*");
            ok = 0;
        }
        if (Choix_annee.getSelectionModel().isEmpty()) {
            PasOkAnnee.setText("*");
            ok = 0;
        }

        if (Edit_Prenom.getText().equals("")) {
            PasOkPrenom.setText("*");
            ok = 0;
        }
        if (ok == 1) {
            //Integer ID =Integer.valueOf(Edit_Id.getText());
            String NOM = Edit_Nom.getText();
            String PRENOM = Edit_Prenom.getText();
            String FILIERE = Choix_Flilere.getValue().toString();
            Integer ANNEE = Integer.valueOf(Choix_annee.getValue().toString());

            //remplissage de la base de données
            try {
                Connection connection = Connection_db.getDatabase();
                PreparedStatement ps = connection.prepareStatement("INSERT INTO participant (Part_id, Part_nom, Part_prenom, Filiere_id) "
                        + "VALUES (?, ?, ?, (SELECT Filiere_id FROM filiere Where Filiere = ? AND Annee = ? ) )");
                ps.setInt(1, ID);
                ps.setString(2, NOM);
                ps.setString(3, PRENOM);
                ps.setString(4, FILIERE);
                ps.setInt(5, ANNEE);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            Stage stage = (Stage) content.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void Annuler() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();

    }
}
