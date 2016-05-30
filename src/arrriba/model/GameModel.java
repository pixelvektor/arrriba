/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import java.util.ArrayList;
import java.util.Observable;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
    private double[] cornerPoints;
    
    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getRotation() {
        return rotation;
    }

    /** Gibt die Groesse eines Objektes zurueck.
     * Immer Breite / Hoehe / Durchmesser, nicht Radius.
     * @return Groesse des Objektes.
     */
    public double getSize() {
        return size;
    }

    public Shape getShape() {
        return shape;
    }
    
    public double[] getCornerPoints(){
        
        if (isCircle()) {
            //double[] cornerPoints={getPosX(),getPosY(),getPosX()+r.getWidth(),getPosY(),getPosX()+r.getWidth(),getPosY()+r.getHeight(),getPosX()+r.getHeight()};
        } else {
            Rectangle r = (Rectangle) this.shape;
            double[] cornerPoints={getPosX(),getPosY(),getPosX()+r.getWidth(),getPosY(),getPosX()+r.getWidth(),getPosY()+r.getHeight(),getPosX()-40,getPosY()+r.getHeight(), getPosX()-40, getPosY()};
            return cornerPoints;
        }      
        return cornerPoints;
    }

    public void setPosX(final double posX) {
        this.posX = posX;
        if (isCircle()) {
            Circle c = (Circle) this.shape;
            c.setCenterX(posX);
        } else {
            Rectangle r = (Rectangle) this.shape;
            r.setX(posX);
        }
        //this.setChanged();
        //this.notifyObservers();
    }

    public void setPosY(final double posY) {
        this.posY = posY;
        if (isCircle()) {
            Circle c = (Circle) this.shape;
            c.setCenterY(posY);
        } else {
            Rectangle r = (Rectangle) this.shape;
            r.setY(posY);
        }
        //this.hasChanged();
        //this.notifyObservers();
    }

    public void setRotation(final double rotation) {
        this.rotation = rotation;
        this.shape.setRotate(rotation);
        this.hasChanged();
        this.notifyObservers();
    }

    public void setSize(final double size) {
        if (isCircle()) {
            Circle c = (Circle) this.shape;
            c.setRadius(size / 2);
            this.size = size;
        } else {
            Rectangle r = (Rectangle) this.shape;
            r.setWidth(size);
            r.setHeight(size);
            this.size = size;
        }
        this.setChanged();
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
    
    private boolean isCircle() {
        return this.getShape().toString().contains("Circle");
    }
}
