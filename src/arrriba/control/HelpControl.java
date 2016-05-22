/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author fabian
 */
public class HelpControl implements Initializable {
    @FXML
    private Button okButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    @FXML
    public void onOkButton() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
    
}
