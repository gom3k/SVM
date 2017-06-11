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

    public static String getC;
    public static String getDebugMode;
    public static String getEpsilon;
    public static String getFilterMode;
    public static String getFunkcjaKernel;
    public static String getMiejscepoprzecinku;

    @FXML
    private Button btnSaveSettings;

    @FXML
    private TextField c;
    @FXML
    private ChoiceBox debugmode;
    @FXML
    private TextField epsilon;
    @FXML
    private ChoiceBox filtermode;
    @FXML
    private ChoiceBox funkcjakernel;
    @FXML
    private TextField miejscepoprzecinku;

    @FXML
    private void saveSettings(ActionEvent event) {
        getC = c.getText();
        getDebugMode = (String) debugmode.getValue();
        getEpsilon = epsilon.getText();
        getFilterMode = (String) filtermode.getValue();
        getFunkcjaKernel = (String) funkcjakernel.getValue();
        getMiejscepoprzecinku = miejscepoprzecinku.getText();

        Stage stageCloseWindow = (Stage) btnSaveSettings.getScene().getWindow();
        stageCloseWindow.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        debugmode.getItems().add("False");
        debugmode.getItems().add("True");
        debugmode.getSelectionModel().select("False");
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
