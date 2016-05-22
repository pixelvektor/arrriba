
/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */

import arrriba.model.Ball;
import arrriba.model.ObstacleListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

/**
 *
 * @author fabian
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button ball0;
    
    @FXML
    private Circle ball01;

    private Ball ball = new Ball(30, 20, 20, 4);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ball01.setCenterX(ball.getPosX());
        ball01.setCenterY(ball.getPosY());
        ball01.setRadius(ball.getSize());
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
                ball01.setCenterX(ball.getPosX());
                ball01.setCenterY(ball.getPosY());
            }
        });
    }
}
