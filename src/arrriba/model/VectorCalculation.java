/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

public class VectorCalculation {
    /** Vektormultiplikation.
     * @param aX X-Koordinate des ersten Vektors.
     * @param aY Y-Koordinate des ersten Vektors.
     * @param bX X-Koordinate des zweiten Vektors.
     * @param bY Y-Koordinate des zweiten Vektors.
     * @return Das Ergebnis
     */
    public static double times(final double aX, final double aY, final double bX, final double bY) {
       double vector=aX*bX+aY*bY;
        return vector; 
    }
    
    /** Betrag eines Vektors.
     * @param aX X-Koordinate.
     * @param aY Y-Koordinate.
     * @return Das Ergebnis.
     */
    public static double abs(final double aX, final double aY) {
       double vector=Math.sqrt(aX*aX+aY*aY);
        return vector; 
    }
}
