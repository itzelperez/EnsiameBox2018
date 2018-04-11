/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import opisiame.model.Question;
import opisiame.dao.*;
import opisiame.model.Reponse;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Affiche_questionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Integer quiz_id;

    @FXML
    private GridPane gpane;

    @FXML
    private Button label_timer;

    @FXML
    private Button rep_1;

    @FXML
    private Button rep_2;

    @FXML
    private Button rep_3;

    @FXML
    private Button rep_4;

    @FXML
    private Button sous_comp;

    @FXML
    private ImageView question_img_view;

    @FXML
    private Label label_question;

    @FXML
    private Pagination pagination_quest;

    private ArrayList<Question> questions;
    //private ImageView img_view;
    private double ratio_height;
    private double ratio_width;

    Question_dao question_dao = new Question_dao();

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
        get_all_questions();
        if (questions.size() > 0) {
            print_question(0);
            pagination_quest.setPageCount(questions.size());
            pagination_quest.setCurrentPageIndex(0);
        }
    }

    private void get_all_questions() {
        questions = question_dao.get_questions_by_quiz(this.quiz_id);
    }

    public void print_question(Integer index) {
        if (questions.size() > 0) {
            Question q = questions.get(index);
            if (q.getTimer() != null) {
                label_timer.setText((q.getTimer()).toString());
            } else {
                label_timer.setText("-");
            }
            label_question.setText(q.getLibelle());
            sous_comp.setText(q.getSous_comp());
            print_image(q.getImg_blob());
            print_reponse(q.getReponses());
        }
    }

    public void print_image(InputStream blob_img) {
        if (blob_img != null) {
            try {
                BufferedImage buffered_image = ImageIO.read(blob_img);
                if (buffered_image != null) {
                    Image image = SwingFXUtils.toFXImage(buffered_image, null);
                    question_img_view.setImage(image);
                    question_img_view.setSmooth(true);
                    question_img_view.setCache(true);
                    question_img_view.setPreserveRatio(true);
                    ratio_height = gpane.getHeight() / question_img_view.getFitHeight();
                    ratio_width = gpane.getWidth() / question_img_view.getFitWidth();
                    buffered_image.flush();
                    blob_img.reset();
                    blob_img.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            question_img_view.setImage(null);
        }
    }

    final ChangeListener<Number> listener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            if (ratio_height == 0) {
                ratio_height = gpane.getHeight() / question_img_view.getFitHeight();
                ratio_width = gpane.getWidth() / question_img_view.getFitWidth();
            }
            question_img_view.setFitHeight(gpane.getHeight() / ratio_height);
            question_img_view.setFitWidth(gpane.getWidth() / ratio_width);
            question_img_view.setSmooth(true);
        }
    };

    public void print_reponse(ArrayList<Reponse> reponses) {
        ArrayList<Button> bts = new ArrayList(Arrays.asList(rep_1, rep_2, rep_3, rep_4));
        for (int i = 0; i < reponses.size(); i++) {
            affiche_rep(reponses.get(i), bts.get(i));
        }
    }

    public void affiche_rep(Reponse rep, Button b) {
        if (rep.getIs_bonne_reponse() == 1) {
            b.setStyle("-fx-background-color: #b7cff4;"
                    + "-fx-border-radius: 5px");
        } else {
            b.setStyle("-fx-background-color: white;"
                    + "-fx-border-color: gray;"
                    + "-fx-border-radius: 5px");
        }
        b.setText(rep.getLibelle());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination_quest.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer p) {
                print_question(p);
                return new VBox();
            }

        });
//        img_view = new ImageView();
//        img_view.setPreserveRatio(true);
        gpane.heightProperty().addListener(listener);
    }

}
