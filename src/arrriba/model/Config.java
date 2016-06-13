/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

/**
 *
 * @author fabian
 */
public class Config {
    private final double[][] upperBoundary = {
        {0,0.119},
        {0.720,0.119},
        {0.900,0.145},
        {1.040,0.190},
        {1.140,0.240},
        {1.230,0.310},
        {1.298,0.400}};
    
    private final double[][] lowerBoundary = {
        {0,0.880},
        {0.720,0.880},
        {0.900,0.855},
        {1.040,0.810},
        {1.140,0.760},
        {1.230,0.690},
        {1.298,0.595}};

    /** Gibt die Abgrenzung der oberen Bordwand zurueck.
     * @return Die Abgrenzung der oberen Bordwand in x und y Koordinaten.
     */
    public double[][] getUpperBoundary() {
        return upperBoundary;
    }

    /** Gibt die Abgrenzung der unteren Bordwand zurueck.
     * @return Die Abgrenzung der unteren Bordwand in x und y Koordinaten.
     */
    public double[][] getLowerBoundary() {
        return lowerBoundary;
    }
}
