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
        {0,119},
        {720,119},
        {900,145},
        {1040,190},
        {1140,240},
        {1230,310},
        {1298,400}};
    
    private final double[][] lowerBoundary = {
        {0,880},
        {720,880},
        {900,855},
        {1040,810},
        {1140,760},
        {1230,690},
        {1298,595}};
    
    private final double[] mast = {425, 500, 44};

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

    /** Gibt die Daten des Mastes zurueck.
     * @return Der Mast mit x und y Koordinate und Radius.
     */
    public double[] getMast() {
        return mast;
    }
    
}
