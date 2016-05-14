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
public class Obstacle extends Observable {
    private double posX;
    private double posY;
    private double rotation;
    private ArrayList<ObstacleListener> listener = new ArrayList<ObstacleListener>();
    
    public Obstacle() {
        
    }
    
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getRotation() {
        return rotation;
    }

    public ArrayList<ObstacleListener> getListener() {
        return listener;
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
    
    public void addListener(final ObstacleListener ol) {
        listener.add(ol);
    }
    
    protected void callListener() {
        for (ObstacleListener l : getListener()) {
            l.onPositionChange();
        }
    }
    
}
