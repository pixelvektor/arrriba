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
    
    public Barrel (final double posX, final double posY, final double size) {
        Circle shape = new Circle(posX, posY, size / 2);
        Image texture = new Image("/arrriba/view/Textur.png");
        shape.setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
        shape.getStyleClass().add("obstacle");
        this.setShape(shape);
        
        this.setPosX(posX/1000);
        this.setPosY(posY/1000);
        this.setSize(size/1000);
        this.setvX(0);
        this.setvY(0);
        this.setMass(10000000);
    }
    
    @Override
    public String toString() {
        return "Barrel";
    }
}
