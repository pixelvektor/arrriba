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
public class Metal extends Material {
    private static final String NAME = "Metall";
    private double density = 7.8;
    private double frictionCoefficient = 0.9; // wert bezieht sich auf Stein, evtl ändern
    private String texturePath = "/arrriba/view/metallkugel.png";
    

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
