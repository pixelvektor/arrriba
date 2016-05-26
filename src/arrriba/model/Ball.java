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
    private double velocityX;
    private double startX;
    private double velocityY;
    private double startY;
    private double friction= -0.1;
    private double number = 0.5;
    private final int size;
    private final Material material;
    
    private Circle shape;
    
    private final ExecutorService service = Executors.newCachedThreadPool();
    
    public Ball() {
        this(1, 20.0, 20.0, 1.0, 1.0, 20.0, 20.0, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocityX, final double velocityY, final double startX, final double startY) {
        this(size, posX, posY, velocityX, velocityY, startX, startY, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocityX, final double velocityY, final double startX, final double startY, final Material material) {
        this.size = size;
        this.setPosX(posX);
        this.setPosY(posY);
        this.setVelocityX(velocityX);
        this.setVelocityY(velocityY);
        this.setStartX(startX);
        this.setStartY(startY);
        this.material = material;
        
        // Erstellt das Shape
        shape = new Circle(getPosX(), getPosY(), getSize());
        shape.setFill(Paint.valueOf("RED"));
    }

    public double getVelocityX() {
        return velocityX;
    }
    
    public double getVelocityY() {
        return velocityY;
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

    public void setVelocityX(final double velocity) {
        this.velocityX = velocity < 0 ? 0 : velocity;
    }
    
    public void setVelocityY(final double velocity) {
        this.velocityY = velocity < 0 ? 0 : velocity;
    }
    
    private void setStartX(double startX) {
        this.startX = startX;
    }
    
    private void setStartY(double startY) {
        this.startY = startY;
    }
    
    // TODO Runnable austauschen?
    public void rollin() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int t = 0; t < 100; t++) {
                    try {
                        Thread.sleep(33);
                    } catch (Exception e) {
                    }
                    double x = number*friction*t*t+t*velocityX+startX;
                    double y = number*friction*t*t+t*velocityY+startY;
                    setPosX(x);
                    setPosY(y);
                    shape.setCenterX(getPosX());
                    callListener();
                }
                service.shutdown();
            }
        });
    }


}
