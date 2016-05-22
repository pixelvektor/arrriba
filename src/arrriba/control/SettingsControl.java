/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author fabian
 */
public class SettingsControl implements Initializable {
    @FXML
    private ChoiceBox materialChoiceBox1;
    
    @FXML
    private ChoiceBox materialChoiceBox2;
    
    @FXML
    private ChoiceBox materialChoiceBox3;
    
    @FXML
    private Button notOkButton;
    
    @FXML
    private Button okButton;
    
    private ArrayList<ChoiceBox> choiceBoxes = new ArrayList<>();
    
    public SettingsControl() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBoxes.add(materialChoiceBox1);
        choiceBoxes.add(materialChoiceBox2);
        choiceBoxes.add(materialChoiceBox3);
        
        int i = 0;
        for (ChoiceBox choiceBox : choiceBoxes) {
            choiceBox.setItems(FXCollections.observableArrayList(
                "Holz", "Kunststoff", "Metall", "Schwamm"));
            choiceBox.getSelectionModel().select(i);
            i++;
        }
    }
    
    @FXML
    public void onNotOkButton() {
        Stage stage = (Stage) notOkButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void onOkButton() {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
    
}
