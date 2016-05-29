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
    private double gX=1560-1560;
    private double gY=40-960;
    private double ngX=-gY;
    private double ngY=gX;
    private double grad=15;
    private double cos= Math.cos(Math.toRadians(grad));
    private double sin= Math.sin(Math.toRadians(grad));
    private double vX;
    private double vY;
    private double hit;
    private double lastHit=0;

    private Material material;
    
    private final ExecutorService service = Executors.newCachedThreadPool();
    
    public Ball(final int size, final double posX, final double posY,
            final double velocityX, final double velocityY) {
        this(size, posX, posY, velocityX, velocityY, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocityX, final double velocityY, final Material material) {
        // Erstellt das Shape
        Circle shape = new Circle(posX, posY, size / 2);
        shape.setFill(Paint.valueOf("RED"));
        this.setShape(shape);
        
        this.setSize(size);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setVelocityX(velocityX);
        this.setVelocityY(velocityY);
        this.setStartX(posX);
        this.setStartY(posY);
        this.material = material;
        vX=velocityX*cos;
        vY=velocityY*sin;
        double e= VectorCalculation.times(ngX, ngY, startX-1560, startY-0);
        double d= Math.abs(e)/VectorCalculation.abs(ngX, ngY);               
        hit =d/vX;
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

    public void setMaterial(Material material) {
        this.material = material;
    }
    private void checkCollision(double t) {
                if(t>=hit){
                    lastHit=t;
                    double gamma = Math.toDegrees(Math.atan(vY/vX))-(2*Math.toDegrees(Math.atan(ngY/ngX)));
                    vX=VectorCalculation.abs(vX,vY)*Math.cos(gamma);
                    vY=VectorCalculation.abs(vX,vY)*Math.sin(Math.toRadians(gamma));
                    setStartX(getPosX());
                    setStartY(getPosY());
                }
            }
    
    // TODO Runnable austauschen?
    public void rollin() {
        service.submit(new Runnable() {
            @Override
            public void run() {

                for (int t = 0; t < 1000; t++) {

                    try {
                        Thread.sleep(33);
                    } catch (Exception e) {
                    }
                    checkCollision(t-lastHit);
                    double x = NUMBER*FRICTION*t*t+(t-lastHit)*vX+startX;
                    double y = NUMBER*FRICTION*t*t+(t-lastHit)*vY+startY;
                    setPosX(x);
                    setPosY(y);
                    callListener();
                }
                service.shutdown();
            }

            
        });
    }


}
