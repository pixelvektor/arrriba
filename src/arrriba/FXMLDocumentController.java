package arrriba;


/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */

import arrriba.model.Ball;
import arrriba.model.GameModel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import arrriba.model.ModelListener;
import arrriba.model.Obstacle;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 *
 * @author fabian
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button ball0;
    
    @FXML
    private AnchorPane gameArea;
    
    private ArrayList<Shape> shapes = new ArrayList<>();
    
    private ArrayList<GameModel> balls = new ArrayList<>();
    
    private Ball ball;
    private Circle circle;
    

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void create(){

        ball = new Ball(40, 40.0, 40.0, 5.0, 5.0);
        balls.add(ball);

        
        circle = (Circle) balls.get(0).getShape();
        shapes.add(circle);
        gameArea.getChildren().add(circle);
        circle.setCenterX(ball.getPosX());
        circle.setCenterY(ball.getPosY());
        circle.setRadius(ball.getSize());
    }
    
    public void move() {
        ball.rollin();
        ball.addListener(new ModelListener() {
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
                circle.setCenterX(ball.getPosX());
                circle.setCenterY(ball.getPosY());
            }

            
        });
    }
}