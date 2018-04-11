/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_resultat;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Point;
import java.io.File;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import opisiame.model.Rep_eleves_quiz;
import opisiame.model.Reponse_question;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Sandratra
 */
public class Choix_exportController implements Initializable {

    @FXML
    AnchorPane content;

    ObservableList<Reponse_question> reponse_questions;
    ObservableList<Rep_eleves_quiz> resultats_eleves;
    String onglet_actif;

    public void setReponse_questions(ObservableList<Reponse_question> reponse_questions) {
        this.reponse_questions = reponse_questions;
    }

    public void setResultatsEleves(ObservableList<Rep_eleves_quiz> res_eleves) {
        this.resultats_eleves = res_eleves;
    }

    public void setOngletActif(String onglet) {
        this.onglet_actif = onglet;
    }

    @FXML
    public void excel_export() {

        File excel_file = choix_chemin_enregistrement("Excel files (*.xls)", "*.xls");

        if (onglet_actif.equals("questions")) {
            if (excel_file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Resultat par question");
                sheet.autoSizeColumn(5);
                create_data1(sheet, 0, "Question", "Pourcentage reponse A", "Pourcentage reponse B", "Pourcentage reponse C", "Pourcentage reponse D", "Pourcentage bonne réponse");

                Row row = sheet.getRow(0);
                HSSFCellStyle cellStyle = null;
                HSSFFont font = wb.createFont();
                font.setBold(true);
                cellStyle = wb.createCellStyle();
                cellStyle.setFont(font);
                row.setRowStyle(cellStyle);

                for (int i = 0; i < reponse_questions.size(); i++) {
                    Reponse_question rq = reponse_questions.get(i);
                    create_data1(sheet, i + 1, rq.getQuestion(), rq.getStr_pourcentage_rep_a(), rq.getStr_pourcentage_rep_b(), rq.getStr_pourcentage_rep_c(), rq.getStr_pourcentage_rep_d(), rq.getStr_pourcentage());
                }

                FileOutputStream fileOut;
                try {
                    fileOut = new FileOutputStream(excel_file);
                    wb.write(fileOut);
                    fileOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (onglet_actif.equals("eleves")) {
            if (excel_file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Resultat des étudiants");
                sheet.autoSizeColumn(5);
                create_data2(sheet, 0, "Nom", "Prénom", "N° étudiant", "Note", "Pourcentage");

                Row row = sheet.getRow(0);
                HSSFCellStyle cellStyle = null;
                HSSFFont font = wb.createFont();
                font.setBold(true);
                cellStyle = wb.createCellStyle();
                cellStyle.setFont(font);
                row.setRowStyle(cellStyle);

                for (int i = 0; i < resultats_eleves.size(); i++) {
                    Rep_eleves_quiz re = resultats_eleves.get(i);
                    create_data2(sheet, i + 1, re.getNom_eleve(), re.getPrenom_eleve(), re.getNum_eleve().toString(), re.getNote_eleve().toString(), re.getPourcent_eleve().toString());
                }

                FileOutputStream fileOut;
                try {
                    fileOut = new FileOutputStream(excel_file);
                    wb.write(fileOut);
                    fileOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (onglet_actif.equals("eleves_pas_num")) {
            if (excel_file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Resultat des étudiants");
                sheet.autoSizeColumn(4);
                create_data3(sheet, 0, "Nom", "Prénom", "Note", "Pourcentage");

                Row row = sheet.getRow(0);
                HSSFCellStyle cellStyle = null;
                HSSFFont font = wb.createFont();
                font.setBold(true);
                cellStyle = wb.createCellStyle();
                cellStyle.setFont(font);
                row.setRowStyle(cellStyle);

                for (int i = 0; i < resultats_eleves.size(); i++) {
                    Rep_eleves_quiz re = resultats_eleves.get(i);
                    create_data3(sheet, i + 1, re.getNom_eleve(), re.getPrenom_eleve(), re.getNote_eleve().toString(), re.getPourcent_eleve().toString());
                }

                FileOutputStream fileOut;
                try {
                    fileOut = new FileOutputStream(excel_file);
                    wb.write(fileOut);
                    fileOut.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        close_window();
    }

    public void create_data1(HSSFSheet sheet, Integer i, String question, String rep_a, String rep_b, String rep_c, String rep_d, String pourcentage) {
        HSSFRow row = sheet.createRow(i); // ligne i

        HSSFCell cell_question = row.createCell((short) 0); // colonne 0
        cell_question.setCellValue(question);

        HSSFCell cell_rep_a = row.createCell((short) 1); // colonne 1
        cell_rep_a.setCellValue(rep_a);

        HSSFCell cell_rep_b = row.createCell((short) 2); // colonne 2
        cell_rep_b.setCellValue(rep_b);

        HSSFCell cell_rep_c = row.createCell((short) 3); // colonne 3
        cell_rep_c.setCellValue(rep_c);

        HSSFCell cell_rep_d = row.createCell((short) 4); // colonne 4
        cell_rep_d.setCellValue(rep_d);

        HSSFCell cell_pourcentage = row.createCell((short) 5); // colonne 5
        cell_pourcentage.setCellValue(pourcentage);
    }

    public void create_data2(HSSFSheet sheet, int i, String nom, String prenom, String n_etudiant, String note, String pourcentage) {
        HSSFRow row = sheet.createRow(i); // ligne i

        HSSFCell cell_n = row.createCell((short) 0); // colonne 0
        cell_n.setCellValue(nom);

        HSSFCell cell_p = row.createCell((short) 1); // colonne 1
        cell_p.setCellValue(prenom);

        HSSFCell cell_num = row.createCell((short) 2); // colonne 2
        cell_num.setCellValue(n_etudiant);

        HSSFCell cell_rep_a = row.createCell((short) 3); // colonne 3
        cell_rep_a.setCellValue(note);

        HSSFCell cell_pourcentage = row.createCell((short) 4); // colonne 4
        cell_pourcentage.setCellValue(pourcentage);
    }

    private void create_data3(HSSFSheet sheet, int i, String nom, String prenom, String note, String pourcentage) {
        HSSFRow row = sheet.createRow(i); // ligne i

        HSSFCell cell_n = row.createCell((short) 0); // colonne 0
        cell_n.setCellValue(nom);

        HSSFCell cell_p = row.createCell((short) 1); // colonne 1
        cell_p.setCellValue(prenom);

        HSSFCell cell_rep_a = row.createCell((short) 2); // colonne 2
        cell_rep_a.setCellValue(note);

        HSSFCell cell_pourcentage = row.createCell((short) 3); // colonne 3
        cell_pourcentage.setCellValue(pourcentage);
    }

    public File choix_chemin_enregistrement(String description, String extension) {
        Stage stage = (Stage) content.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choix d'enregistrement du fichier");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
        fileChooser.getExtensionFilters().add(extFilter);
        File selected_directory = fileChooser.showSaveDialog(stage);
        //System.out.println("file : " + selected_directory.getAbsolutePath());
        return selected_directory;
    }

    @FXML
    public void pdf_export() {
        File pdf_file = choix_chemin_enregistrement("PDF files (*.pdf)", "*.pdf");

        if (onglet_actif.equals("questions")) {

            if (pdf_file != null) {
                Document document = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
                    document.open();
                    document.add(new Paragraph("Résultat Quiz"));

                    Table tableau = new Table(6, reponse_questions.size());
                    tableau.setAutoFillEmptyCells(true);
                    tableau.setPadding(2);

                    Cell cell = new Cell("Question");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Pourcentage reponse A");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Pourcentage reponse B");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Pourcentage reponse C");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Pourcentage reponse D");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Pourcentage bonne réponse");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    tableau.endHeaders();
                    tableau.setWidth(100);
                    fill_data_pdf(tableau);
                    document.add(tableau);
                } catch (DocumentException | IOException de) {
                    de.printStackTrace();
                }
                document.close();
            }

        } else if (onglet_actif.equals("eleves")) {

            if (pdf_file != null) {
                Document document = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
                    document.open();
                    document.add(new Paragraph("Résultats des étudiants"));

                    Table tableau = new Table(5, resultats_eleves.size());
                    tableau.setAutoFillEmptyCells(true);
                    tableau.setPadding(2);

                    Cell cell = new Cell("Nom");
                    cell.setHeader(true);
                    tableau.addCell(cell);
                    
                    cell = new Cell("Prénom");
                    cell.setHeader(true);
                    tableau.addCell(cell);
                    
                    cell = new Cell("N° étudiant");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Note");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Pourcentage");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    tableau.endHeaders();
                    tableau.setWidth(100);
                    fill_data_pdf(tableau);
                    document.add(tableau);
                } catch (DocumentException | IOException de) {
                    de.printStackTrace();
                }
                document.close();
            }
        }else if (onglet_actif.equals("eleves_pas_num")) {

            if (pdf_file != null) {
                Document document = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
                    document.open();
                    document.add(new Paragraph("Résultats des étudiants"));

                    Table tableau = new Table(4, resultats_eleves.size());
                    tableau.setAutoFillEmptyCells(true);
                    tableau.setPadding(2);

                    Cell cell = new Cell("Nom");
                    cell.setHeader(true);
                    tableau.addCell(cell);
                    
                    cell = new Cell("Prénom");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Note");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    cell = new Cell("Pourcentage");
                    cell.setHeader(true);
                    tableau.addCell(cell);

                    tableau.endHeaders();
                    tableau.setWidth(100);
                    fill_data_pdf(tableau);
                    document.add(tableau);
                } catch (DocumentException | IOException de) {
                    de.printStackTrace();
                }
                document.close();
            }
        }

        close_window();
    }

    public void fill_data_pdf(Table table) throws BadElementException {
        if (onglet_actif.equals("questions")) {
            for (int i = 0; i < reponse_questions.size(); i++) {
                table.addCell(reponse_questions.get(i).getQuestion(), new Point(i + 1, 0));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_a(), new Point(i + 1, 1));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_b(), new Point(i + 1, 2));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_c(), new Point(i + 1, 3));
                table.addCell(reponse_questions.get(i).getStr_pourcentage_rep_d(), new Point(i + 1, 4));
                table.addCell(reponse_questions.get(i).getStr_pourcentage(), new Point(i + 1, 5));
            }
        } else if (onglet_actif.equals("eleves")) {
            for (int i = 0; i < resultats_eleves.size(); i++) {
                table.addCell(resultats_eleves.get(i).getNom_eleve(), new Point(i + 1, 0));
                table.addCell(resultats_eleves.get(i).getPrenom_eleve(), new Point(i + 1, 1));
                table.addCell(resultats_eleves.get(i).getNum_eleve().toString(), new Point(i + 1, 2));
                table.addCell(resultats_eleves.get(i).getNote_eleve().toString(), new Point(i + 1, 3));
                table.addCell(resultats_eleves.get(i).getPourcent_eleve().toString(), new Point(i + 1, 4));
            }
        }else if (onglet_actif.equals("eleves_pas_num")) {
            for (int i = 0; i < resultats_eleves.size(); i++) {
                table.addCell(resultats_eleves.get(i).getNom_eleve(), new Point(i + 1, 0));
                table.addCell(resultats_eleves.get(i).getPrenom_eleve(), new Point(i + 1, 1));
                table.addCell(resultats_eleves.get(i).getNote_eleve().toString(), new Point(i + 1, 2));
                table.addCell(resultats_eleves.get(i).getPourcent_eleve().toString(), new Point(i + 1, 3));
            }
        }

    }

    public void close_window() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
