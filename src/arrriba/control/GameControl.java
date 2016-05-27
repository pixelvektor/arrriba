/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import arrriba.model.Barrel;
import arrriba.model.Box;
import arrriba.model.Obstacle;
import arrriba.model.Puffer;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    private Shape activeShape = null;
    
    /** Alle Gegenstaende auf dem Spielfeld. */
    private final ArrayList<Obstacle> obstacles = new ArrayList<>();
    
    private final EventHandler<MouseEvent> shapeOnMousePressedEH;
    
    private final EventHandler<MouseEvent> shapeOnMouseDraggedEH;
    
    private double origSceneX, origSceneY, origTranslateX, origTranslateY;

    public GameControl() {
        this.shapeOnMousePressedEH = (MouseEvent e) -> {
            origSceneX = e.getSceneX();
            origSceneY = e.getSceneY();
            origTranslateX = ((Obstacle) ((Shape) e.getSource()).getUserData()).getPosX();
            origTranslateY = ((Obstacle) ((Shape) e.getSource()).getUserData()).getPosY();
            
            activeShape = (Shape) e.getSource();
            activeShape.toFront();
        };
        
        this.shapeOnMouseDraggedEH = (MouseEvent e) -> {
            double newTranslateX = origTranslateX + e.getSceneX() - origSceneX;
            double newTranslateY = origTranslateY + e.getSceneY() - origSceneY;
            
            ((Obstacle) ((Shape) (e.getSource())).getUserData()).setPosX(newTranslateX);
            ((Obstacle) ((Shape) (e.getSource())).getUserData()).setPosY(newTranslateY);
        };
    }
    
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
    
    /** Erstellt ein neues Fass.
     * Fass wird dem Spielfeld hinzugefuegt.
     */
    @FXML
    public void onBarrelMenuItem() {
        Barrel barrel = new Barrel(50, 165, 10);
        barrel.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
        barrel.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        barrel.addObserver(this);
        obstacles.add(barrel);
        gameArea.getChildren().add(barrel.getShape());
        activeShape = barrel.getShape();
    }
    
    /** Erstellt eine neue Kiste.
     * Kiste wird dem Spielfeld hinzugefuegt.
     */
    @FXML
    public void onBoxMenuItem() {
        Box box = new Box(100, 130, 70);
        box.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
        box.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        box.addObserver(this);
        obstacles.add(box);
        gameArea.getChildren().add(box.getShape());
        activeShape = box.getShape();
    }
    
    @FXML
    public void onPufferMenuItem() {
        Puffer puffer = new Puffer(100, 130, 15);
        puffer.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
        puffer.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        puffer.addObserver(this);
        obstacles.add(puffer);
        gameArea.getChildren().add(puffer.getShape());
        activeShape = puffer.getShape();
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
    
    @FXML
    public void onSizeSlider() {
        double roundedSlider = (double) Math.round(sizeSlider.getValue() * 10) / 10;
        ((Obstacle) activeShape.getUserData()).setSize(roundedSlider);
        sizeNTF.setValue(roundedSlider);
    }

    @FXML
    public void onSizeNTF() {
        System.out.println(sizeNTF.getValue());
    }
}
