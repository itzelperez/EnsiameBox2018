/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import com.rapplogic.xbee.XBeePin;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import opisiame.controller.com.ListenToRemoteThread;
import opisiame.controller.gestion_eleve.Link_eleve_teleController;
import opisiame.model.Question;
import opisiame.dao.*;
import opisiame.model.Eleve;
import opisiame.model.Reponse;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Lancer_questionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Integer quiz_id;
    private Integer quiz_timer;

    private Integer current_question_no;

    private List<Eleve> eleves;
    private XBee xbee = new XBee();

    @FXML
    private GridPane gpane;

    ListenToRemoteThread listenRemote;

//    @FXML
//    private Button label_timer;
    @FXML
    private Button rep_1;

    @FXML
    private Button rep_2;

    @FXML
    private Button rep_3;

    @FXML
    private Button rep_4;

    @FXML
    private ImageView question_img_view;

    @FXML
    private Label label_question;

    @FXML
    private Button btn_next_question;

    private ArrayList<Question> questions;
    //private ImageView img_view;
    private double ratio_height;
    private double ratio_width;
    String num_port;

    String led_yellow = "D7";
    String led_green = "D5";
    String led_red = "D4";

    Question_dao question_dao = new Question_dao();

    Question current_question;

    Timer t;

    public void setEleves(List<Eleve> eleves) {
        this.eleves = eleves;
    }

    public void setNum_port(String num_port) {
        this.num_port = num_port;
        try {
            xbee.open(num_port, 9600);
            System.out.println("xbee connected");
        } catch (XBeeException ex) {
            ex.printStackTrace();
        }
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
        get_all_questions();
        if (questions.size() > 0) {
            current_question_no = 0;
            print_question();
//            pagination_quest.setPageCount(questions.size());
//            pagination_quest.setCurrentPageIndex(0);
        }
    }

    public Integer getQuiz_timer() {
        return quiz_timer;
    }

    public void setQuiz_timer(Integer quiz_timer) {
        this.quiz_timer = quiz_timer;
    }

    private void get_all_questions() {
        questions = question_dao.get_questions_by_quiz(this.quiz_id);
    }

    public void print_question() {
        if (current_question_no < questions.size()) {
            Question q = questions.get(current_question_no);
            current_question = q;
            //label_timer.setText((q.getTimer()).toString());
            label_question.setText(q.getLibelle());
            print_image(q.getImg_blob());
            print_reponse(q.getReponses());
            int timer = get_quest_timer();
            listen_remote();
            if (timer > 0) {
                run_timer(timer);
                btn_next_question.setDisable(true);
            } else {
                btn_next_question.setDisable(false);
                if (current_question_no >= questions.size() - 1) {
                    btn_next_question.setText("Terminer");
                } else {
                    btn_next_question.setText(" >> ");
                }
            }
        }
    }

    public void switch_off_remotes(String led_id) {
        try {
            RemoteAtRequest request_led_off = new RemoteAtRequest(XBeeAddress64.BROADCAST, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_LOW.getValue()});
            xbee.sendAsynchronous(request_led_off);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Link_eleve_teleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (XBeeException ex) {
            Logger.getLogger(Lancer_questionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switch_on_remotes(String led_id) {
        try {
            RemoteAtRequest request_led_off = new RemoteAtRequest(XBeeAddress64.BROADCAST, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_HIGH.getValue()});
            xbee.sendAsynchronous(request_led_off);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Link_eleve_teleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (XBeeException ex) {
            Logger.getLogger(Lancer_questionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listen_remote() {
        switch_off_remotes(led_yellow);
        switch_on_remotes(led_green);
        if (num_port != null) {
            if (listenRemote != null) {
                listenRemote.interrupt();
            }
            listenRemote = new ListenToRemoteThread(xbee, num_port, eleves);
            listenRemote.setRep_id_a(current_question.getReponses().get(0).getId());
            listenRemote.setRep_id_b(current_question.getReponses().get(1).getId());
            listenRemote.setRep_id_c(current_question.getReponses().get(2).getId());
            listenRemote.setRep_id_d(current_question.getReponses().get(3).getId());
            listenRemote.start();
        }
    }

    public void run_timer(Integer duree) {
        btn_next_question.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if ("0".equals(newValue)) {
                    if (current_question_no >= questions.size() - 1) {
                        switch_off_remotes(led_green);
                        switch_off_remotes(led_yellow);
                        xbee.close();
                        close_window();
                    } else {
                        next_question();
                    }
                }
            }
        });
        Timer_update_label thread_class = new Timer_update_label(btn_next_question, duree);
        thread_class.start();
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
        b.setText(rep.getLibelle());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gpane.heightProperty().addListener(listener);
    }

    @FXML
    private void next_question() {
        listenRemote.setRunning(false);
        current_question_no++;
        if (current_question_no >= questions.size() - 1) {
            end_quiz();
        }
        print_question();
    }

    public int get_quest_timer() {
        if (current_question.getTimer() != null) {
            if (current_question.getTimer() > 0) {
                return current_question.getTimer();
            } else if (quiz_timer > 0) {
                return quiz_timer;
            }
        }
        return 0;
    }

    public void end_quiz() {
        btn_next_question.setText("Terminer");
        btn_next_question.setDisable(false);
        btn_next_question.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                switch_off_remotes(led_green);
                switch_off_remotes(led_yellow);
                xbee.close();
                close_window();
            }
        });
    }

    public void close_window() {
        Stage stage = (Stage) rep_1.getScene().getWindow();
        stage.close();
    }

}
