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
    private double density = 2200;
    private double frictionCoefficient = 0.008;
    private String texturePath = "/arrriba/view/plastikkugel.png";
    
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
        return frictionCoefficient;
    }
}
