/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import arrriba.model.material.Wood;
import arrriba.model.material.Material;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author fabian
 */
public class Ball extends Obstacle {
    private double velocity;
    private final int size;
    private final Material material;
    
    private Circle shape;
    
    private final ExecutorService service = Executors.newCachedThreadPool();
    
    public Ball() {
        this(1, 20.0, 20.0, 1.0, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocity) {
        this(size, posX, posY, velocity, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final Material material) {
        this.size = size;
        this.setPosX(posX);
        this.setPosY(posY);
        this.setVelocity(velocity);
        this.material = material;
        
        // Erstellt das Shape
        shape = new Circle(getPosX(), getPosY(), getSize());
        shape.setFill(Paint.valueOf("RED"));
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

    public Circle getShape() {
        return shape;
    }

    public void setVelocity(final double velocity) {
        this.velocity = velocity < 0 ? 0 : velocity;
    }
    
    // TODO Runnable austauschen?
    public void rollin() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(33);
                    } catch (Exception e) {
                    }
                    setPosX(getPosX()+getVelocity());
                    shape.setCenterX(getPosX());
                    //setVelocity(getVelocity()+0.1);
                    callListener();
                }
                service.shutdown();
            }
        });
    }
}
