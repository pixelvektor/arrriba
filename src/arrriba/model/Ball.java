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
    private double velocityX;
    private double startX;
    private double velocityY;
    private double startY;
    private static final double FRICTION= -0.1;
    private static final double NUMBER = 0.5;

    private Material material;
    
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
        this.setSize(size);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setVelocityX(velocityX);
        this.setVelocityY(velocityY);
        this.setStartX(startX);
        this.setStartY(startY);
        this.material = material;
        
        // Erstellt das Shape
        Circle shape = new Circle(getPosX(), getPosY(), getSize());
        shape.setFill(Paint.valueOf("RED"));
        this.setShape(shape);
    }

    public double getVelocityX() {
        return velocityX;
    }
    
    public double getVelocityY() {
        return velocityY;
    }

    public Material getMaterial() {
        return material;
    }
    
    public void setVelocityX(final double velocity) {
        this.velocityX = velocity < 0 ? 0 : velocity;
    }
    
    public void setVelocityY(final double velocity) {
        this.velocityY = velocity < 0 ? 0 : velocity;
    }
    
    private void setStartX(final double startX) {
        this.startX = startX;
    }
    
    private void setStartY(final double startY) {
        this.startY = startY;
    }
    
    @Override
    public void setSize(final double size) {
        Circle c = (Circle) this.getShape();
        if (c != null) {
            c.setRadius(size);
        }
        super.setSize(size);
    }

    public void setMaterial(Material material) {
        this.material = material;
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
                    double x = NUMBER*FRICTION*t*t+t*velocityX+startX;
                    double y = NUMBER*FRICTION*t*t+t*velocityY+startY;
                    setPosX(x);
                    setPosY(y);
                    Circle c = (Circle) getShape();
                    c.setCenterX(getPosX());
                    c.setCenterY(getPosY());
                    callListener();
                }
                service.shutdown();
            }
        });
    }


}
