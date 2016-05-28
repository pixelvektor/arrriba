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
public class Barrel extends Obstacle {
    
    public Barrel (final double posX, final double posY, final double size) {
        this.setSize(size);
        this.setPosX(posX);
        this.setPosY(posY);
        
        Circle shape = new Circle(getPosX(), getPosY(), getSize());
        Image texture = new Image("/arrriba/view/Textur.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("obstacle");
        this.setShape(shape);
    }
    
    @Override
    public void setPosX(final double posX) {
        Circle r = (Circle) this.getShape();
        if (r != null) {
            r.setCenterX(posX);
        }
        super.setPosX(posX);
    }
    
    @Override
    public void setPosY(final double posY) {
        Circle r = (Circle) this.getShape();
        if (r != null) {
            r.setCenterY(posY);
        }
        super.setPosY(posY);
    }
    
    @Override
    public void setSize(final double size) {
        Circle c = (Circle) this.getShape();
        if (c != null) {
            c.setRadius(size);
        }
        super.setSize(size);
    }
}
