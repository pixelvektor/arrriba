/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */


import arrriba.model.Ball;
import arrriba.model.Obstacle;
import arrriba.model.ObstacleListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author fabian
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button ball0;
    
    private Ball ball = new Ball();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ball0.setLayoutX(ball.getPosX());
        ball0.setLayoutY(ball.getPosY());
    }
    
    public void move() {
        ball.rollin();
        ball.addListener(new ObstacleListener() {
            @Override
            public void onPositionChange() {
                updatePosition();
            }
        });
    }
    
    private void updatePosition() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ball0.setLayoutX(ball.getPosX());
            }
        });
    }
}
