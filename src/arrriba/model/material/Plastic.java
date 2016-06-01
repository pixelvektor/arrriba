/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model.material;

/**
 *
 * @author fabian
 */
public class Plastic extends Material {
    private static final String NAME = "Kunststoff";
    private double density = 1.14;
    
    @Override
    public String toString() {
        return NAME;
    }
    
     @Override
    public double getDensity() {
        return density;
    }
}
