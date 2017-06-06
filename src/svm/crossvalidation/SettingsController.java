package svm.crossvalidation;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Gomex
 */
public class SettingsController implements Initializable {
    public static Boolean getWyborA;
    public static Boolean getWyborB;
    public static String getFolds;
    
    @FXML
    private TextField numberoffolds;
    @FXML
    private RadioButton wyborA;
    @FXML
    private RadioButton wyborB;
    @FXML
    private Button btnSaveSettings;
    
    @FXML
    private void saveSettings(ActionEvent event){
        getWyborA = wyborA.isSelected();
        getWyborB = wyborB.isSelected();
        getFolds = numberoffolds.getText();
        
        Stage stageCloseWindow = (Stage) btnSaveSettings.getScene().getWindow();
        stageCloseWindow.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
