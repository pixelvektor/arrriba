/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model.material;

public class Metal extends Material {
    /** Name des Materials. */
    private static final String NAME = "Metall";
    /** Dichte des Materials. */
    private static final double DENSITY = 7874;
    /** Der Rollreibungskoeffizient des Materials. */
    private static final double FRICTION_COEFFICIENT = 0.016;
     /** Der Texturpfad. */
    private static final String TEXTURE_PATH = "/arrriba/view/metallkugel.png";
    
    /** Gibt den Namen des Materials zurueck.
     * @return Name des Materials (Metall).
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
