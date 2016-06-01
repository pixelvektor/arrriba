/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.shape.Circle;

/**
 *
 * @author fabian
 */
public class Hole extends GameModel {
    private double size;
    
    public Hole (final double posX, final double posY, final double size) {
        Circle shape = new Circle(posX, posY, size / 2);
        shape.getStyleClass().add("hole");
        this.setShape(shape);
        
        this.setPosX(posX);
        this.setPosY(posY);
        this.setSize(size);
    }
    
    @Override
    public double getSize() {
        return size;
    }
    
    @Override
    public void setSize(final double size) {
        Circle c = (Circle) this.getShape();
        c.setRadius(size / 2);
        // Wenn das Loch zu klein wird, setze den eigentlichen Radius
        this.size = size > 20 ? size / 4 : size / 2;
        this.setChanged();
        this.notifyObservers();
    }
}
