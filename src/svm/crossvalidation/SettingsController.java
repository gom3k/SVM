package svm.crossvalidation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Gomex
 */
public class SettingsController implements Initializable {

    public static String getBatchSize;
    public static String getCaliModel;
    public static String getC;
    public static String getChecksTurnedOff;
    public static String getDebugMode;
    public static String getdoNotCheckCapabilities;
    public static String getEpsilon;
    public static String getFilterMode;
    public static String getFunkcjaKernel;
    public static String getMiejscepoprzecinku;
    public static String getNumFolds;
    public static String getRandomSeed;
    public static String getToleranceParameter;

    @FXML
    private Button btnSaveSettings;

    @FXML
    private TextField batchsize;
    @FXML
    private ChoiceBox calimodel;
    @FXML
    private TextField c;
    @FXML
    private ChoiceBox checksoff;
    @FXML
    private ChoiceBox debugmode;
    @FXML
    private ChoiceBox dontcheck;
    @FXML
    private TextField epsilon;
    @FXML
    private ChoiceBox filtermode;
    @FXML
    private ChoiceBox funkcjakernel;
    @FXML
    private TextField miejscepoprzecinku;
    @FXML
    private TextField numfolds;
    @FXML
    private TextField randomseed;
    @FXML
    private TextField tolePara;

    @FXML
    private void saveSettings(ActionEvent event) {
        getBatchSize = batchsize.getText();
        getCaliModel = (String) calimodel.getValue();
        getC = c.getText();
        getChecksTurnedOff = (String) checksoff.getValue();
        getDebugMode = (String) debugmode.getValue();
        getdoNotCheckCapabilities = (String) dontcheck.getValue();
        getEpsilon = epsilon.getText();
        getFilterMode = (String) filtermode.getValue();
        getFunkcjaKernel = (String) funkcjakernel.getValue();
        getMiejscepoprzecinku = miejscepoprzecinku.getText();
        getNumFolds = numfolds.getText();
        getRandomSeed = randomseed.getText();
        getToleranceParameter = tolePara.getText();
        
        Stage stageCloseWindow = (Stage) btnSaveSettings.getScene().getWindow();
        stageCloseWindow.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        calimodel.getItems().add("False");
        calimodel.getItems().add("True");
        calimodel.getSelectionModel().select("False");
        checksoff.getItems().add("False");
        checksoff.getItems().add("True");
        checksoff.getSelectionModel().select("False");
        debugmode.getItems().add("False");
        debugmode.getItems().add("True");
        debugmode.getSelectionModel().select("False");
        dontcheck.getItems().add("False");
        dontcheck.getItems().add("True");
        dontcheck.getSelectionModel().select("False");
        filtermode.getItems().add("Standaryzuj");
        filtermode.getItems().add("Normalizuj");
        filtermode.getItems().add("Wyłączony");
        filtermode.getSelectionModel().select("Standaryzuj");
        funkcjakernel.getItems().add("Puk");
        funkcjakernel.getItems().add("RBFKernel");
        funkcjakernel.getItems().add("PolyKernel");
        funkcjakernel.getItems().add("StringKernel");
        funkcjakernel.getItems().add("NormalizedPolyKernel");
        funkcjakernel.getSelectionModel().select("PolyKernel");
    }
}
