package svm.crossvalidation;

import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.functions.supportVector.SMOset;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.classifiers.functions.supportVector.Puk;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.functions.supportVector.StringKernel;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;

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
    private Label labelLiczbafoldow;
    @FXML
    private TextArea poleWyniku;
    @FXML
    private RadioButton wyborA;
    @FXML
    private RadioButton wyborB;
    @FXML
    private TextField numberoffolds;
    @FXML
    private Button btnShow;
    @FXML
    private Button btnUstawienieKlasyfikatora;
    @FXML
    private Button btnUruchomKlasyfikatora;

    private File crossvalidationDataFile;
    private Stage stage;
    Instances crossvalidationInstances = null;
    private StringBuilder WynikString;

    @FXML
    private void btnPrzegladaj(ActionEvent event) {
        try {
            crossvalidationDataFile = openFile();
            labelNazwaPliku.setText("Wybrano plik " + crossvalidationDataFile.getName() + " do wczytania.");
            String extension;
            int dot = crossvalidationDataFile.getName().lastIndexOf(".");
            extension = crossvalidationDataFile.getName().substring(dot + 1, crossvalidationDataFile.getName().length());
            WczytajDane loadInstances = new WczytajDane(extension).invoke(crossvalidationDataFile);
            if (loadInstances.is()) {
                return;
            }
            crossvalidationInstances = loadInstances.getData();
            labelIloscWczytanych.setText("Wczytano " + crossvalidationInstances.numInstances() + " obiektów.");
//            poleWyniku.setText("Już jest wczytany plik.");
            btnShow.setVisible(true);
            btnUstawienieKlasyfikatora.setVisible(true);
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

            SMO smo = new SMO();

            //BatchSize
            smo.setBatchSize(SettingsController.getBatchSize);
            //BuildCalibrationModel
            switch (SettingsController.getCaliModel) {
                case "False":
                    smo.setBuildCalibrationModels(false);
                    break;
                case "True":
                    smo.setBuildCalibrationModels(true);
                    break;
            }
            //C
            smo.setC(Double.parseDouble(SettingsController.getC));
            //ChecksTurnedOff <<----------- coś tu nie tak
            switch (SettingsController.getChecksTurnedOff) {
                case "False":
                    smo.setChecksTurnedOff(false);
                    break;
                case "True":
                    smo.setChecksTurnedOff(true);
                    break;
            }
            //Debug
            switch (SettingsController.getDebugMode) {
                case "False":
                    smo.setDebug(false);
                    break;
                case "True":
                    smo.setDebug(true);
                    break;
            }
            //DoNotCheckCapabilities
            switch (SettingsController.getdoNotCheckCapabilities) {
                case "False":
                    smo.setDoNotCheckCapabilities(false);
                    break;
                case "True":
                    smo.setDoNotCheckCapabilities(true);
                    break;
            }
            //Epsilon
            smo.setEpsilon(Double.parseDouble(SettingsController.getEpsilon));
            //Filder Mode
            SelectedTag selectedtag = smo.getFilterType();
            Tag[] tagi = selectedtag.getTags();
            Tag normalize = null;
            Tag standarize = null;
            Tag disabled = null;
            for (Tag tag : tagi) {
                if (tag.getReadable().equals("Normalize training data")) {
                    normalize = tag;
                } else if (tag.getReadable().equals("Standardize training data")) {
                    standarize = tag;
                } else if (tag.getReadable().equals("No normalization/standardization")) {
                    disabled = tag;
                }
            }
            switch (SettingsController.getFilterMode) {
                case "Normalizuj":
                    smo.setFilterType(new SelectedTag(normalize.getID(), tagi));
                    break;
                case "Standaryzuj":
                    smo.setFilterType(new SelectedTag(standarize.getID(), tagi));
                    break;
                case "Wyłączony":
                    smo.setFilterType(new SelectedTag(disabled.getID(), tagi));
                    break;
            }
            //Funkcja Kernel
            switch (SettingsController.getFunkcjaKernel) {
                case "Puk":
                    smo.setKernel(new Puk());
                    break;
                case "RBFKernel":
                    smo.setKernel(new RBFKernel());
                    break;
                case "PolyKernel":
                    smo.setKernel(new PolyKernel());
                    break;
                case "StringKernel":
                    smo.setKernel(new StringKernel());
                    break;
                case "NormalizedPolyKernel":
                    smo.setKernel(new NormalizedPolyKernel());
                    break;
            }
            //Liczba miejsc po przecinku
            smo.setNumDecimalPlaces(Integer.parseInt(SettingsController.getMiejscepoprzecinku));
            //NumFolds
            smo.setNumFolds(Integer.parseInt(SettingsController.getNumFolds));
            //RandomSeed
            smo.setRandomSeed(Integer.parseInt(SettingsController.getRandomSeed));
            //Tolerancja Parameter
            smo.setToleranceParameter(Double.parseDouble(SettingsController.getToleranceParameter));

            smo.buildClassifier(data);
            Evaluation eval = new Evaluation(data);
            //Cross-validation
            if (wyborA.isSelected()) {
                eval.crossValidateModel(smo, data, Integer.parseInt(numberoffolds.getText()), new Random(1)); //Wybor probek deterministyczny
            }
            if (wyborB.isSelected()) {
                eval.crossValidateModel(smo, data, Integer.parseInt(numberoffolds.getText()), new Random()); //Wybor probek losowy
            }

            StringBuilder builder = new StringBuilder();
            try {
                builder.append(eval.toSummaryString("Metoda Cross-Validation:", false) + "\n\n");
//                builder.append("Liczba wszystkich obiektow testowanych = " + eval.numInstances() + "\n");
//                builder.append("Liczba poprawnie sklasyfikowanych = " + eval.correct() + "\n");
//                builder.append("Procent poprawnie sklasyfikowanych = " + eval.pctCorrect() + "\n");
//                builder.append("Liczba niepoprawnie sklasyfikowanych = " + eval.incorrect() + "\n");
//                builder.append("Procent niepoprawnie sklasyfikowanych = " + eval.pctIncorrect() + "\n");
//                builder.append("Liczba niesklasyfikowanych = " + eval.unclassified() + "\n");
//                builder.append("Procent niesklasyfikowanych = " + eval.pctUnclassified() + "\n\n");
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
            Integer.parseInt(numberoffolds.getText());
        } catch (Exception e) {
            poleWyniku.setText("Proszę ustawiać dokładnie w ustawieniach Klasyfikatora");
        }
    }

    public void btnSaveInTxt() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Plik tekstowy .txt", "*.txt"));
            File file = chooser.showSaveDialog(stage);

            BufferedWriter savein = new BufferedWriter(new FileWriter(file));
            savein.write(WynikString.toString());
            savein.close();
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
        String s = "Program służący do uczenia klasyfikatora SVM (SMO) metodą Cross-Validation i obliczania wyników klasyfikacji na danych wejściowych wykonany w technologii JavaFXML. \n\nMade by Kamil Dudek. ";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, s, ButtonType.OK);
        alert.setTitle("About");
        alert.setHeaderText("O programie.");
        alert.showAndWait();
//        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
//            System.out.println("Czas uzywania programu w sekundach: ");
//            Platform.exit(); 
//        }
    }

    public void showDane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowDane.fxml"));
            ShowDaneController controller = new ShowDaneController(crossvalidationInstances);
            loader.setController(controller);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Dane " + crossvalidationDataFile.getName());
            stage.show();
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd wczytywania danych.");
            alert.setContentText("Proszę najpierw wczytać plik.");
            alert.showAndWait();
        }
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
            
            btnUruchomKlasyfikatora.setVisible(true);
            labelLiczbafoldow.setVisible(true);
            numberoffolds.setVisible(true);
            wyborA.setVisible(true);
            wyborB.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
