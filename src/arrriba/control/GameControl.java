/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import arrriba.model.Ball;
import arrriba.model.Barrel;
import arrriba.model.Box;
import arrriba.model.Config;
import arrriba.model.GameModel;
import arrriba.model.Hole;
import arrriba.model.Level;
import arrriba.model.Puffer;
import arrriba.model.Spring;
import arrriba.model.material.Plastic;
import arrriba.model.material.Material;
import arrriba.model.material.Metal;
import arrriba.model.material.Cork;
import arrriba.model.material.Wood;
import arrriba.view.NumberTextField;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameControl implements Initializable {
    // Konstanten
    /** Anzahl der Baelle im Spiel. */
    private static final int BALL_COUNT = 3;
    
    /** CSS-Klasse fuer das aktive Shape. */
    private static final String ACTIVE = "active";
    
    /** Zeit zwischen Frames in Millisekunden. */
    private static final int FRAME_RATE = 30;
    
    /** Skalierungsfaktor zwischen Welt- und Grafikmassen. */
    private static final double SCALE_FACTOR = 1000;
    
    // FXML Variablen
    /** Menuebar. */
    @FXML
    private MenuBar menuBar;
    
    // Einstellungen
    /** Slider fuer die Groesse eines Objektes. */
    @FXML
    private Slider sizeSlider;
    
    /** NumberTextField fuer die Groesse eines Objektes. */
    @FXML
    private NumberTextField sizeNTF;
    
    /** Slider fuer die Rotation eines Objektes. */
    @FXML
    private Slider rotationSlider;
    
    /** NumberTextField fuer die Rotation eines Objektes. */
    @FXML
    private NumberTextField rotationNTF;
    
    /** NumberTextField fuer die x-Position eines Objektes. */
    @FXML
    private NumberTextField posXNTF;
    
    /** NumberTextField fuer die y-Position eines Objektes. */
    @FXML
    private NumberTextField posYNTF;
    
    /** Auswahlmenue fuer die Materialien der Kugeln. */
    @FXML
    private ChoiceBox<Material> materialMenu;
    
    /** Button zum Loeschen eines Objektes (asugenommen Kugeln). */
    @FXML
    private Button deleteButton;
    
    /** Spielfeld auf dem die Animation laeuft. */
    @FXML
    private Pane gameArea;
  
    /** Materialien. */
    private final ArrayList<Material> materials = new ArrayList<>();
    
    /** Timer fuer die regelmaessige Berechnung. */
    private Timer timer;
    
    /** Letzter Bildaufruf. */
    private long lastFrame = 0;
    
    
    /** Level. */
    Level level = new Level();
    
    /** Konfiguration des Spiels. */
    Config config = new Config();
    
    /** Speichert ob das Spiel laeuft. */
    private boolean play=false;
    
    /** Angewaehltes Shape. */
    private Shape activeShape = null;
    
    /** Das Loch in das die Kugeln fallen. */
    private Hole hole;
    
    /** Alle bewegbaren Gegenstaende auf dem Spielfeld. */
    private final ArrayList<GameModel> obstacles = new ArrayList<>();
    
    /** Alle vorgegebenen Gegenstaende auf dem Spielfeld. */
    private ArrayList<GameModel> levelObstacles = new ArrayList<>();
    
    /** Alle Baelle, die sich im Spiel befinden. */
    private final ArrayList<Ball> balls = new ArrayList<>();
    
    /** EventHandler fuer angeklickte Objekte. */
    private final EventHandler<MouseEvent> shapeOnMousePressedEH;
    
    /** EventHandler fuer Objekte, die verschoben werden. */
    private final EventHandler<MouseEvent> shapeOnMouseDraggedEH;
    
    /** Hilfskoordinaten fuer die Verschiebung der Elemente. */
    private double origSceneX, origSceneY, origTranslateX, origTranslateY;
    
    /** Ctor der Hauptkontrolklasse.
     * Initialisiert die MouseEventHandler.
     */
    public GameControl() {
        this.shapeOnMousePressedEH = (MouseEvent e) -> {
            origSceneX = e.getSceneX();
            origSceneY = e.getSceneY();
            origTranslateX = ((GameModel) ((Shape) e.getSource()).getUserData()).getPosX()*SCALE_FACTOR;
            origTranslateY = ((GameModel) ((Shape) e.getSource()).getUserData()).getPosY()*SCALE_FACTOR;
            
            // Setzen des aktuellen Shapes als ausgewaehlt
            if (activeShape != null) {
                activeShape.getStyleClass().remove(ACTIVE);
            }
            activeShape = (Shape) e.getSource();
            activeShape.getStyleClass().add(ACTIVE);
            activeShape.toFront();
            
            // Laden der aktuellen Werte des Objektes in die Einstellungen
            sizeSlider.setValue(((GameModel) activeShape.getUserData()).getSize()*SCALE_FACTOR);
            sizeNTF.setValue(((GameModel) activeShape.getUserData()).getSize()*SCALE_FACTOR);
            rotationSlider.setValue(((GameModel) activeShape.getUserData()).getRotation());
            rotationNTF.setValue(((GameModel) activeShape.getUserData()).getRotation());
            posXNTF.setValue(((GameModel) activeShape.getUserData()).getPosX()*SCALE_FACTOR);
            posYNTF.setValue(((GameModel) activeShape.getUserData()).getPosY()*SCALE_FACTOR);
            
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
            // Drag relativ zu dem Ort wo das Objekt angefasst wurde.
            double newTranslateX = origTranslateX + e.getSceneX() - origSceneX;
            double newTranslateY = origTranslateY + e.getSceneY() - origSceneY;
            
            ((GameModel) ((Shape) (e.getSource())).getUserData()).setPosX(newTranslateX/SCALE_FACTOR);
            ((GameModel) ((Shape) (e.getSource())).getUserData()).setPosY(newTranslateY/SCALE_FACTOR);
        };
    }
    
    /** Initialisiert das Spiel.
     * @param location n/a
     * @param resources n/a
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Materialien erstellen
        materials.add(new Wood());
        materials.add(new Metal());
        materials.add(new Plastic());
        materials.add(new Cork());
        
        // Materialien dem Materialmenue hinzufuegen
        materialMenu.setItems(FXCollections.observableArrayList(materials));
        materialMenu.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ((Ball) activeShape.getUserData()).setMaterial(materials.get(newValue.intValue()));
            }
        });
        
        // Starten des Levels
        levelStart();

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

    /** Startet und laedt das erste Level.
     */
    private void levelStart() {
        level.loadOberdeck();
        loadLevel();
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
        obstacles.add(spring);
        gameArea.getChildren().add(spring.getShape());
    }
    
    /** Startet und laedt das erste Level.
     */
    @FXML
    public void onOberdeckMI() {
        level.loadOberdeck();
        loadLevel();
    }
    
    /** Startet und laedt das zweite Level.
     */
    @FXML
    public void onZwischendeckMI() {
        level.loadZwischendeck();
        loadLevel();
    }
    
    /** Startet und laedt das dritte Level.
     */
    @FXML
    public void onUnterdeckMI() {
        level.loadUnterdeck();
        loadLevel();
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
    
    /** Startet das Spiel und fixiert die Obstacles auf dem Spielfeld.
     */
    @FXML
    public void onGamePlay() {
        // Alle Baelle nach vorne bringen, damit diese ueber den Loechern sind
        for (Ball b : balls) {
            b.getShape().toFront();
        }
        
        // Obstacles fixieren
        for (GameModel o : obstacles) {
            o.getShape().removeEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
            o.getShape().removeEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        }
        
        // activeShape deaktivieren
        if (activeShape != null) {
            activeShape.getStyleClass().remove("active");
        }
        activeShape = null;
        
        // Aktivieren des Spiels
        play = true;
    }
    
    /** Stoppt das Spiel, setzt die Kugeln auf die Startpositionen und reaktiviert die Obstacles.
     */
    @FXML
    public void onGameRetry() {
        play = false;
        
        // Reset der Kugeln und der Zeit
        for (Ball b : balls) {
            gameArea.getChildren().remove(b.getShape());
        }
        balls.removeAll(balls);
        lastFrame = 0;
        
        // Obstacles veraenderbar setzen
        for (GameModel o : obstacles) {
            o.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
            o.getShape().addEventHandler(MouseEvent.MOUSE_DRAGGED, shapeOnMouseDraggedEH);
        }
        
        createBalls(level.getStartPos());
    }
    
    /** Laedt das aktuelle Level neu.
     */
    @FXML
    public void onGameReset() {
        loadLevel();
    }
    
    /** Reagiert auf den SizeSlider.
     * Setzt die Einstellung des Sliders in das sizeNTF und in das dazugehoerige Objekt.
     */
    @FXML
    public void onSizeSlider() {
        final double size = round(sizeSlider.getValue());
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setSize(size/SCALE_FACTOR);
        }
        sizeNTF.setValue(size);
    }

    /** Setzt den SizeSlider und das dazugehoerige Objekt auf den eingegebenen Wert.
     */
    @FXML
    public void onSizeNTF() {
        final double origSize = sizeNTF.getValue();
        double size;
        
        // Eingabe soll zwischen 20 und 100 liegen
        if (origSize > 100) {
            size = 100;
        } else if (origSize < 20) {
            size = 20;
        } else {
            size = origSize;
        }
        
        // Setzen des angepasstene Wertes in das NTF
        sizeNTF.setText(Double.toString(size));
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setSize(size/SCALE_FACTOR);
        }
        
        sizeSlider.setValue(size);
    }
    
    /** Reagiert auf den RotationSlider.
     * Setzt die Einstellung des Sliders in das rotationNTF und in das dazugehoerige Objekt.
     */
    @FXML
    public void onRotationSlider() {
        final double rotation = round(rotationSlider.getValue());
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setRotation(rotation);
        }
        rotationNTF.setValue(rotation);
    }
    
    /** Setzt den RotationSlider und das dazugehoerige Objekt auf den eingegebenen Wert.
     */
    @FXML
    public void onRotationNTF() {
        double rotation = rotationNTF.getValue() % 360;
        rotationNTF.setText(Double.toString(rotation));
        if (activeShape != null) {
            ((GameModel) activeShape.getUserData()).setRotation(rotation);
        }
        rotationSlider.setValue(rotation);
    }
    
    /** Setzt das dazugehoerige Objekt auf die x-Position.
     */
    @FXML
    public void onPosXNTF() {
        double posX = posXNTF.getValue();
        ((GameModel) activeShape.getUserData()).setPosX(posX/SCALE_FACTOR);
    }
    
    /** Setzt das dazugehoerige Objekt auf die y-Position.
     */
    @FXML
    public void onPosYNTF() {
        double posY = posYNTF.getValue();
        ((GameModel) activeShape.getUserData()).setPosY(posY/SCALE_FACTOR);
    }
    
    /** Loescht das dazugehoerige Objekt aus allen Listen und vom Spielfeld.
     */
    @FXML
    public void onDeleteButton() {
        gameArea.getChildren().remove(activeShape);
        obstacles.remove((GameModel) activeShape.getUserData());
        activeShape = null;
        deleteButton.disableProperty().set(true);
    }
    
    /** Erstellt die Baelle auf dem Spielfeld.
     * @param startPos ArrayList mit x- und y-Positionen der Kugeln.
     */
    private void createBalls(final ArrayList<Double> startPos) {
        for (int i = 0; i < BALL_COUNT; i++) {
            Ball b = new Ball(100,
                    startPos.get(i*2),
                    startPos.get((i*2)+1),
                    500, 15, materials.get(0));
            b.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
            balls.add(b);
            gameArea.getChildren().add(b.getShape());
        }
    }
    
    /** Erstellt das Zielloch.
     * @param x x-Position des Lochs.
     * @param y y-Position des Lochs.
     */
    private void createHole(final double x, final double y) {
        hole = new Hole(x, y, 100);
        hole.getShape().setEffect(new InnerShadow(50, Color.BLACK));
        gameArea.getChildren().add(hole.getShape());
    }
    
    /** Berechnet den naechsten Spielschritt.
     * Die Berechnung findet aufgrund der verstrichenen Zeit seit der letzten Berechnung statt.
     */
    private void nextStep() {
        // Wenn das Spiel freigegeben ist
        if(play){
            // Wenn die erste Berechnung durchlaeuft wird die Zeit initialisiert.
            if (lastFrame == 0) {
                lastFrame = System.currentTimeMillis() - FRAME_RATE;
            }
            
            // Zusammenstellen aller Kollisionsobjekte
            ArrayList<GameModel> objects = new ArrayList<>();
            objects.addAll(obstacles);
            objects.addAll(levelObstacles);
            objects.addAll(balls);
            objects.add(hole);
            
            // vergangene Zeit zum letzten Bildaufruf in Sekunden (SI-Einheit)
            long actualTime = System.currentTimeMillis();
            double deltaTime = (actualTime - lastFrame) / 1000.0;
            lastFrame = actualTime;
            
            // Kollision der Baelle mit den Kollisionsobjekten und der Aussenbordwand
            for (Ball b : balls) {
                // Entfernen des Balls selbst aus der Pruefliste
                objects.remove(b);
                
                if (!b.isFinished()) {
                    b.setVelocityVector(deltaTime);
                    b.checkCollisionBoundary(config, deltaTime);
                    for(GameModel object : objects){
                        b.checkCollision(object, deltaTime);
                    }
                }
                
                // Weiterbewegen des Balls
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
    
    /** Laedt die Daten aus dem aktuellen Level.
     */
    private void loadLevel() {
        gameReset();
        
        // Laden der Gegenstaende im Level
        levelObstacles = level.getObstacles();
        for( GameModel obstacle : levelObstacles){
            gameArea.getChildren().add(obstacle.getShape());
        }
        
        createBalls(level.getStartPos());
        createHole(level.getHoleX(),level.getHoleY());
    }

    /** Setzt das Spiel auf einen leeren Zustand zurueck.
     */
    private void gameReset() {
        play = false;
        gameArea.getChildren().remove(0, gameArea.getChildren().size());
        balls.removeAll(balls);
        obstacles.removeAll(obstacles);
        lastFrame = 0;
    }
}
