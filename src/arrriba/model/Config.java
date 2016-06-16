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
    /** Die Position der oberen Bande. */
    private final double[][] upperBoundary = {
        {0.0,0.119,180.0},
        {0.720,0.119,188.22},
        {0.900,0.145,197.82},
        {1.040,0.190,206.57},
        {1.140,0.240,217.87},
        {1.230,0.310,232.93},
        {1.298,0.400,0.0}};
    
    /** Die Position der unteren Bande. */
    private final double[][] lowerBoundary = {
        {0,0.880,0.0},
        {0.720,0.880,-7.91},
        {0.900,0.855,-17.81},
        {1.040,0.810,-26.57},
        {1.140,0.760,-37.87},
        {1.230,0.690,-54.51},
        {1.298,0.595,0.0}};

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
