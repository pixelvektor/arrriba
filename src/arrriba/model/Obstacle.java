/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author fabian
 */
public class Obstacle extends Observable {
    private double posX;
    private double posY;
    private double rotation;
    
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

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    
    
}
