/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import arrriba.model.material.Material;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class GameModel {
    /** Skalierungsfaktor 100 Pixel = 0.1 Meter. */
    protected final static double SCALE_FACTOR=1000;
    /** Die Position des Objekts (x). */
    private double posX;
    /** Die Position des Objekts (y). */
    private double posY;
    /** Die Rotation des Objekts. */
    private double rotation;
    /** Die Größe des Objekts. */
    private double size;
    /** Der Geschwindigkeitsvektor des Objekts (x). */
    private double vX;
    /** Der Geschwindigkeitsvektor des Objekts (y). */
    private double vY;
    /** Die Reibung (x). */
    private double aX;
    /** Die Reibung (y). */
    private double aY;
    /** Die Masse des Objekts. */
    private double mass;
    /** Das Shape des Objekts. */
    private Shape shape;
    /** Das Material einer Kugel. */
    private Material material;
    /** Die Eckpunkte des Objekts wenn es ein Rechteck ist. */
    private double[] cornerPoints;
    /** Ob die Feder schon getroffen wurde. */
    private Boolean onFirstHit=true;
    /** Ob die Feder aktiv ist. */
    private Boolean isActive=true;
    
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
    
    /** Getter für die Reibung (x).
     * @return Die x-Komponente der Reibung.
     */
    protected double getaX() {
        return aX;
    }

    /** Getter für die Reibung (y).
     * @return Die y-Komponente der Reibung.
     */
    protected double getaY() {
        return aY;
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

    /** Gibt das Material der Kugel zurueck.
     * @return Das Material der Kugel.
     */
    public Material getMaterial() {
        return material;
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
            double mX=getPosX()+r.getWidth()/SCALE_FACTOR/2;
            double mY=getPosY()+r.getHeight()/SCALE_FACTOR/2;
            // Die Eckpunkte des Rechtecks in der Reihenfolge in der die Seiten berechnet werden.
            double[] cornerPoints={mX+((getPosX()-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()+r.getWidth()/SCALE_FACTOR-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()+r.getWidth()/SCALE_FACTOR-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()+r.getWidth()/SCALE_FACTOR-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/SCALE_FACTOR-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()+r.getWidth()/SCALE_FACTOR-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/SCALE_FACTOR-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/SCALE_FACTOR-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()+r.getHeight()/SCALE_FACTOR-mY)*Math.cos(Math.toRadians(-getRotation())))
                ,mX+((getPosX()-mX)*Math.cos(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.sin(Math.toRadians(-getRotation())))
                ,mY+(-(getPosX()-mX)*Math.sin(Math.toRadians(-getRotation()))+(getPosY()-mY)*Math.cos(Math.toRadians(-getRotation())))};
            return cornerPoints;
        }      
        return cornerPoints;
    }
    
    /** Getter ob die Feder schon einmal getroffen wurde.
     * @return Ob die Feder schon einmal getroffen wurde.
     */
    public Boolean getOnFirstHit(){
        return onFirstHit;
    }
     
    /** Getter ob die Feder aktiv ist.
     * @return Ob die Feder aktiv ist.
     */
    public Boolean getActive(){
        return isActive;
    }
    
    /** Setter für den ersten Treffer.
     * @param onFirstHit Ob die Feder schon einmal getroffen wurde.
     */
    public void setOnFirstHit(Boolean onFirstHit){
        this.onFirstHit=onFirstHit;
    }
    
    /** Setter für den Status des Feder.
     * @param isActive Ob die Feder aktiv ist.
     */
    public void setActive(Boolean isActive){
        this.isActive=isActive;
    }

    /** Setter für die Positon des Objekts.
     * @param posX Die X-Position des Objekts.
     */
    public void setPosX(final double posX) {
        this.posX = posX;
        // Skalierungsfaktor 100 Pixel = 0.1 Meter
        if (isCircle()) {
            Circle c = (Circle) this.shape;
            c.setCenterX(posX*SCALE_FACTOR);
        } else {
            Rectangle r = (Rectangle) this.shape;
            r.setX(posX*SCALE_FACTOR);
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
            c.setCenterY(posY*SCALE_FACTOR);
        } else {
            Rectangle r = (Rectangle) this.shape;
            r.setY(posY*SCALE_FACTOR);
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
            c.setRadius(size*SCALE_FACTOR / 2);
            this.size = size;
        }else if(isPuffer()){
            Rectangle r = (Rectangle) this.shape;
            r.setWidth(size*SCALE_FACTOR*2);
            r.setHeight(size*SCALE_FACTOR);
            this.size = size;
        }else {
            Rectangle r = (Rectangle) this.shape;
            r.setWidth(size*SCALE_FACTOR);
            r.setHeight(size*SCALE_FACTOR);
            this.size = size;
        }
        //this.setChanged();
        //this.notifyObservers();
    }

    /** Setter für den Geschwindigkeitsvektor.
     * @param vX X-Koordinate des Geschwindigkeitsvektors
     */
    protected void setvX(double vX) {
        this.vX = vX;
    }

    /** Setter für den Geschwindigkeitsvektor.
     * @param vY Y-Koordinate des Geschwindigkeitsvektors
     */
    protected void setvY(double vY) {
        this.vY = vY;
    }

    /** Setter für die Reibung (x).
     * @param aX x-Komponente der Reibung.
     */
    protected void setaX(double aX) {
        this.aX = aX;
    }

    /** Setter für die Reibung (y).
     * @param aY y-Komponente der Reibung.
     */
    protected void setaY(double aY) {
        this.aY = aY;
    }

    /** Setter für die Masse des Objekts.
     * @param mass Die Masse des Objekts.
     */
    public void setMass(final double mass) {
        this.mass = mass;
    }
    
    /** Setzt das Shape des GameModel und verknuepft das Shape mit selbigem.
     * @param shape Das zu setzende Shape.
     */
    protected void setShape(final Shape shape) {
        this.shape = shape;
        this.shape.setUserData(this);
    }

    /** Setzt das Material der Kugel.
     * @param material Das zu setzende Material.
     */
    public void setMaterial(final Material material) {
        this.material = material;
    }
    
    /** Ob das Objekt ein Kreis ist.
     * @return true, wenn das Objekt ein Kreis ist.
     */
    protected boolean isCircle() {
        return this.getShape().toString().contains("Circle");
    }
    
    /** Ob das Objekt ein Kugelfisch ist.
     * @return true, wenn das Objekt ein Kugelfisch ist.
     */
    protected boolean isPuffer() {
        return this.getClass().toString().contains("Puffer");
    }
}
