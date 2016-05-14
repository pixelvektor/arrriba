/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author fabian
 */
public class Ball extends Obstacle {
    private double velocity;
    private final int size;
    private final Material material;
    private ExecutorService service = Executors.newCachedThreadPool();
    
    public Ball() {
        this.size = 1;
        velocity = 2.0;
        this.setPosX(20.0);
        this.setPosY(42.0);
        this.setRotation(349.0);
        this.material = new Wood();
    }

    public double getVelocity() {
        return velocity;
    }

    public int getSize() {
        return size;
    }

    public Material getMaterial() {
        return material;
    }

    public void setVelocity(double velocity) {
        if (velocity >= 0) {
            this.velocity = velocity;
        }
    }
    
    public void rollin() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(33);
                    } catch (Exception e) {
                    }
                    setPosX(getPosX()+1);
                    callListener();
                }
            }
        });
    }
}
