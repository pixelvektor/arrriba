/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import java.util.Observable;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author fabian
 */
public class GameModel extends Observable {
    /** Die Position des Objekts. */
    private double posX;
    private double posY;
    /** Die Rotation des Objekts. */
    private double rotation;
    /** Die Größe des Objekts. */
    private double size;
    /** Der Geschwindigkeitsvektor des Objekts. */
    private double vX;
    private double vY;
    /** Die Masse des Objekts. */
    private double mass;
    /** Das Shape des Objekts. */
    private Shape shape;
    /** Die Eckpunkte des Objekts wenn es ein Rechteck ist. */
    private double[] cornerPoints;
    
    /** Getter der Position des Objekts.
     * @return Die x-Position des Objekts.
     */
    public double getPosX() {
        return posX;
    }

    /** Getter der Position des Objekts.
     * @return Die Y-Position des Objekts.
     */
    public double getPosY() {
        return posY;
    }

    /** Getter für die Rotation des Objekts.
     * @return Die Rotation des Objekts.
     */
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

    /** Getter für den Geschwindigkeitsvektor des Objekts.
     * @return Die X-Koordinate des Geschwindigkeitsvektors.
     */
    protected double getvX() {
        return vX;
    }

    /** Getter für den Geschwindigkeitsvektor des Objekts.
     * @return Die Y-Koordinate des Geschwindigkeitsvektors.
     */
    protected double getvY() {
        return vY;
    }

    /** Getter für die Masse des Objekts. 
     * @return Die Masse des Objekts.
     */
    protected double getMass() {
        return mass;
    }

    /** Getter für das Shape des Objekts.
     * @return Das Shape des Objekts.
     */
    public Shape getShape() {
        return shape;
    }
    
    /** Verschiebt das Rechteck in den Mittelpunkt, rotiert es dort und schiebt es dann in seine Ursprungsposition und gibt die Eckpunkte zurück.
     * @return Die Eckpunkte des Rechtecks
     */
    protected double[] getCornerPoints(){
        // Wenn das Objekt ein Rechteck ist.
        if (!isCircle()) {
            // Das Rechteck.
            Rectangle r = (Rectangle) this.shape;
            // Der Mittelpunkt des Rechtecks.
            double mX=getPosX()+r.getWidth()/1000/2;
            double mY=getPosY()+r.getHeight()/1000/2;
            // Die Eckpunkte des Rechtecks in der Reihenfolge in der die Seiten berechnet werden.
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
            return cornerPoints;
        }      
        return cornerPoints;
    }

    /** Setter für die Positon des Objekts.
     * @param posX Die X-Position des Objekts.
     */
    public void setPosX(final double posX) {
        this.posX = posX;
        // Skalierungsfaktor 100 Pixel = 0.1 Meter
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

    /** Setter für die Positon des Objekts.
     * @param posY Die Y-Position des Objekts.
     */
    public void setPosY(final double posY) {
        this.posY = posY;
        // Skalierungsfaktor 100 Pixel = 0.1 Meter
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

    /** Setter für die Rotation des Objekts.
     * @param rotation Die Rotation des Objekts.
     */
    public void setRotation(final double rotation) {
        this.rotation = rotation;
        this.shape.setRotate(rotation);
        //this.hasChanged();
        //this.notifyObservers();
    }

    /** Setter für die Größe des Objekts.
     * @param size Die Größe des Objekts. 
     */
    public void setSize(final double size) {
        if (isCircle()) {
            // Skalierungsfaktor 100 Pixel = 0.1 Meter
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
        //this.setChanged();
        //this.notifyObservers();
    }

    /** Setter für den Geschwindigkeitsvektor.
     * @param vX X-Koordinate des Geschwindigkeitsvektors
     */
    public void setvX(double vX) {
        this.vX = vX;
    }

    /** Setter für den Geschwindigkeitsvektor.
     * @param vY Y-Koordinate des Geschwindigkeitsvektors
     */
    public void setvY(double vY) {
        this.vY = vY;
    }

    /** Setter für die Masse des Objekts.
     * @param mass Die Masse des Objekts.
     */
    public void setMass(final double mass) {
        this.mass = mass;
    }
    protected void setShape(final Shape shape) {
        this.shape = shape;
        this.shape.setUserData(this);
    }
    
    /** Ob das Onjekt ein Kreis ist.
     * @return true, wenn das Objekt ein Kreis ist.
     */
    protected boolean isCircle() {
        return this.getShape().toString().contains("Circle");
    }
    
    /** Ob das Onjekt ein Kugelfisch ist.
     * @return true, wenn das Objekt ein Kugelfisch ist.
     */
    protected boolean isPuffer() {
        return this.getClass().toString().contains("Puffer");
    }
}
