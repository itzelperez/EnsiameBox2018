/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_eleve;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;
import opisiame.database.*;
import opisiame.model.Eleve;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Liste_eleves_adminController implements Initializable {

    private ObservableList<Eleve> eleves = FXCollections.observableArrayList();

    @FXML
    private AnchorPane content;
    @FXML
    private TableView<Eleve> Tableau;
    @FXML
    private TableColumn<Eleve, Boolean> Action_col;
    @FXML
    private TableColumn<Eleve, Integer> id;
    @FXML
    private TableColumn<Eleve, String> Nom;
    @FXML
    private TableColumn<Eleve, String> Prenom;
    @FXML
    private TableColumn<Eleve, String> Filiere;
    @FXML
    private TableColumn<Eleve, Integer> Annee;
    @FXML
    private TextField Champ_recherche;

    private List<Integer> liste_supr = new ArrayList<>();
    private String Cont_recherche = null;

    /*
    Fonction qui récupère la liste des élèves
     */
    public void getAllEleve(/*ObservableList<Eleve> eleves*/) {
        try {
            //ObservableList<Eleve> eleves = FXCollections.observableArrayList();
            Connection connection = Connection_db.getDatabase();
            liste_supr.clear();
            PreparedStatement requette;

            if (Cont_recherche != null) {
                requette = connection.prepareStatement("SELECT participant.Part_id, participant.Part_nom, participant.Part_prenom, filiere.Filiere, filiere.Annee FROM participant \n"
                        + "LEFT JOIN filiere \n"
                        + "ON filiere.Filiere_ID = participant.Filiere_id\n"
                        + "WHERE participant.Part_id LIKE ?\n"
                        + "OR participant.Part_nom LIKE ?\n"
                        + "OR participant.Part_prenom LIKE ?\n"
                        + "OR filiere.Filiere LIKE ?\n"
                        + "OR filiere.Annee LIKE ?");
                requette.setString(1, "%" + Cont_recherche + "%");
                requette.setString(2, "%" + Cont_recherche + "%");
                requette.setString(3, "%" + Cont_recherche + "%");
                requette.setString(4, "%" + Cont_recherche + "%");
                requette.setString(5, "%" + Cont_recherche + "%");
            } else {
                requette = connection.prepareStatement("SELECT participant.Part_id, participant.Part_nom, participant.Part_prenom, filiere.Filiere, filiere.Annee FROM participant \n"
                        + "LEFT JOIN filiere \n"
                        + "ON filiere.Filiere_ID = participant.Filiere_id");
            }
            ResultSet res_requette = requette.executeQuery();
            while (res_requette.next()) {
                Eleve etudiant = new Eleve();
                etudiant.setId(res_requette.getInt(1));
                etudiant.setNom(res_requette.getString(2));
                etudiant.setPrenom(res_requette.getString(3));
                etudiant.setFiliere(res_requette.getString(4));
                etudiant.setAnnee(res_requette.getInt(5));
                eleves.add(etudiant);
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
        id.setCellValueFactory(new PropertyValueFactory<Eleve, Integer>("id"));
        Nom.setCellValueFactory(new PropertyValueFactory<Eleve, String>("Nom"));
        Prenom.setCellValueFactory(new PropertyValueFactory<Eleve, String>("Prenom"));
        Filiere.setCellValueFactory(new PropertyValueFactory<Eleve, String>("Filiere"));
        Annee.setCellValueFactory(new PropertyValueFactory<Eleve, Integer>("Annee"));
        getAllEleve(/*eleves*/);
        Tableau.setItems(eleves);

        //Insert Button    
        Action_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Eleve, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Eleve, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        Action_col.setCellFactory(new Callback<TableColumn<Eleve, Boolean>, TableCell<Eleve, Boolean>>() {
            @Override
            public TableCell<Eleve, Boolean> call(TableColumn<Eleve, Boolean> param) {
                return new CheckBoxCell();
            }
        });

        Tableau.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    Tableau.getSelectionModel().select(row.getIndex());
                }
            }
        });

    }

    private class CheckBoxCell extends TableCell<Eleve, Boolean> {

        final CheckBox check = new CheckBox();

        CheckBoxCell() {
            check.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (check.isSelected()) {
                        //Integer id = Tableau.getSelectionModel().getSelectedItem().getId();
                        Integer id = Tableau.getFocusModel().getFocusedItem().getId();
                        liste_supr.add(id);
                        System.out.println("Elegiste un batillo pa' borrar" + liste_supr.size());
                    }
                }
            });
        }

        //Affichage des boutons
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                HBox lay = new HBox(1);
                lay.getChildren().add(check);
                setGraphic(lay);
            }
        }
    };

    public void update_tableau() {
        Tableau.getItems().clear();
        eleves.clear();
        //getAllEleve(/*eleves*/);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/eleve/Liste_eleves_adminController.fxml"));
        URL url = fxmlLoader.getLocation();
        ResourceBundle rb = fxmlLoader.getResources();
        this.initialize(url, rb);
        Tableau.setItems(eleves);
        Tableau.refresh();
    }

    public void Rechercher() {
        Cont_recherche = Champ_recherche.getText();
        update_tableau();
    }

    public void delete_eleve() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_eleve/Delete_eleve.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Delete_eleveController delete_controller = fxmlLoader.<Delete_eleveController>getController();
            delete_controller.setEleve_id(liste_supr);
            System.out.println("GOT HERE");
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation de suppression");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(Tableau.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();

            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    update_tableau();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_eleves_adminController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Boutton de retour
    @FXML
    public void ClicBoutonRetour() throws IOException {
        //Retour sur la fenetre menu
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //boutton log out
    @FXML
    public void ClicBoutonHome() throws IOException {
        //Retour sur la fenetre d'identification
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        session.Session.Logout();
    }

    @FXML
    public void Clic_ajout_eleve() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_eleve/Add_eleve.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Add_eleveController add_controller = fxmlLoader.<Add_eleveController>getController();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Ajouter un élève");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //stage.initOwner(Tableau.getScene().getWindow());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                update_tableau();
            }
        });
    }

    @FXML
    public void Import_eleve() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_eleve/import_excel.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Import_excelController add_controller = fxmlLoader.<Import_excelController>getController();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Importer des élèves");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //stage.initOwner(Tableau.getScene().getWindow());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                    update_tableau();
            }

        });
    }
}
