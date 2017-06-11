package svm.crossvalidation;

import javafx.scene.control.Alert;
import weka.core.Instances;
import weka.core.converters.AbstractFileLoader;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;

public class WczytajDane {
    private boolean wynik;
    private String extension;
    private Instances data;

    public WczytajDane(String extension) {
        this.extension = extension;
        this.data = data;
    }

    boolean is() {
        return wynik;
    }

    public Instances getData() {
        return data;
    }

    public WczytajDane invoke(File file) {
        try {
            if (extension.equals("arff")) {
                ArffLoader loader = new ArffLoader();
                data = ladujData(loader, file);
            }

            if (extension.equals("csv")) {
                CSVLoader loader = new CSVLoader();
                data = ladujData(loader, file);
            }
        } catch(IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd wczytywania danych");
            alert.setTitle(e.getMessage());
            alert.showAndWait();
            wynik = true;
            return this;
        }
        wynik = false;
        return this;
    }

    private Instances ladujData(AbstractFileLoader loader, File file) throws IOException {
        Instances data;
        loader.setFile(file);
        data = loader.getDataSet();
        return data;
    }
}
