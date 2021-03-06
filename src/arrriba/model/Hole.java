/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import javafx.scene.shape.Circle;

public class Hole extends GameModel {
    /** Groesse des Lochs. */
    private double size;
    
    /** Erstellt ein neues Zielloch.
     * @param posX x-Position des Lochs
     * @param posY y-Position des Lochs
     * @param size Groesse des Lochs
     */
    public Hole (final double posX, final double posY, final double size) {
        Circle shape = new Circle(posX, posY, size / 2);
        shape.getStyleClass().add("hole");
        this.setShape(shape);
        
        this.setPosX(posX/SCALE_FACTOR);
        this.setPosY(posY/SCALE_FACTOR);
        this.setSize(size/SCALE_FACTOR);
    }
    
    /** Gibt die Groesse zurueck.
     * @return Groesse des Lochs.
     */
    @Override
    public double getSize() {
        return size;
    }
    
    /** Setzt die neue Groesse.
     * @param size neue Groesse.
     */
    @Override
    public void setSize(final double size) {
        Circle c = (Circle) this.getShape();
        c.setRadius(size * SCALE_FACTOR / 2);
        this.size = size / 2;
    }
    
    /** Gibt den Typ zurueck.
     * @return Gibt "Hole" zurueck
     */
    @Override
    public String toString() {
        return "Hole";
    }
}
