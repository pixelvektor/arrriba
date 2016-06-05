/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model.material;

/**
 *
 * @author fabian
 */
public class Wood extends Material {
    private static final String NAME = "Holz";
    private double density = 0.8;
    private String texturePath = "/arrriba/view/holzkugel.png";
    
    public Wood() {
        
    }
    
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
}
