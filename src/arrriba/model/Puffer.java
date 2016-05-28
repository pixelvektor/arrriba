/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author fabian
 */
public class Puffer extends Obstacle {
    public Puffer (final double posX, final double posY, final double size) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setSize(size);
        
        Circle shape = new Circle(posX, posY, size);
        Image texture = new Image("/arrriba/view/Textur.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("puffer");
        this.setShape(shape);
    }
    
    @Override
    public void setPosX(final double posX) {
        Circle c = (Circle) this.getShape();
        if (c != null) {
            c.setCenterX(posX);
        }
        super.setPosX(posX);
    }
    
    @Override
    public void setPosY(final double posY) {
        Circle c = (Circle) this.getShape();
        if (c != null) {
            c.setCenterY(posY);
        }
        super.setPosY(posY);
    }
    
    @Override
    public void setSize(final double size) {
        Circle c = (Circle) this.getShape();
        if (c != null) {
            c.setRadius(size / 2);
        }
        super.setSize(size / 2);
    }
}
