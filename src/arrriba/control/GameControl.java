/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author fabian
 */
public class GameControl implements Initializable {
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private Pane gameArea;
    
    private ArrayList<Shape> shapes = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    /** Schliesst das Fenster und beendet das Programm.
     */
    @FXML
    public void onCloseMenuItem() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void onBarrelMenuItem() {
        Circle circle = new Circle(50, 165, 35);
        Image texture = new Image("/arrriba/view/Textur.png");
        circle.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        circle.getStyleClass().add("obstacle");
        shapes.add(circle);
        
        gameArea.getChildren().add(circle);
        
        setPosition(circle, true);
    }
    
    @FXML
    public void onBoxMenuItem() {
        Rectangle rect = new Rectangle(50, 70);
        shapes.add(rect);
        
        Image textur = new Image("/arrriba/view/Textur.png");
        rect.setFill(new ImagePattern(textur, 0, 0, 1, 1, true));
        rect.setX(100);
        rect.setY(130);
        rect.getStyleClass().add("obstacle");
//        rect.setArcWidth(5);
//        rect.setArcHeight(5);
        gameArea.getChildren().add(rect); 
        setPosition(rect);
    }
    
    @FXML
    public void onPufferMenuItem() {
        
    }
    
    @FXML
    public void onSpringMenuItem() {
        
    }
    
    @FXML
    public void onHelpMenuItem() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/arrriba/view/Help.fxml"));
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Hilfe");
            stage.setResizable(false);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setPosition(final Shape shape) {
        setPosition(shape, false);
    }
    
    private void setPosition(final Shape shape, final boolean isCircle) {
        shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shape.toFront();
                if (isCircle) {
                    Circle c = (Circle) shape;
                    c.setCenterX(event.getX());
                    c.setCenterY(event.getY());
                } else {
                    Rectangle r = (Rectangle) shape;
                    r.setX(event.getX()-20);
                    r.setY(event.getY()-30);
                }
            }
        });
//        shape.setOnMouseMoved(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (isCircle) {
//                    Circle c = (Circle) shape;
//                    c.setCenterX(event.getX());
//                    c.setCenterY(event.getY());
//                } else {
//                    Rectangle r = (Rectangle) shape;
//                    r.setX(event.getX()-20);
//                    r.setY(event.getY()-30);
//                }
//            }
//        });
//
//        shape.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                shape.setOnMouseMoved(null);
//            }
//        });
    }
}
