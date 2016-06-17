/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Puffer extends GameModel {
    /** Konstruktor des Kugelfischs.
     * @param posX Die X-Position des Kugelfischs.
     * @param posY Die Y-Position des Kugelfischs.
     * @param size Die Höhe des Kugelfischs.
     */
    public Puffer (final double posX, final double posY, final double size) {
        // Das Shape des Kugelfischs. 
        Rectangle shape = new Rectangle(posX, posY, size*2, size);
        // Die Textur.
        Image texture = new Image("/arrriba/view/Kugelfisch.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("puffer");
        this.setShape(shape);
        
        this.setPosX(posX/SCALE_FACTOR);
        this.setPosY(posY/SCALE_FACTOR);
        this.setSize(size/SCALE_FACTOR);
    }
    
    /** Gibt den Typ zurueck.
     * @return Gibt "Puffer" zurueck
     */
    @Override
    public String toString() {
        return "Puffer";
    }
}
