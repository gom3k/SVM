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
    public static String getFilterMode;
    public static String getFunkcjaKernel;

    @FXML
    private Button btnSaveSettings;

    @FXML
    private TextField c;
    @FXML
    private ChoiceBox filtermode;
    @FXML
    private ChoiceBox funkcjakernel;

    @FXML
    private void saveSettings(ActionEvent event) {
        getC = c.getText();
        getFilterMode = (String) filtermode.getValue();
        getFunkcjaKernel = (String) funkcjakernel.getValue();

        Stage stageCloseWindow = (Stage) btnSaveSettings.getScene().getWindow();
        stageCloseWindow.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        filtermode.getItems().add("Wyłączony");
        filtermode.getItems().add("Normalizuj");
        filtermode.getItems().add("Standaryzuj");
        filtermode.getSelectionModel().select("Standaryzuj");
        funkcjakernel.getItems().add("Puk");
        funkcjakernel.getItems().add("RBFKernel");
        funkcjakernel.getItems().add("PolyKernel");
        funkcjakernel.getItems().add("StringKernel");
        funkcjakernel.getItems().add("NormalizedPolyKernel");
        funkcjakernel.getSelectionModel().select("PolyKernel");
    }
}
