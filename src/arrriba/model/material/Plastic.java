/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model.material;

public class Plastic extends Material {
    /** Name des Materials. */
    private static final String NAME = "Kunststoff";
    /** Dichte des Materials. */
    private static final double DENSITY = 2200;
    /** Der Rollreibungskoeffizient des Materials. */
    private static final double FRICTION_COEFFICIENT = 0.008;
    /** Der Texturpfad. */
    private static final String TEXTURE_PATH = "/arrriba/view/plastikkugel.png";
    
    /** Gibt den Namen des Materials zurueck.
     * @return Name des Materials (Kunststoff).
     */
    @Override
    public String toString() {
        return NAME;
    }
    
    /** Getter für die Dichte des Materials.
     * @return Die Dichte des Materials.
     */
    @Override
    public double getDensity() {
        return DENSITY;
    }
    
    /** Getter für den Texturpfad.
     * @return Der Texturpfad.
     */
    @Override
    public String getTexturePath() {
        return TEXTURE_PATH;
    }
    
    /** Getter für den Rollreibungskoeffizienten.
     * @return Der Rollreibungskoeffizient.
     */
    @Override
    public double getFrictionCoefficient() {
        return FRICTION_COEFFICIENT;
    }
}
