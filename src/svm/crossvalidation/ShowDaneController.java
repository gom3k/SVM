package svm.crossvalidation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ShowDaneController implements Initializable {

    @FXML
    private TableView<Instance> showDane;
    @FXML
    private Button Zamknij;

    private final Instances instances;

    public void btnClose() {
        Stage stageCloseLogowanie = (Stage) Zamknij.getScene().getWindow();
        stageCloseLogowanie.close();
    }

    public ShowDaneController(Instances instances) {
        this.instances = instances;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showDane.setEditable(true);
        showDane.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList columns = showDane.getColumns();
        columns.clear();
        Enumeration<Attribute> iterator = instances.enumerateAttributes();
        while (iterator.hasMoreElements()) {
            Attribute attribute = iterator.nextElement();
            TableColumn column = new TableColumn(attribute.name());
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures, ObservableValue>() {
                @Override
                public ObservableValue call(TableColumn.CellDataFeatures cellDataFeatures) {
                    Instance instance = (Instance) cellDataFeatures.getValue();
                    if (attribute.isDate() || attribute.isNominal() || attribute.isString()) {
                        return new SimpleStringProperty(instance.stringValue(attribute));
                    } else if (attribute.isNumeric()) {
                        return new SimpleStringProperty(String.valueOf(instance.value(attribute)));
                    } else {
                        return new SimpleStringProperty("-");
                    }
                }
            });
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
                public void handle(TableColumn.CellEditEvent cellEditEvent) {
                    TablePosition position = cellEditEvent.getTablePosition();
                    Instance instance = instances.get(position.getRow());
                    Attribute attribute1 = instance.attribute(position.getColumn());
                    if (attribute1.isNominal() || attribute1.isString()) {
                        instance.setValue(position.getColumn(), cellEditEvent.getNewValue().toString());
                    } else if (attribute1.isNumeric()) {
                        instance.setValue(position.getColumn(), Double.parseDouble(cellEditEvent.getNewValue().toString()));
                    }
                }
            });
            columns.add(column);
        }
        Enumeration<Instance> instanceEnumeration = instances.enumerateInstances();
        while (instanceEnumeration.hasMoreElements()) {
            showDane.getItems().add(instanceEnumeration.nextElement());
        }
    }
}
