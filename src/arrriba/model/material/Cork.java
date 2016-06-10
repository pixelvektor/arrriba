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
public class Cork extends Material {
    private static final String NAME = "Kork";
    private double density = 0.5;
    private double frictionCoefficient = 0.4;
    private String texturePath = "/arrriba/view/korkkugel.png";

    @Override
    public String toString() {
        return NAME;
    }
    
     @Override
    public double getDensity() {
        return density;
    }
    
    @Override
    public String getTexturePath() {
        return texturePath;
    }
    
    @Override
    public double getFrictionCoefficient() {
        return density;
    }
}
