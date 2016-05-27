/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import arrriba.model.Barrel;
import arrriba.model.Box;
import arrriba.model.Obstacle;
import arrriba.view.NumberTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
public class GameControl implements Initializable, Observer {
    // Menuebar
    @FXML
    private MenuBar menuBar;
    
    // Einstellungen
    @FXML
    private Slider sizeSlider;
    
    @FXML
    private NumberTextField sizeNTF;
    
    // Spielfeld
    @FXML
    private Pane gameArea;
    
    /** Angewaehltes Shape. */
    private Shape active = null;
    
    private ArrayList<Shape> shapes = new ArrayList<>();
    
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
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
        Barrel barrel = new Barrel(50, 165, 10);
        obstacles.add(barrel);
        
        Circle circle = (Circle) obstacles.get(0).getShape();
        shapes.add(circle);
        gameArea.getChildren().add(circle);
        
        setPosition(circle, true);
    }
    
    @FXML
    public void onBoxMenuItem() {
        Box box = new Box(100, 130, 70);
        obstacles.add(box);
        Rectangle rect = (Rectangle) box.getShape();
        shapes.add(rect);
        
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
    }
    
    @FXML
    public void onSizeSlider() {
        double roundedSlider = (double) Math.round(sizeSlider.getValue() * 10) / 10;
        obstacles.get(0).setSize(roundedSlider);
        sizeNTF.setValue(roundedSlider);
//        System.out.println(sizeNTF.getValue());
    }

    @FXML
    public void onSizeNTF() {
        System.out.println(sizeNTF.getValue());
    }
}
