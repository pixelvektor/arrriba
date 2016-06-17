/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Spring extends GameModel {
    
    /** Konstruktor der Feder.
     * @param posX X-Koordinate der linken oberen Ecke der Feder. 
     * @param posY Y-Koordinate der linken oberen Ecke der Feder.
     * @param size Die Größe der Feder.
     */
    public Spring (final double posX, final double posY, final double size) {
        // Das Shape der Feder.
        Rectangle shape = new Rectangle(posX, posY, size, size);
        // Die Textur der Feder.
        Image texture = new Image("/arrriba/view/Sprungfeder.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("spring");
        this.setShape(shape);
        // Skalierungsfaktor 100 Pixel = 0.1 Meter
        this.setPosX(posX/SCALE_FACTOR);
        this.setPosY(posY/SCALE_FACTOR);
        this.setSize(size/SCALE_FACTOR);
    }
    
    /** Gibt den Typ zurueck.
     * @return Gibt "Spring" zurueck
     */
    @Override
    public String toString() {
        return "Spring";
    }
}
