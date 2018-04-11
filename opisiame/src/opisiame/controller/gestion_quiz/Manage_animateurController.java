/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import opisiame.dao.Animateur_dao;
import opisiame.model.Animateur;
import opisiame.model.Quiz;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Manage_animateurController implements Initializable {

    private Integer quiz_id;
    @FXML
    private TableView<Animateur> t_liste_anim; // ex: fx:id de la tableView dans FXML builder est aussi t_liste_quiz (c'est pour les lier)
    @FXML
    private TableColumn<Animateur, Boolean> col_cb;
    @FXML
    private TableColumn<Animateur, Integer> col_id_animateur;
    @FXML
    private TableColumn<Animateur, String> col_animateur;
    @FXML
    private TableView<Animateur> t_liste_anim_quiz; // ex: fx:id de la tableView dans FXML builder est aussi t_liste_quiz (c'est pour les lier)
    @FXML
    private TableColumn<Animateur, Boolean> col_cb_quiz;
    @FXML
    private TableColumn<Animateur, Integer> col_id_animateur_quiz;
    @FXML
    private TableColumn<Animateur, String> col_animateur_quiz;
    @FXML
    private TextField txt_search;

    Animateur_dao animateur_dao = new Animateur_dao();

    List<Animateur> animateur_to_add = new ArrayList<>();
    List<Animateur> animateur_to_delete = new ArrayList<>();
    
    ObservableList<Animateur> liste_animateurs;
    ObservableList<Animateur> liste_animateurs_quiz;

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
        set_data_tab_anim();
        set_data_tab_anim_quiz();
    }

    public void update() {
        t_liste_anim.getItems().clear();
        set_data_tab_anim();
        t_liste_anim.refresh();
        t_liste_anim_quiz.getItems().clear();
        set_data_tab_anim_quiz();
        t_liste_anim_quiz.refresh();
    }

    public void set_data_tab_anim() {
        liste_animateurs = animateur_dao.get_animateurs_not_in_quiz(quiz_id);
        t_liste_anim.setItems(liste_animateurs);
    }

    public void set_data_tab_anim_quiz() {
        liste_animateurs_quiz = animateur_dao.get_animateurs_in_quiz(quiz_id);
        t_liste_anim_quiz.setItems(liste_animateurs_quiz);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        col_animateur.setCellValueFactory(new PropertyValueFactory<Animateur, String>("nom"));
        col_id_animateur.setCellValueFactory(new PropertyValueFactory<Animateur, Integer>("id"));

        col_cb.setCellFactory(new Callback<TableColumn<Animateur, Boolean>, TableCell<Animateur, Boolean>>() {
            @Override
            public TableCell<Animateur, Boolean> call(TableColumn<Animateur, Boolean> p) {
                return new CheckBoxCell();
            }
        });

        t_liste_anim.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    t_liste_anim.getSelectionModel().select(row.getIndex());
                }
            }
        });

        col_animateur_quiz.setCellValueFactory(new PropertyValueFactory<Animateur, String>("nom"));
        col_id_animateur_quiz.setCellValueFactory(new PropertyValueFactory<Animateur, Integer>("id"));

        col_cb_quiz.setCellFactory(new Callback<TableColumn<Animateur, Boolean>, TableCell<Animateur, Boolean>>() {
            @Override
            public TableCell<Animateur, Boolean> call(TableColumn<Animateur, Boolean> p) {
                return new CheckBoxCell_quiz();
            }
        });

        t_liste_anim_quiz.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    t_liste_anim_quiz.getSelectionModel().select(row.getIndex());
                }
            }
        });
    }

    public Boolean check_if_is_in_list(List<Animateur> animateurs, Animateur animateur) {
        for (int i = 0; i < animateurs.size(); i++) {
            if (Objects.equals(animateurs.get(i).getId(), animateur.getId())) {
                return true;
            }
        }
        return false;
    }

    // checkbox to add anim in anim_quiz
    private class CheckBoxCell extends TableCell<Animateur, Boolean> {

        final CheckBox check = new CheckBox();

        CheckBoxCell() {
            check.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (check.isSelected()) {
                        Animateur temp = t_liste_anim.getSelectionModel().getSelectedItem();
                        if (!check_if_is_in_list(animateur_to_add, temp)) {
                            animateur_to_add.add(temp);
                        }
                    }
                    if (!check.isSelected()) {
                        Animateur temp = t_liste_anim.getSelectionModel().getSelectedItem();
                        if (check_if_is_in_list(animateur_to_add, temp)) {
                            animateur_to_add.remove(temp);
                        }
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

    private class CheckBoxCell_quiz extends TableCell<Animateur, Boolean> {

        final CheckBox check = new CheckBox();

        CheckBoxCell_quiz() {
            check.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (check.isSelected()) {
                        Animateur temp = t_liste_anim_quiz.getSelectionModel().getSelectedItem();
                        if (!check_if_is_in_list(animateur_to_delete, temp)) {
                            animateur_to_delete.add(temp);
                        }
                    }
                    if (!check.isSelected()) {
                        Animateur temp = t_liste_anim_quiz.getSelectionModel().getSelectedItem();
                        if (check_if_is_in_list(animateur_to_delete, temp)) {
                            animateur_to_delete.remove(temp);
                        }
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

    @FXML
    public void remove_animateur() {
        for (int i = 0; i < animateur_to_delete.size(); i++) {
            animateur_dao.remove_from_anim_quiz(animateur_to_delete.get(i).getId(), quiz_id);
        }
        update();
    }

    @FXML
    public void search_anim() {
        String str = txt_search.getText().toLowerCase();
        ObservableList<Animateur> animateurs = FXCollections.observableArrayList();
        for (Animateur animateur : liste_animateurs) {
            Boolean contain = false;
            if (animateur.getId().toString().toLowerCase().contains(str)) {
                contain = true;
            }
            if (animateur.getNom().toLowerCase().contains(str)) {
                contain = true;
            }
            if (contain == true) {
                animateurs.add(animateur);
            }
        }
        t_liste_anim.setItems(animateurs);
        t_liste_anim.refresh();
    }

    @FXML
    public void add_animateur() {
        for (int i = 0; i < animateur_to_add.size(); i++) {
            animateur_dao.insert_in_anim_quiz(animateur_to_add.get(i).getId(), quiz_id);
        }
        update();
    }
}
