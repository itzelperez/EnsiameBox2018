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
import java.util.logging.*;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import opisiame.database.Connection_db;

/**
 * FXML Controller class
 *
 * @author clement
 */
public class Delete_eleveController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane content;

    private List<Integer> liste_supr = new ArrayList<>();

    public void setEleve_id(List liste_id) {
        this.liste_supr = liste_id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void Confirmer() throws IOException {
        ListIterator<Integer> i = liste_supr.listIterator();
        try {
            Connection connection = Connection_db.getDatabase();
            while (i.hasNext()) {
                int supr = i.next();
                PreparedStatement requette;
                System.out.println("SHOULD BE AFTER");
                requette = connection.prepareStatement("SELECT * FROM participant WHERE Part_id = ?");
                requette.setInt(1, supr);
                requette.executeUpdate();

            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void Annuler() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }
}
