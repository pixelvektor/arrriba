/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author fabian
 */
public class Box extends GameModel {
    /** Der Konstruktor der Kiste.
     * @param posX Die X-Koordinate der oberen linken Ecke der Kiste.
     * @param posY Die Y-Koordinate der oberen linken Ecke der Kiste.
     * @param size Die Größe der Kiste.
     */
    public Box (final double posX, final double posY, final double size) {
        // Das Shape der Kiste.
        Rectangle shape = new Rectangle(posX, posY, size, size);
        // Die Textur der Kiste.
        Image texture = new Image("/arrriba/view/Textur.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("obstacle");
        this.setShape(shape);
        // Skalierungsfaktor 100 Pixel = 0.1 Meter
        this.setPosX(posX/SCALE_FACTOR);
        this.setPosY(posY/SCALE_FACTOR);
        this.setSize(size/SCALE_FACTOR);
    }
    
    @Override
    public String toString() {
        return "Box";
    }
}
