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
import arrriba.model.Puffer;
import arrriba.model.Spring;
import arrriba.model.VectorCalculation;
import arrriba.view.NumberTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
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
    // Menuebar
    @FXML
    private MenuBar menuBar;
    
    // Einstellungen
    @FXML
    private Slider sizeSlider;
    
    @FXML
    private NumberTextField sizeNTF;
    
    @FXML
    private Slider rotationSlider;
    
    @FXML
    private NumberTextField rotationNTF;
    
    @FXML
    private Label labelPosX;
    
    @FXML
    private Label labelPosY;
    
    // Spielfeld
    @FXML
    private Pane gameArea;
    
    private Ball subject;
    
    private double gX=1248-1248;
    private double gY=50-946;
    private double ngX=-gY;
    private double ngY=gX;
    private double hit;
    private int zeroCounter=0; 
    
    /** Timer fuer die regelmaessige Berechnung. */
    private Timer timer;
    
    /** Angewaehltes Shape. */
    private Shape activeShape = null;
    
    /** Alle Gegenstaende auf dem Spielfeld. */
    private final ArrayList<GameModel> obstacles = new ArrayList<>();
    
    /** Alle Baelle, die sich im Spiel befinden. */
    private final ArrayList<Ball> balls = new ArrayList<>();
    
    private final ArrayList<Rectangle> wallElements = new ArrayList<>();
    
    private final EventHandler<MouseEvent> shapeOnMousePressedEH;
    
    private final EventHandler<MouseEvent> shapeOnMouseDraggedEH;
    
    private double origSceneX, origSceneY, origTranslateX, origTranslateY;

    public GameControl() {
        this.shapeOnMousePressedEH = (MouseEvent e) -> {
            origSceneX = e.getSceneX();
            origSceneY = e.getSceneY();
            origTranslateX = ((GameModel) ((Shape) e.getSource()).getUserData()).getPosX();
            origTranslateY = ((GameModel) ((Shape) e.getSource()).getUserData()).getPosY();
            
            // Setzen des aktuellen Shapes als ausgewaehlt
            activeShape = (Shape) e.getSource();
            activeShape.toFront();
            
            // Laden der aktuellen Werte des Objektes in die Einstellungen
            sizeSlider.setValue(((GameModel) activeShape.getUserData()).getSize());
            sizeNTF.setValue(((GameModel) activeShape.getUserData()).getSize());
            rotationSlider.setValue(((GameModel) activeShape.getUserData()).getRotation());
            rotationNTF.setValue(((GameModel) activeShape.getUserData()).getRotation());
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
        
        // Temp Offset
        int offset = 50;
        for (int i = 0; i < 1; i++) {
            Ball b = new Ball(100,
                    200 + offset * i,
                    200 + (offset * i) / 2,
                    10, 10);
            b.getShape().addEventHandler(MouseEvent.MOUSE_PRESSED, shapeOnMousePressedEH);
            balls.add(b);
            b.addObserver(this);
            gameArea.getChildren().add(b.getShape());
            
        }
        
        createWall();
        
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                nextStep();
            }
        };
        
        timer = new Timer(true);
        timer.schedule(timerTask, 0, 30);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if(subject==null){
            subject = (Ball) o;
        }
        double t=(Double)arg;
        
        if(zeroCounter<1){
            double e= VectorCalculation.times(ngX, ngY, subject.getStartX()-1248, subject.getStartY()-0);
            double d= Math.abs(e)/VectorCalculation.abs(ngX, ngY);
            hit =d/Math.abs(subject.getVX());
        }
        
        if(zeroCounter>=1){
            if(t>=hit){            
                    subject.setLastHit(t);
                    
                    double gamma = Math.toDegrees(Math.atan(subject.getVY()/subject.getVX()))-(2*Math.toDegrees(Math.atan(ngY/ngX)));
                    subject.setVX(VectorCalculation.abs(subject.getVX(),subject.getVY())*Math.cos(gamma));
                    subject.setVY(VectorCalculation.abs(subject.getVX(),subject.getVY())*Math.sin(Math.toRadians(gamma)));
                    
                    subject.setStartX(subject.getPosX());
                    subject.setStartY(subject.getPosY());

            }
        }
        if(t==0){
            zeroCounter=zeroCounter+1;
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
        activeShape = barrel.getShape();
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
        activeShape = box.getShape();
    }
    
    /** Erstellt einen neue Kugelfisch.
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
        activeShape = puffer.getShape();
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
        activeShape = spring.getShape();
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
        for (Ball b : balls) {
            b.rollin();
        }
    }
    
    @FXML
    public void onGamePause() {
        
    }
    
    @FXML
    public void onGameReset() {
        
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
    
    long startTime = System.currentTimeMillis();
    
    /** Erstellt die Waende des Schiffes fuer die Kollisionskontrolle.
     */
    private void createWall() {
        String styleClass = "wall";
        // Eckdaten aller Waende
        // Format: posX, posY, width, height, rotation
        double[][] config = {{0, 77, 1200, 43, 0},
            {0, 884, 1200, 43, 0},
            {1202, 83, 100, 43, 7.5},
            {1202, 878, 100, 43, -7.5}};
        
        for (int i = 0; i < config.length; i++) {
            Rectangle r = new Rectangle(config[i][0], config[i][1], config[i][2], config[i][3]);
            r.setRotate(config[i][4]);
            r.getStyleClass().add(styleClass);
            gameArea.getChildren().add(r);
            wallElements.add(r);
        }
    }
    
    private void nextStep() {
        long newTime = System.currentTimeMillis();
//        System.out.println("Vergangen: " + (newTime - startTime));
        startTime = newTime;
        
    }
    
    /** Rundet auf eine Nachkommastelle.
     * @param unround Der zu rundende Wert.
     * @return Gerundeter Wert.
     */
    private double round(final double unround) {
        return (double) Math.round(unround * 10) / 10;
    }
}
