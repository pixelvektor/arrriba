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
public class Ball extends GameModel {
    private double velocityX;
    private double startX;
    private double velocityY;
    private double startY;
    private static final double FRICTION= 0.0;
    private static final double NUMBER = 0.5;

    private Material material;
    
    private final ExecutorService service = Executors.newCachedThreadPool();
    
    public Ball() {
        this(1, 20.0, 20.0, 1.0, 1.0, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocityX, final double velocityY) {
        this(size, posX, posY, velocityX, velocityY, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocityX, final double velocityY, final Material material) {
        this.setSize(size);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setVelocityX(velocityX);
        this.setVelocityY(velocityY);
        this.setStartX(posX);
        this.setStartY(posY);
        this.material = material;
        
        // Erstellt das Shape
        Circle shape = new Circle(getPosX(), getPosY(), getSize());
        shape.setFill(Paint.valueOf("RED"));
        this.setShape(shape);
    }

    public double getVelocityX() {
        return velocityX;
    }
    
    public double getStartX(){
        return startX;
    }
    
    public double getVelocityY() {
        return velocityY;
    }

    public Material getMaterial() {
        return material;
    }
    
    public void setVelocityX(final double velocity) {
        this.velocityX = velocity;
    }
    
    public void setVelocityY(final double velocity) {
        this.velocityY = velocity;
    }
    
    public void setStartX(final double startX) {
        this.startX = startX;
    }
    
    public void setStartY(final double startY) {
        this.startY = startY;
    }
    
    @Override
    public void setSize(final double size) {
        Circle c = (Circle) this.getShape();
        if (c != null) {
            c.setRadius(size / 2);
        }
        super.setSize(size / 2);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    // TODO Runnable austauschen?
    public void rollin() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int t = 0; t < 400; t++) {
                    try {
                        Thread.sleep(33);
                    } catch (Exception e) {
                    }
                    double x = NUMBER*FRICTION*t*t+t*velocityX+startX;
                    double y = NUMBER*FRICTION*t*t+t*velocityY+startY;
                    setPosX(x);
                    setPosY(y);
                    callListener();
                }
                service.shutdown();
            }
        });
    }


}
