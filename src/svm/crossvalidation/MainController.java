package svm.crossvalidation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

/**
 *
 * @author Gomex
 */
public class MainController implements Initializable {

    @FXML
    private Label labelNazwaPliku;
    @FXML
    private Label labelIloscWczytanych;
    @FXML
    private TextArea poleWyniku;

    private File crossvalidationDataFile;
    private Stage stage;
    private Instances crossvalidationInstances = null;
    private StringBuilder WynikString;

    private void Load(ActionEvent event) {
        String extension;
        int dot = crossvalidationDataFile.getName().lastIndexOf(".");
        extension = crossvalidationDataFile.getName().substring(dot + 1, crossvalidationDataFile.getName().length());
        LoadInstances loadInstances = new LoadInstances(extension).invoke(crossvalidationDataFile);
        if (loadInstances.is()) {
            return;
        }
        crossvalidationInstances = loadInstances.getData();
    }

    @FXML
    private void btnPrzegladajAction(ActionEvent event) {
        try {
            crossvalidationDataFile = openFile();
            labelNazwaPliku.setText("Wybrano plik " + crossvalidationDataFile.getName() + " do wczytania.");
            Load(event);
            labelIloscWczytanych.setText("Wczytano " + crossvalidationInstances.numInstances() + " obiektów.");
            poleWyniku.setText("Już jest wczytany plik, możesz uruchomić klasyfikator.");
        } catch (Exception e) {
            poleWyniku.setText("Co się stało? Wczytaj jeszcze raz!");
        }
    }

    private File openFile() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter arff = new FileChooser.ExtensionFilter("Plik arff", "*.arff");
        FileChooser.ExtensionFilter csv = new FileChooser.ExtensionFilter("Plik CSV", "*.csv");
        chooser.getExtensionFilters().add(arff);
        chooser.getExtensionFilters().add(csv);
        return chooser.showOpenDialog(stage);
    }

    @FXML
    private void btnUruchomKlasyfikator(ActionEvent event) throws Exception {
        try {

            Instances data = crossvalidationInstances;
            data.setClassIndex(data.numAttributes() - 1);

//            String[] options = {"-M"};
            SMO smo = new SMO();
//            smo.setOptions(options);
            smo.buildClassifier(data);

            Evaluation eval = new Evaluation(data);
            //Cross-validation
            if (SettingsController.getWyborA) {
                eval.crossValidateModel(smo, data, Integer.parseInt(SettingsController.getFolds), new Random()); //Wybor probek losowy
            }
            if (SettingsController.getWyborB) {
                eval.crossValidateModel(smo, data, Integer.parseInt(SettingsController.getFolds), new Random(1)); //Wybor probek deterministyczny
            }

            StringBuilder builder = new StringBuilder();
            try {
                builder.append("Liczba fold'ów: " + SettingsController.getFolds + "\n");
                if (SettingsController.getWyborA) {
                    builder.append("Wybrałeś próbkę losowy.\n\n");
                }
                if (SettingsController.getWyborB) {
                    builder.append("Wybrałeś próbkę deterministyczny.\n\n");
                }

                builder.append(eval.toSummaryString("Wyniki dla metody CV:", false));
                builder.append("\n\n");
                builder.append("Liczba wszystkich obiektow testowanych = " + eval.numInstances() + "\n");
                builder.append("Liczba poprawnie sklasyfikowanych = " + eval.correct() + "\n");
                builder.append("Procent poprawnie sklasyfikowanych = " + eval.pctCorrect() + "\n");
                builder.append("Liczba niepoprawnie sklasyfikowanych = " + eval.incorrect() + "\n");
                builder.append("Procent niepoprawnie sklasyfikowanych = " + eval.pctIncorrect() + "\n");
                builder.append("Liczba niesklasyfikowanych = " + eval.unclassified() + "\n");
                builder.append("Procent niesklasyfikowanych = " + eval.pctUnclassified() + "\n\n");
                builder.append(eval.toClassDetailsString("Klasy decyzyjne:") + "\n\n");
//            builder.append(eval.toCumulativeMarginDistributionString()+"\n\n");
                builder.append(eval.toMatrixString("Macierz:") + "\n\n");
                poleWyniku.setEditable(false);
                poleWyniku.setText(builder.toString());
                WynikString = builder;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            poleWyniku.setText("Proszę najpierw wczytać pliku!");
            e.printStackTrace();
        }
        try {
            Integer.parseInt(SettingsController.getFolds);
        } catch (Exception e) {
            poleWyniku.setText("Proszę podać tylko liczbowe w polu fold'ów!");
        }
    }

    public void saveintxt() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik tekstowy .txt", "*.txt"));
            File file = chooser.showSaveDialog(stage);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(WynikString.toString());
            writer.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd zapisu pliku.");
            alert.setContentText("Proszę zapisać ponownie.");
            alert.showAndWait();
        }
    }

    public void CzyscWynik() {
        poleWyniku.setText("");
        StringBuilder newbuilder = new StringBuilder();
        newbuilder.append("");
        WynikString = newbuilder;
    }

    public void zamknijOkno() {
        Platform.exit(); //Zakonczenie pracy programu
    }

    public void aboutProgram() {
        String s = "Jest to program obliczający klasyfikatora SVM (SMO) metodą Cross-Validation. \n\nMade by Kamil Dudek. ";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK);
        alert.setTitle("About");
        alert.setHeaderText("O programie.");
        alert.showAndWait();
//        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
//            System.out.println("Czas uzywania programu w sekundach: ");
//            Platform.exit(); 
//        }
    }

    public void settingklasyfikator() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Ustawienie klasyfikatora");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
