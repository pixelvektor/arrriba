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
    private double vX;
    private double vY;
    private double mass;
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

    protected double getvX() {
        return vX;
    }

    protected double getvY() {
        return vY;
    }

    protected double getMass() {
        return mass;
    }

    public Shape getShape() {
        return shape;
    }
    
    protected double[] getCornerPoints(){
        
        if (isCircle()) {
           
        } else {
            Rectangle r = (Rectangle) this.shape;
            double mX=getPosX()+r.getWidth()/1000/2;
            double mY=getPosY()+r.getHeight()/1000/2;
            
            double[] cornerPoints={mX+((getPosX()-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()+r.getWidth()/1000-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()+r.getWidth()/1000-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()+r.getWidth()/1000-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/1000-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()+r.getWidth()/1000-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/1000-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/1000-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/1000-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.cos(Math.toRadians(-getRotation())))};
            //for(int i=0;i<cornerPoints.length;i++ ){
            //   System.out.println(cornerPoints[i] + " " + i);
            //}
            return cornerPoints;
        }      
        return cornerPoints;
    }

    public void setPosX(final double posX) {
        this.posX = posX;
        if (isCircle()) {
            Circle c = (Circle) this.shape;
            c.setCenterX(posX*1000);
        } else {
            Rectangle r = (Rectangle) this.shape;
            r.setX(posX*1000);
        }
        //this.setChanged();
        //this.notifyObservers();
    }

    public void setPosY(final double posY) {
        this.posY = posY;
        if (isCircle()) {
            Circle c = (Circle) this.shape;
            c.setCenterY(posY*1000);
        } else {
            Rectangle r = (Rectangle) this.shape;
            r.setY(posY*1000);
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
            c.setRadius(size*1000 / 2);
            this.size = size;
        }else if(isPuffer()){
            Rectangle r = (Rectangle) this.shape;
            r.setWidth(size*1000*2);
            r.setHeight(size*1000);
            this.size = size;
        }else {
            Rectangle r = (Rectangle) this.shape;
            r.setWidth(size*1000);
            r.setHeight(size*1000);
            this.size = size;
        }
        this.setChanged();
        this.notifyObservers();
    }

    public void setvX(double vX) {
        this.vX = vX;
    }

    public void setvY(double vY) {
        this.vY = vY;
    }

    public void setMass(final double mass) {
        System.out.println("arrriba.model.GameModel.setMass(): " + mass);
        this.mass = mass;
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
    
    protected boolean isCircle() {
        return this.getShape().toString().contains("Circle");
    }
    
    protected boolean isPuffer() {
        return this.getClass().toString().contains("Puffer");
    }
}
