/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 *
 * @author fabian
 */
public class Barrel extends GameModel {
    /** Konstruktor der Tonne.
     * @param posX Die X-Koordinate des Mittelpunkts der Tonne.
     * @param posY Die Y-Koordinate des Mittelpunkts der Tonne.
     * @param size Der Durchmesser der Tonne.
     */
    public Barrel (final double posX, final double posY, final double size) {
        // Das Shape der Tonne.
        Circle shape = new Circle(posX, posY, size / 2);
        // Die Textur der Tonne.
        Image texture = new Image("/arrriba/view/Textur.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("obstacle");
        this.setShape(shape);
        // Skalierungsfaktor 100 Pixel = 0.1 Meter
        this.setPosX(posX/SCALE_FACTOR);
        this.setPosY(posY/SCALE_FACTOR);
        this.setSize(size/SCALE_FACTOR);
        this.setvX(0);
        this.setvY(0);
        this.setMass(10000000);
    }
    
    @Override
    public String toString() {
        return "Barrel";
    }
}
