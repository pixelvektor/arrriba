/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arrriba.model;

/**
 *
 * @author Adrian
 */
public class VectorCalculation {
    
    public static double times(final double aX, final double aY, final double bX, final double bY) {
       double vector=aX*bX+aY*bY;
        return vector; 
    }
    
    public static double abs(final double aX, final double aY) {
       double vector=Math.sqrt(aX*aX+aY*aY);
        return vector; 
    }
    
}
