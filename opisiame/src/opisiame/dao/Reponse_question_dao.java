/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import opisiame.model.Participation_quiz;
import opisiame.model.Reponse;
import opisiame.model.Reponse_question;

/**
 *
 * @author Sandratra
 */
public class Reponse_question_dao {

    Reponse_dao reponse_dao = new Reponse_dao();
    Participation_quiz_dao participation_quiz_dao = new Participation_quiz_dao();

    public Reponse_question get_res_by_quest(Integer quest_id, Participation_quiz participation_quiz) {
        Timestamp date_participation = participation_quiz.getDate_participation();
        Reponse_question rq = new Reponse_question();
        // récupérer les réponses possibles d'une question
        List<Reponse> reponses = reponse_dao.get_reponses_by_quest(quest_id);
        // liste pour sauvegarder le nombre de participants ayant selectionné chacune des réponses possibles
        List<Integer> nb_reps = new ArrayList<>();
//        nombre total des participants ayant répondu aux questions
        Integer nb_participants = participation_quiz_dao.get_participants_quizs(participation_quiz).size();

        if (nb_participants == 0.0) {
            rq.setPourcentage_rep_a(0.0);
            rq.setPourcentage_rep_b(0.0);
            rq.setPourcentage_rep_c(0.0);
            rq.setPourcentage_rep_d(0.0);
            rq.setPourcentage(0.0);
        } else {
            Integer nb_bonne_rep = 0;
            char bonne_rep = 'A';
            for (Reponse reponse : reponses) {
                // récupère le nb de participants qui ont choisi cette réponse
                Integer value = reponse_dao.get_repondant_rep(reponse.getId(), date_participation).size();
                nb_reps.add(value);
                if (reponse.getIs_bonne_reponse() == 1) {
                    // nombre de partiipant ayant selectionnée cette bonne réponse 
                    nb_bonne_rep = value;
                    rq.setBonne_rep(String.valueOf(bonne_rep));
                }
                bonne_rep++;
            }
            rq.setPourcentage_rep_a((double) nb_reps.get(0) * 100 / nb_participants);
            rq.setPourcentage_rep_b((double) nb_reps.get(1) * 100 / nb_participants);
            rq.setPourcentage_rep_c((double) nb_reps.get(2) * 100 / nb_participants);
            rq.setPourcentage_rep_d((double) nb_reps.get(3) * 100 / nb_participants);
            rq.setPourcentage((double) nb_bonne_rep * 100 / nb_participants);
        }
        return rq;
    }
}
