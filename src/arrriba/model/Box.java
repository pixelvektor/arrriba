/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author fabian
 */
public class Box extends Obstacle {
    
    public Box (final double posX, final double posY, final double size) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setSize(size);
        
        Rectangle shape = new Rectangle(posX, posY, size, size);
        Image texture = new Image("/arrriba/view/Textur.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("obstacle");
        this.setShape(shape);
    }
    
    @Override
    public void setPosX(final double posX) {
        Rectangle r = (Rectangle) this.getShape();
        if (r != null) {
            r.setX(posX);
        }
        super.setPosX(posX);
    }
    
    @Override
    public void setPosY(final double posY) {
        Rectangle r = (Rectangle) this.getShape();
        if (r != null) {
            r.setY(posY);
        }
        super.setPosY(posY);
    }
    
    @Override
    public void setSize(final double size) {
        Rectangle r = (Rectangle) this.getShape();
        if (r != null) {
            r.setHeight(size);
            r.setWidth(size);
        }
        super.setSize(size);
    }
}
