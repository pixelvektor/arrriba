/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model.material;

public abstract class Material {
    /** Die Dichte des Materials. */
    private double density;
    /** Der Rollreibungskoeffizient des Materials. */
    private double frictionCoefficient;
    /** Der Texturpfad. */
    private String texturePath;

    /** Getter fuer die Dichte des Materials.
     * @return Die Dichte des Materials.
     */
    public abstract double getDensity();
    
    /** Getter fuer den Texturpfad.
     * @return Der Texturpfad.
     */
    public abstract String getTexturePath();
    
    /** Getter fuer den Rollreibungskoeffizienten.
     * @return Der Rollreibungskoeffizient.
     */
    public abstract double getFrictionCoefficient();

}
