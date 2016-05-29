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
    private double velocity;
    private double startX;
    private double startY;
    private static final double FRICTION= 0.0;
    private static final double NUMBER = 0.5;
    private double gX=1560-1560;
    private double gY=40-960;
    private double ngX=-gY;
    private double ngY=gX;
    private double grad;
    private double cos;
    private double sin;
    private double vX;
    private double vY;
    private double hit;
    private double lastHit=0;

    private Material material;
    
    private final ExecutorService service = Executors.newCachedThreadPool();
    
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double grad) {
        this(size, posX, posY, velocity, grad, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double grad, final Material material) {
        // Erstellt das Shape
        Circle shape = new Circle(posX, posY, size / 2);
        shape.setFill(Paint.valueOf("RED"));
        this.setShape(shape);
        
        this.setSize(size);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setVelocity(velocity);
        this.setGrad(grad);
        this.setStartX(posX);
        this.setStartY(posY);
        this.material = material;
        
        cos= Math.cos(Math.toRadians(grad));
        sin= Math.sin(Math.toRadians(grad));
        vX=velocity*cos;
        vY=velocity*sin;
        double e= VectorCalculation.times(ngX, ngY, startX-1560, startY-0);
        double d= Math.abs(e)/VectorCalculation.abs(ngX, ngY);               
        hit =d/vX;
    }

    public double getVelocity() {
        return velocity;
    }
    
    public double getStartX(){
        return startX;
    }
    
    public double getGrad() {
        return grad;
    }

    public Material getMaterial() {
        return material;
    }
    
    public void setVelocity(final double velocity) {
        this.velocity = velocity;
    }
    
    public void setGrad(final double grad) {
        this.grad = grad;
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
