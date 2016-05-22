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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
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
    public void onSettingsMenuItem() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/arrriba/view/Settings.fxml"));
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Einstellungen");
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void onBarrelMenuItem() {
        Circle circle = new Circle(40, 40, 30);
        shapes.add(circle);
        circle.setFill(Paint.valueOf("DODGERBLUE"));
        
        gameArea.getChildren().add(circle);
        
        setPosition(circle, true);
    }
    
    @FXML
    public void onBoxMenuItem() {
        Rectangle rect = new Rectangle(40, 60, Paint.valueOf("RED"));
        shapes.add(rect);
        rect.setX(100);
        rect.setY(200);
        
        gameArea.getChildren().add(rect);
        
        setPosition(rect);
    }
    
    @FXML
    public void onPufferMenuItem() {
        
    }
    
    @FXML
    public void onSpringMenuItem() {
        
    }
    
    private void setPosition(final Shape shape) {
        setPosition(shape, false);
    }
    
    private void setPosition(final Shape shape, final boolean isCircle) {
            shape.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
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

            shape.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    shape.setOnMouseMoved(null);
                }
            });
    }
}
