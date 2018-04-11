/* A FAIRE
** FINIR INTERFACE
**
 */
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.competence;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;
import opisiame.database.Connection_db;
import opisiame.model.Competence;
import opisiame.model.Sous_competence;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Affichage_sous_competencesController implements Initializable {

    /*
    Injection des élèments depuis la vue (fichier fxml) dans le code (à partir de fx:id)
     */
    @FXML
    private GridPane content;
    @FXML
    private TableView<Sous_competence> t_liste_souscompetence; // ex: fx:id de la tableView dans FXML builder est aussi t_liste_quiz (c'est pour les lier)
    @FXML
    private TableColumn<Sous_competence, String> nom_souscompetence;
    @FXML
    private TableColumn<Sous_competence, Boolean> action;
    @FXML
    private TableColumn<Sous_competence, Integer> c_comp_id;
    @FXML
    private TableColumn<Sous_competence, Boolean> c_selec;
    @FXML
    private TableColumn<Sous_competence, Integer> id;
    @FXML
    private TextField txt_search;

    private List<Integer> liste_supr = new ArrayList<>();
    private String Cont_recherche = null;
    private int compid;

    public void setComp_id(int animID) {
        this.compid = animID;
    }

    //récupération de la liste des sous-competences dans la BDD, et affichage
    public ObservableList<Sous_competence> getAllSousComp() {

        ObservableList<Sous_competence> comps = FXCollections.observableArrayList();
        try {
            Connection connection = Connection_db.getDatabase();
            liste_supr.clear();
            PreparedStatement ps;

            if (Cont_recherche != null) {
                ps = connection.prepareStatement("SELECT * FROM souscompetence \n"
                        + "WHERE Comp_id LIKE ?\n"
                        + "AND( SousCompetence LIKE ? OR SousComp_id = ? )\n");
                ps.setInt(1, compid);
                ps.setString(2, "%" + Cont_recherche + "%");
                ps.setString(3, "%" + Cont_recherche + "%");
            } else {
                ps = connection.prepareStatement("SELECT * FROM souscompetence \n"
                        + "WHERE Comp_id LIKE ?\n");
                ps.setInt(1, compid);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sous_competence sscomp = new Sous_competence();
                sscomp.setId(rs.getInt(1));
                sscomp.setLibelle(rs.getString(2));
                sscomp.setComp_id(rs.getInt(3));
                comps.add(sscomp);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return comps;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //remplissage de la table
        id.setCellValueFactory(new PropertyValueFactory<Sous_competence, Integer>("id"));
        nom_souscompetence.setCellValueFactory(new PropertyValueFactory<Sous_competence, String>("libelle"));
        c_comp_id.setCellValueFactory(new PropertyValueFactory<Sous_competence, Integer>("comp_id"));
        t_liste_souscompetence.setItems(getAllSousComp());

        //Insert Button
        action.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Sous_competence, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Sous_competence, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });

        action.setCellFactory(new Callback<TableColumn<Sous_competence, Boolean>, TableCell<Sous_competence, Boolean>>() {

            @Override
            public TableCell<Sous_competence, Boolean> call(TableColumn<Sous_competence, Boolean> p) {
                return new Affichage_sous_competencesController.ButtonCell();
            }

        });

        c_selec.setCellFactory(new Callback<TableColumn<Sous_competence, Boolean>, TableCell<Sous_competence, Boolean>>() {
            @Override
            public TableCell<Sous_competence, Boolean> call(TableColumn<Sous_competence, Boolean> param) {
                return new Affichage_sous_competencesController.CheckBoxCell();
            }
        });

        t_liste_souscompetence.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    t_liste_souscompetence.getSelectionModel().select(row.getIndex());
                }
            }
        });
    }

    public void Rechercher() throws IOException {
        Cont_recherche = txt_search.getText();
        update_tableau();

    }

    private class CheckBoxCell extends TableCell<Sous_competence, Boolean> {

        final CheckBox check = new CheckBox();

        CheckBoxCell() {
            check.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (check.isSelected()) {
                        Integer id = t_liste_souscompetence.getFocusModel().getFocusedItem().getId();
                        liste_supr.add(id);
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

                lay.setAlignment(Pos.CENTER);

                lay.getChildren().add(check);

                setGraphic(lay);
            }
        }
    };

    //Define the button cell
    private class ButtonCell extends TableCell<Sous_competence, Boolean> {

        final Button btn_edit = new Button();

        ButtonCell() {

            btn_edit.setStyle("-fx-background-color: gray");
            btn_edit.setCursor(Cursor.HAND);

            btn_edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {

                    editer_sous_competence();
                }
            });

        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {

                HBox box = new HBox(1);

                box.setAlignment(Pos.CENTER);

                Image img_edit = new Image(getClass().getResourceAsStream("/opisiame/image/edit.png"), 20, 20, true, true);
                btn_edit.setGraphic(new ImageView(img_edit));

                box.setPadding(new Insets(5, 0, 5, 0));
                box.getChildren().add(btn_edit);
                setGraphic(box);
            }
        }

    };

    public void editer_sous_competence() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/competence/editer_sous_competence.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Editer_sous_competenceController edit_controller = fxmlLoader.<Editer_sous_competenceController>getController();
            int compID = t_liste_souscompetence.getFocusModel().getFocusedItem().getId();
            edit_controller.setSousComp_id(compID);

            URL url = fxmlLoader.getLocation();
            ResourceBundle rb = fxmlLoader.getResources();
            edit_controller.initialize(url, rb);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modification");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste_souscompetence.getScene().getWindow());
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

        } catch (IOException ex) {
            Logger.getLogger(Affichage_sous_competencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update_tableau() {
        t_liste_souscompetence.getItems().clear();
        t_liste_souscompetence.setItems(getAllSousComp());
        t_liste_souscompetence.refresh();

    }

//    //Clic bouton on/off -> retourne sur la page d'identification
//    @FXML
//    public void ClicImageOnOff() throws IOException {
//        //Retour sur la fenetre d'identification
//        Stage stage = (Stage) content.getScene().getWindow();
//        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.setResizable(false);
//        stage.show();
//        session.Session.Logout();
//    }
    //Clic bouton de retour -> retourne sur la fenêtre 
    @FXML
    public void ClicBoutonRetour() throws IOException {
        //Retour sur la fenetre compétences
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    //Affiche la page "ajouter une sous compétence"
    @FXML
    public void ClicBoutonAjoutSousComp() throws IOException {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/competence/ajout_sous_comp.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Ajout_sous_compController ajout_sous_comp_controller = fxmlLoader.<Ajout_sous_compController>getController();

            ajout_sous_comp_controller.setId_comp(compid);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajout compétence");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(t_liste_souscompetence.getScene().getWindow());
            stage.centerOnScreen();
            stage.show();

            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    update_tableau();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(CompetencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Affiche la page de confirmation pour supprimer des sous compétences
    @FXML
    public void ClicBoutonSupprSelec() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/competence/delete_sous_comp.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Delete_sous_compController delete_controller = fxmlLoader.<Delete_sous_compController>getController();
            delete_controller.setSousComp_id(liste_supr);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation de suppression");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste_souscompetence.getScene().getWindow());
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

        } catch (IOException ex) {
            Logger.getLogger(CompetencesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

};
