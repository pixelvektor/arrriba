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
    
    public Box (final double posX, final double posY, final double size) {
        Rectangle shape = new Rectangle(posX, posY, size, size);
        Image texture = new Image("/arrriba/view/Textur.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("obstacle");
        this.setShape(shape);
        
        this.setPosX(posX);
        this.setPosY(posY);
        this.setSize(size);
    }
    
    @Override
    public String toString() {
        return "Box";
    }
}
