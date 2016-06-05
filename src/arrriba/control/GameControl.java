/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import arrriba.model.Ball;
import arrriba.model.Barrel;
import arrriba.model.Box;
import arrriba.model.GameModel;
import arrriba.model.Hole;
import arrriba.model.Ground;
import arrriba.model.Puffer;
import arrriba.model.Spring;
import arrriba.model.material.Plastic;
import arrriba.model.material.Material;
import arrriba.model.material.Metal;
import arrriba.model.material.Sponge;
import arrriba.model.material.Wood;
import arrriba.view.NumberTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
    // Konstanten
    /** Anzahl der Baelle im Spiel. */
    private static final int BALL_COUNT = 1;
    
    /** CSS-Klasse fuer das aktive Shape. */
    private static final String ACTIVE = "active";
    
    /** Zeit zwischen Frames in Millisekunden. */
    private static final int FRAME_RATE = 30;
    
    // FXML Variablen
    /** Menuebar. */
    @FXML
    private MenuBar menuBar;
    
// Einstellungen
    @FXML
    private Accordion settingsAccord;
    
    @FXML
    private TitledPane settingsPane0;
    
    @FXML
    private Slider sizeSlider;
    
    @FXML
    private NumberTextField sizeNTF;
    
    @FXML
    private Slider rotationSlider;
    
    @FXML
    private NumberTextField rotationNTF;
    
    @FXML
    private NumberTextField posXNTF;
    
    @FXML
    private NumberTextField posYNTF;
    
    @FXML
    private ChoiceBox<Material> materialMenu;
    
    @FXML
    private Button deleteButton;
    
    // Spielfeld
    @FXML
    private Pane gameArea;
  
    /** Materialien. */
    private final ArrayList<Material> materials = new ArrayList<>();
    
    /** Timer fuer die regelmaessige Berechnung. */
    private Timer timer;
    
    /** Speichert ob das Spiel laeuft. */
    private boolean play=false;
    
    /** Angewaehltes Shape. */
    private Shape activeShape = null;
    
    /** Alle Gegenstaende auf dem Spielfeld. */
    private final ArrayList<GameModel> obstacles = new ArrayList<>();
    
    /** Alle Baelle, die sich im Spiel befinden. */
    private final ArrayList<Ball> balls = new ArrayList<>();
    
    private final ArrayList<Hole> holes = new ArrayList<>();
    
    private final ArrayList<Rectangle> wallElements = new ArrayList<>();
    
    private final EventHandler<MouseEvent> shapeOnMousePressedEH;
    
    private final EventHandler<MouseEvent> shapeOnMouseDraggedEH;
    
    /** Hilfskoordinaten fuer die Verschiebung der Elemente. */
    private double origSceneX, origSceneY, origTranslateX, origTranslateY;
    
    /** Letzter Bildaufruf. */
    private long lastFrame = 0;
    Ground ground = new Ground();
    

    public GameControl() {
        this.shapeOnMousePressedEH = (MouseEvent e) -> {
            origSceneX = e.getSceneX();
            origSceneY = e.getSceneY();
            origTranslateX = ((GameModel) ((Shape) e.getSource()).getUserData()).getPosX();
            origTranslateY = ((GameModel) ((Shape) e.getSource()).getUserData()).getPosY();
            
            // Setzen des aktuellen Shapes als ausgewaehlt
            if (activeShape != null) {
                activeShape.getStyleClass().remove(ACTIVE);
            }
            activeShape = (Shape) e.getSource();
            activeShape.getStyleClass().add(ACTIVE);
            activeShape.toFront();
            
            // Laden der aktuellen Werte des Objektes in die Einstellungen
            sizeSlider.setValue(((GameModel) activeShape.getUserData()).getSize());
            sizeNTF.setValue(((GameModel) activeShape.getUserData()).getSize());
            rotationSlider.setValue(((GameModel) activeShape.getUserData()).getRotation());
            rotationNTF.setValue(((GameModel) activeShape.getUserData()).getRotation());
            posXNTF.setValue(((GameModel) activeShape.getUserData()).getPosX());
            posYNTF.setValue(((GameModel) activeShape.getUserData()).getPosY());
            
            // Materialmenue bei Baellen
            if (activeShape.getUserData().toString().contains("Ball")) {
                materialMenu.disableProperty().set(false);
                posXNTF.disableProperty().set(true);
                posYNTF.disableProperty().set(true);
                deleteButton.disableProperty().set(true);
                materialMenu.getSelectionModel().select(((Ball) activeShape.getUserData()).getMaterial());
            } else {
                materialMenu.disableProperty().set(true);
                posXNTF.disableProperty().set(false);
                posYNTF.disableProperty().set(false);
                deleteButton.disableProperty().set(false);
            }
        };
        
        this.shapeOnMouseDraggedEH = (MouseEvent e) -> {
            double newTranslateX = origTranslateX + e.getSceneX() - origSceneX;
            double newTranslateY = origTranslateY + e.getSceneY() - origSceneY;
            
            ((GameModel) ((Shape) (e.getSource())).getUserData()).setPosX(newTranslateX);
            ((GameModel) ((Shape) (e.getSource())).getUserData()).setPosY(newTranslateY);
        };
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Materialien erstellen
        materials.add(new Wood());
        materials.add(new Metal());
        materials.add(new Plastic());
        materials.add(new Sponge());
        
        // Materialmenue
        materialMenu.setItems(FXCollections.observableArrayList(materials));
        materialMenu.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ((Ball) activeShape.getUserData()).setMaterial(materials.get(newValue.intValue()));
            }
        });
        
        settingsAccord.setExpandedPane(settingsPane0);
        
        createBalls();
        
        // Erstellen des Timers fuer den Spielablauf
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                nextStep();
            }
        };
        
        timer = new Timer(true);
        timer.schedule(timerTask, 0, FRAME_RATE);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        for (Ball b : balls) {
            if (b.isFinished()) {
//                b.getShape().toFront();
                b.getShape().getStyleClass().add("finish");
            }
        }
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
        Barrel barrel = new Barrel(125, 165, 50);
        barrel.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
        barrel.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        barrel.addObserver(this);
        obstacles.add(barrel);
        gameArea.getChildren().add(barrel.getShape());
    }
    
    /** Erstellt eine neue Kiste.
     * Kiste wird dem Spielfeld hinzugefuegt.
     */
    @FXML
    public void onBoxMenuItem() {
        Box box = new Box(25, 140, 50);
        box.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
        box.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        box.addObserver(this);
        obstacles.add(box);
        gameArea.getChildren().add(box.getShape());
    }
    
    /** Erstellt einen neuen Kugelfisch.
     * Kugelfisch wird dem Spielfeld hinzugefuegt.
     */
    @FXML
    public void onPufferMenuItem() {
        Puffer puffer = new Puffer(200, 165, 50);
        puffer.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
        puffer.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        puffer.addObserver(this);
        obstacles.add(puffer);
        gameArea.getChildren().add(puffer.getShape());
    }
    
    /** Erstellt eine neue Sprungfeder.
     * Springfeder wird dem Spielfeld hinzugefuegt.
     */
    @FXML
    public void onSpringMenuItem() {
        Spring spring = new Spring(250, 140, 50);
        spring.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
        spring.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        spring.addObserver(this);
        obstacles.add(spring);
        gameArea.getChildren().add(spring.getShape());
    }
    
    @FXML
    public void onOberdeckMI() {
        System.out.println("arrriba.control.GameControl.onOberdeckMI()");
    }
    
    @FXML
    public void onZwischendeckMI() {
        System.out.println("arrriba.control.GameControl.onZwischendeckMI()");
    }
    
    @FXML
    public void onUnterdeckMI() {
        System.out.println("arrriba.control.GameControl.onUnterdeckMI()");
    }
    
    /** Ruft das Hilfefenster auf.
     */
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
    public void onGamePlay() {
        play = true;
    }
    
    @FXML
    public void onGameRetry() {
        play = false;
        
        for (Ball b : balls) {
            gameArea.getChildren().remove(b.getShape());
        }
        balls.removeAll(balls);
        lastFrame = 0;
        
        createBalls();
    }
    
    @FXML
    public void onGameReset() {
        play = false;
        
        gameArea.getChildren().remove(0, gameArea.getChildren().size());
        balls.removeAll(balls);
        obstacles.removeAll(obstacles);
        lastFrame = 0;
        
        createBalls();
    }
    
    
    @FXML
    public void onSizeSlider() {
        final double size = round(sizeSlider.getValue());
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setSize(size);
        }
        sizeNTF.setValue(size);
    }

    @FXML
    public void onSizeNTF() {
        final double origSize = sizeNTF.getValue();
        double size;
        
        if (origSize > 100) {
            size = 100;
        } else if (origSize < 20) {
            size = 20;
        } else {
            size = origSize;
        }
        
        sizeNTF.setText(Double.toString(size));
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setSize(size);
        }
        sizeSlider.setValue(size);
    }
    
    @FXML
    public void onRotationSlider() {
        final double rotation = round(rotationSlider.getValue());
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setRotation(rotation);
        }
        rotationNTF.setValue(rotation);
    }
    @FXML
    public void onRotationNTF() {
        double rotation = rotationNTF.getValue() % 360;
        rotationNTF.setText(Double.toString(rotation));
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setRotation(rotation);
        }
        rotationSlider.setValue(rotation);
    }
    
    @FXML
    public void onPosXNTF() {
        double posX = posXNTF.getValue();
        ((GameModel) activeShape.getUserData()).setPosX(posX);
    }
    
    @FXML
    public void onPosYNTF() {
        double posY = posYNTF.getValue();
        ((GameModel) activeShape.getUserData()).setPosY(posY);
    }
    
    @FXML
    public void onDeleteButton() {
        gameArea.getChildren().remove(activeShape);
        obstacles.remove((GameModel) activeShape.getUserData());
        activeShape = null;
        deleteButton.disableProperty().set(true);
    }
    
    /** Erstellt die Baelle auf dem Spielfeld.
     */
    private void createBalls() {
        // Baelle erstellen
        // Temp Offset
        int offset = 50;
        for (int i = 0; i < BALL_COUNT; i++) {
            Ball b = new Ball(100,
                    400 + offset * i,
                    400 + (offset * i) / 2,
                    500, 10, materials.get(i), ground);
            b.addObserver(this);
            b.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
            balls.add(b);
            b.addObserver(this);
            gameArea.getChildren().add(b.getShape());
        }
        createHoles();
    }
    
    private void createHoles() {
        // Holes erstellen
        // Temp Offset
        int offset = 50;
        for (int i = 0; i < BALL_COUNT; i++) {
            Hole h = new Hole(300 + offset * i,
                    420 + (offset * i) / 2,
                    balls.get(i).getSize());
            holes.add(h);
            h.addObserver(this);
            gameArea.getChildren().add(h.getShape());
        }
    }
    
    private void nextStep() {
        if(play){
            if (lastFrame == 0) {
                lastFrame = System.currentTimeMillis() - FRAME_RATE;
            }

            // vergangene Zeit zum letzten Bildaufruf in Sekunden (SI-Einheit)
            long actualTime = System.currentTimeMillis();
            double deltaTime = (actualTime - lastFrame) / 1000.0;
            lastFrame = actualTime;
            
            for (Ball b : balls) {
                for(GameModel obstacle : obstacles){
                    b.checkCollision(obstacle, deltaTime);
                }
                for(GameModel h : holes){
                    b.checkCollision(h, deltaTime);
                }
                b.move(deltaTime);
            }
        }
        
    }
    
    
    /** Rundet auf eine Nachkommastelle.
     * @param unround Der zu rundende Wert.
     * @return Gerundeter Wert.
     */
    private double round(final double unround) {
        return (double) Math.round(unround * 10) / 10;
    }
}
