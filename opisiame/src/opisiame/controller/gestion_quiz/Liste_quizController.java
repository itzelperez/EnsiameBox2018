/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.io.IOException;
import java.net.URL;
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
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;
import opisiame.dao.Quiz_dao;
import opisiame.model.*;
import session.Session;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Liste_quizController implements Initializable {

    /*
    Injection des élèments depuis la vue (fichier fxml) dans le code (à partir de fx:id)
     */
    @FXML
    private GridPane content;
    @FXML
    private TableView<Quiz> t_liste_quiz; // ex: fx:id de la tableView dans FXML builder est aussi t_liste_quiz (c'est pour les lier)
    @FXML
    private TableColumn<Quiz, String> nom_quiz;
    @FXML
    private TableColumn<Quiz, String> date;
    @FXML
    private TableColumn<Quiz, Integer> id;
    @FXML
    private TableColumn<Quiz, Boolean> actionCol;
    @FXML
    private TableColumn<Quiz, Boolean> col_cb;
    @FXML
    private TableColumn<Quiz, Integer> timer;
    @FXML
    private TextField txt_search;
    @FXML
    private Button btn_suppr_selected;
    @FXML
    private Button btn_ajout_quiz;

    private List<Integer> liste_supr = new ArrayList<>();
    private ObservableList<Quiz> liste_quizs;
    Quiz_dao quiz_dao = new Quiz_dao();

    /*
    Fonction qui récupère la liste des quizs
     */
    public ObservableList<Quiz> getAllquiz() {
        liste_supr.clear();
        liste_quizs = quiz_dao.getAllquiz();
        return liste_quizs;
    }

    /*
    Fonction qui initalise la vue
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Stage stage = (Stage) t_liste_quiz.getScene().getWindow();
        nom_quiz.setCellValueFactory(new PropertyValueFactory<Quiz, String>("nom"));
        date.setCellValueFactory(new PropertyValueFactory<Quiz, String>("date_creation"));
        id.setCellValueFactory(new PropertyValueFactory<Quiz, Integer>("id"));
        timer.setCellValueFactory(new PropertyValueFactory<Quiz, Integer>("timer"));
        t_liste_quiz.setItems(getAllquiz());

        actionCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Quiz, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Quiz, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });

        actionCol.setCellFactory(new Callback<TableColumn<Quiz, Boolean>, TableCell<Quiz, Boolean>>() {

            @Override
            public TableCell<Quiz, Boolean> call(TableColumn<Quiz, Boolean> p) {
                return new ButtonCell();
            }

        });

        col_cb.setCellFactory(new Callback<TableColumn<Quiz, Boolean>, TableCell<Quiz, Boolean>>() {
            @Override
            public TableCell<Quiz, Boolean> call(TableColumn<Quiz, Boolean> p) {
                return new CheckBoxCell();
            }
        });

        t_liste_quiz.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    t_liste_quiz.getSelectionModel().select(row.getIndex());
                }
            }
        });
        
        if (Session.getType().equals("anim")) {
            btn_suppr_selected.setVisible(false);
            btn_ajout_quiz.setVisible(false);
        }
        if (Session.getType().equals("admin")) {
            btn_ajout_quiz.setVisible(false);
        }

    }

    private class CheckBoxCell extends TableCell<Quiz, Boolean> {

        final CheckBox check = new CheckBox();

        CheckBoxCell() {
            check.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (check.isSelected()) {
                        Integer id = t_liste_quiz.getFocusModel().getFocusedItem().getId();
                        //Integer id = t_liste_quiz.get
                        liste_supr.add(id);
                        System.out.println(id);
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
    private class ButtonCell extends TableCell<Quiz, Boolean> {

        final Button btn_edit = new Button();
        final Button btn_delete = new Button();
        final Button btn_detail = new Button();
        final Button btn_lancer = new Button();

        ButtonCell() {
            btn_edit.setStyle("-fx-background-color: gray");
            btn_edit.setCursor(Cursor.HAND);

            btn_detail.setStyle("-fx-background-color: green");
            btn_detail.setCursor(Cursor.HAND);

            btn_delete.setStyle("-fx-background-color: red");
            btn_delete.setCursor(Cursor.HAND);

            btn_lancer.setStyle("-fx-background-color: blue");
            btn_lancer.setCursor(Cursor.HAND);

            btn_edit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    edit_quiz();
                }
            });
            btn_delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    delete_quiz();
                }
            });
            btn_detail.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    detail_quiz();
                }
            });
            btn_lancer.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    lancer_quiz();
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {

                HBox box = new HBox(3);

                box.setAlignment(Pos.CENTER);

                Image img_edit = new Image(getClass().getResourceAsStream("/opisiame/image/edit.png"), 20, 20, true, true);
                btn_edit.setGraphic(new ImageView(img_edit));

                Image img_delete = new Image(getClass().getResourceAsStream("/opisiame/image/delete.png"), 20, 20, true, true);
                btn_delete.setGraphic(new ImageView(img_delete));

                Image img_detail = new Image(getClass().getResourceAsStream("/opisiame/image/detail.png"), 20, 20, true, true);
                btn_detail.setGraphic(new ImageView(img_detail));

                Image img_lancer = new Image(getClass().getResourceAsStream("/opisiame/image/lancer.png"), 20, 20, true, true);
                btn_lancer.setGraphic(new ImageView(img_lancer));

                box.setPadding(new Insets(5, 0, 5, 0));//ajout de marge à l'interieur du bouton
                // box.setPrefColumns(1);
                if (!Session.getType().equals("anim")) {
                    box.getChildren().add(btn_edit);
                    box.getChildren().add(btn_delete);
                    box.getChildren().add(btn_detail);
                }
                box.getChildren().add(btn_lancer);
                setGraphic(box);
            }
        }
    }

    public void lancer_quiz() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/lancer_quiz.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Lancer_quizController lancer_controller = fxmlLoader.<Lancer_quizController>getController();
            lancer_controller.setQuiz_id(t_liste_quiz.getSelectionModel().getSelectedItem().getId());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Détail quiz");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste_quiz.getScene().getWindow());
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void edit_quiz() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/edit_quiz.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Edit_quizController edit_controller = fxmlLoader.<Edit_quizController>getController();
            edit_controller.setQuiz_id(t_liste_quiz.getFocusModel().getFocusedItem().getId());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setTitle("Modifier quiz");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste_quiz.getScene().getWindow());
            stage.centerOnScreen();
            stage.show();

            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    update_tableau();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete_quiz() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/delete_quiz.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Delete_quizController delete_controller = fxmlLoader.<Delete_quizController>getController();
            delete_controller.setQuiz_id(t_liste_quiz.getFocusModel().getFocusedItem().getId());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation de suppression");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste_quiz.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    update_tableau();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update_tableau() {
        t_liste_quiz.getItems().clear();
        t_liste_quiz.setItems(getAllquiz());
        t_liste_quiz.refresh();
    }

//    final ChangeListener<Number> listener_modif = new ChangeListener<Number>() {
//        @Override
//        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//            System.out.println(".changed()");
//        }
//    };
    private void handleButtonAction(ActionEvent event) {
        // Button was clicked, do something...
        System.out.println("opisiame.controller.gestion_quiz.Liste_quizController.handleButtonAction()");
    }

    public void detail_quiz() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/affichage_quiz.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Affichage_quizController detail_controller = fxmlLoader.<Affichage_quizController>getController();
            detail_controller.setQuiz_id(t_liste_quiz.getFocusModel().getFocusedItem().getId());

            detail_controller.getBtn_modif().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    edit_quiz();
                    Node source = (Node) event.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                }
            });

            detail_controller.getBtn_delete().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    delete_quiz();
                    Node source = (Node) event.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.close();
                }
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Détail quiz");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste_quiz.getScene().getWindow());
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void ajout_quiz() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_quiz/nouveau_quiz.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajout quiz");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initOwner(t_liste_quiz.getScene().getWindow());
            stage.centerOnScreen();
            stage.show();

            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    update_tableau();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void search_quiz() {
        String str = txt_search.getText().toLowerCase();
        ObservableList<Quiz> quizs_temp = FXCollections.observableArrayList();
        for (Quiz quiz : liste_quizs) {
            Boolean contain = false;
            if (quiz.getId().toString().toLowerCase().contains(str)) {
                contain = true;
            }
            if (quiz.getDate_creation().toLowerCase().contains(str)) {
                contain = true;
            }
            if (quiz.getNom().toLowerCase().contains(str)) {
                contain = true;
            }
            if (quiz.getTimer().toString().toLowerCase().contains(str)) {
                contain = true;
            }
            if (contain == true) {
                quizs_temp.add(quiz);
            }
        }
        t_liste_quiz.setItems(quizs_temp);
        t_liste_quiz.refresh();
    }

//    public ObservableList<Quiz> recherche_quiz(String str) {
//        
//        return quizs_temp;
//    }
    //Clic bouton on/off
    @FXML
    public void ClicImageOnOff() throws IOException {
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

    //Clic bouton de retour
    @FXML
    public void ClicBoutonRetour() throws IOException {
        //Retour sur la fenetre menu
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
    public void ClicBoutonSupprSelec() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/delete_selected_quiz.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Delete_selected_quizController delete_controller = fxmlLoader.<Delete_selected_quizController>getController();
            delete_controller.setQuiz_id(liste_supr);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Confirmation de suppression");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(t_liste_quiz.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    try {
                        Stage stage = (Stage) content.getScene().getWindow();
                        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_quiz/liste_quiz.fxml"));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setResizable(true);
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
