/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

/**
 *
 * @author fabian
 */
public class Ground {
    
    private double frictionCoefficient;
       
    public double getFrictionCoefficient(){
        return frictionCoefficient;
    }
        
    public void setFrictionCoeffcient(double frictionCoefficient){
            this.frictionCoefficient=frictionCoefficient;
    }
    
}
