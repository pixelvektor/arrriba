package arrriba;

/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Arrriba extends Application {
    
    /** Startet das Spiel mit dem Hauptfenster.
     * @param stage Stage auf der das Spiel laufen soll.
     * @throws Exception n/a
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/Arrriba.fxml"));
        
        Scene scene = new Scene(root);

        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    /** Hauptmethode.
     * @param args Uebergabeparameter werden ignoriert.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
