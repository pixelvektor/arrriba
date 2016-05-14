/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

/**
 *
 * @author fabian
 */
public class Ball extends Obstacle {
    private double velocity;
    private final int size;
    private final Material material;
    
    public Ball() {
        this.size = 1;
        this.material = new Wood();
    }

    public double getVelocity() {
        return velocity;
    }

    public int getSize() {
        return size;
    }

    public Material getMaterial() {
        return material;
    }

    public void setVelocity(double velocity) {
        if (velocity >= 0) {
            this.velocity = velocity;
        }
    }
    
}
