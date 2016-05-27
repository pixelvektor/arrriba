/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import java.util.ArrayList;
import java.util.Observable;
import javafx.scene.shape.Shape;

/**
 *
 * @author fabian
 */
public class GameModel extends Observable {
    private double posX;
    private double posY;
    private double rotation;
    private double size;
    private Shape shape;
    private ArrayList<ModelListener> listener = new ArrayList<ModelListener>();

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getRotation() {
        return rotation;
    }

    public double getSize() {
        return size;
    }

    public Shape getShape() {
        return shape;
    }

    public void setPosX(final double posX) {
        this.posX = posX;
        this.setChanged();
        this.notifyObservers();
    }

    public void setPosY(final double posY) {
        this.posY = posY;
        this.hasChanged();
        this.notifyObservers();
    }

    public void setRotation(final double rotation) {
        this.rotation = rotation;
        this.hasChanged();
        this.notifyObservers();
    }

    public void setSize(final double size) {
        this.size = size;
        this.hasChanged();
        this.notifyObservers();
    }

    protected void setShape(final Shape shape) {
        this.shape = shape;
        this.shape.setUserData(this);
    }
    
    public ArrayList<ModelListener> getListener() {
        return listener;
    }
    
    public void addListener(final ModelListener ol) {
        listener.add(ol);
    }
    
    protected void callListener() {
        for (ModelListener l : getListener()) {
            l.onPositionChange();
        }
    }
}
