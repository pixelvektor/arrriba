/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author fabian
 */
public class GameModel extends Observable {
    private double posX;
    private double posY;
    private double rotation;
    private double size;
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

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setSize(double size) {
        this.size = size;
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
