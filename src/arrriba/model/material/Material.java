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
public abstract class Material {
    private final String type;
    private String texturePath;
    private double density = 0.8;
    
    public Material() {
        this.type = "Wood";
    }

    /*public String getTexturePath(){
        return texturePath;
    }*/
    
    public String getType() {
        return type;
    }
    
    public abstract double getDensity();
    
    public abstract String getTexturePath();
    
    public abstract double getFrictionCoefficient();

}
