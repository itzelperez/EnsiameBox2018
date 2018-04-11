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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import opisiame.database.Connection_db;
import opisiame.model.Animateur;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Liste_animController implements Initializable {

    //injection des éléments graphiques
    @FXML
    private GridPane content;
    @FXML
    private TableView<Animateur> t_liste;
    @FXML
    private TableColumn<Animateur, Integer> c_id;
    @FXML
    private TableColumn<Animateur, String> c_nom;
    @FXML
    private TableColumn<Animateur, String> c_login;
    @FXML
    private TableColumn<Animateur, Boolean> c_actions;
    @FXML
    private TableColumn<Animateur, Boolean> c_selec;
    @FXML
    private TextField Champ_recherche;

    private List<Integer> liste_supr = new ArrayList<>();
    private String Cont_recherche = null;

    //récupération de la liste des Animateurs dans la BDD, et affichage
    public ObservableList<Animateur> getAllAnimateur() {

        ObservableList<Animateur> anims = FXCollections.observableArrayList();
        try {
            Connection connection = Connection_db.getDatabase();
            liste_supr.clear();
            PreparedStatement ps;

            if (Cont_recherche != null) {
                ps = connection.prepareStatement("SELECT * FROM animateur \n"
                        + "WHERE Anim_id LIKE ?\n"
                        + "OR Anim_nom LIKE ?\n"
                        + "OR Anim_login LIKE ?\n");
                ps.setString(1, "%" + Cont_recherche + "%");
                ps.setString(2, "%" + Cont_recherche + "%");
                ps.setString(3, "%" + Cont_recherche + "%");
            } else {
                ps = connection.prepareStatement("SELECT * FROM animateur");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Animateur anim = new Animateur();
                anim.setId(rs.getInt(1));
                anim.setNom(rs.getString(2));
                anim.setLg(rs.getString(3));
                anims.add(anim);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return anims;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //remplissage de la table
        c_id.setCellValueFactory(new PropertyValueFactory<Animateur, Integer>("id"));
        c_nom.setCellValueFactory(new PropertyValueFactory<Animateur, String>("nom"));
        c_login.setCellValueFactory(new PropertyValueFactory<Animateur, String>("lg"));
        t_liste.setItems(getAllAnimateur());

        //Insert Button
        c_actions.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Animateur, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Animateur, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });

        c_actions.setCellFactory(new Callback<TableColumn<Animateur, Boolean>, TableCell<Animateur, Boolean>>() {

            @Override
            public TableCell<Animateur, Boolean> call(TableColumn<Animateur, Boolean> p) {
                return new Liste_animController.ButtonCell();
            }

        });

        c_selec.setCellFactory(new Callback<TableColumn<Animateur, Boolean>, TableCell<Animateur, Boolean>>() {
            @Override
            public TableCell<Animateur, Boolean> call(TableColumn<Animateur, Boolean> param) {
                return new Liste_animController.CheckBoxCell();
            }
        });

        t_liste.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    t_liste.getSelectionModel().select(row.getIndex());
                }
            }
        });

    }

    public void Rechercher() throws IOException {
        Cont_recherche = Champ_recherche.getText();
        update_tableau();

        //appel de la fonction initialize, permet d'afficher correctement les checkbox/boutons
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/animateur/liste_anim.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        URL url = fxmlLoader.getLocation();
        ResourceBundle rb = fxmlLoader.getResources();
        this.initialize(url, rb);

    }

    private class CheckBoxCell extends TableCell<Animateur, Boolean> {

        final CheckBox check = new CheckBox();

        CheckBoxCell() {
            check.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (check.isSelected()) {
                        Integer id = t_liste.getFocusModel().getFocusedItem().getId();
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
                lay.getChildren().add(check);
                setGraphic(lay);
            }
        }
    };

    //Define the button cell
    private class ButtonCell extends TableCell<Animateur, Boolean> {

        final Button btn_edit = new Button();

        ButtonCell() {
            btn_edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {

                    editer_prof();
                }
            });

        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {

                HBox box = new HBox(3);

                Image img_edit = new Image(getClass().getResourceAsStream("/opisiame/image/edit.png"), 20, 20, true, true);
                btn_edit.setGraphic(new ImageView(img_edit));

                box.setPadding(new Insets(5, 0, 5, 0));
                // box.setPrefColumns(1);
                box.getChildren().add(btn_edit);
                setGraphic(box);
            }
        }
    }

    public void editer_prof() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/animateur/edit_anim.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Edit_animController edit_controller = fxmlLoader.<Edit_animController>getController();
            int animID = t_liste.getFocusModel().getFocusedItem().getId();
            edit_controller.setAnim_id(animID);

            URL url = fxmlLoader.getLocation();
            ResourceBundle rb = fxmlLoader.getResources();
            edit_controller.initialize(url, rb);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modification");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste.getScene().getWindow());
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
            Logger.getLogger(Liste_animController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //EN DEHORS DE LA TABLE
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
        //ouverture fenêtre menu_ens
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public void update_tableau() {
        t_liste.getItems().clear();
        t_liste.setItems(getAllAnimateur());
        t_liste.refresh();

    }

    @FXML
    public void ClicBoutonSupprSelec() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/animateur/del_anim.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Del_animController delete_controller = fxmlLoader.<Del_animController>getController();
            delete_controller.setAnim_id(liste_supr);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation de suppression");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    try {
                        Stage stage = (Stage) content.getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/animateur/liste_anim.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(true);
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(Liste_animController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_animController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void ToutSupprimer() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/animateur/del_all_anim.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Del_all_animController delete_controller = fxmlLoader.<Del_all_animController>getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation de suppression");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    try {
                        Stage stage = (Stage) content.getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/animateur/liste_anim.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(true);
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(Liste_animController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_animController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void ClicBoutonAjoutAnim() throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/animateur/ajout_anim.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajout animateur");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(t_liste.getScene().getWindow());
            stage.centerOnScreen();
            stage.show();

            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    update_tableau();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_animController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
