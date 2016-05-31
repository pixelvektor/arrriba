/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import arrriba.model.material.Wood;
import arrriba.model.material.Material;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author fabian
 */
public class Ball extends GameModel {
    private double velocity;
    private double startX;
    private double startY;
    private static final double FRICTION= 0.0;
    private static final double ONE_HALF = 0.5;
    private double cos;
    private double sin;
    private double vX;
    private double vY;
    private ArrayList<Double> gX=new ArrayList<Double>();
    private ArrayList<Double> gY=new ArrayList<Double>();
    private ArrayList<Double> ngX=new ArrayList<Double>();
    private ArrayList<Double> ngY=new ArrayList<Double>();
    private ArrayList<Double> hit=new ArrayList<Double>();
    private Material material;
    
    private final ExecutorService service = Executors.newCachedThreadPool();

    
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double rotation) {
        this(size, posX, posY, velocity, rotation, new Wood());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double rotation, final Material material) {
        // Erstellt das Shape
        Circle shape = new Circle(posX, posY, size / 2);
        shape.setFill(Paint.valueOf("RED"));
        this.setShape(shape);
        
        this.setSize(size);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setVelocity(velocity);
        this.setRotation(rotation);
        this.setStartX(posX);
        this.setStartY(posY);
        this.material = material;
        cos= Math.cos(Math.toRadians(getRotation()));
        sin= Math.sin(Math.toRadians(getRotation()));
        vX=getVelocity()*cos;
        vY=getVelocity()*sin;
    }

    public double getVelocity() {
        return velocity;
    }
    
    public double getStartX(){
        return startX;
    }
    
    public double getStartY(){
        return startY;
    }
    
    public double getVX(){
    return vX;
    }
    
    public double getVY(){
    return vY;
    }

    public Material getMaterial() {
        return material;
    }
    
    public void setVelocity(final double velocity) {
        this.velocity = velocity;
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
    
    public void setVX(double vX){
    this.vX=vX;
    }
    
    public void setVY(double vY){
    this.vY=vY;
    }
    
    public void checkCollision(final GameModel that, final double time) {
        if (that.isCircle()) {
            double distance = Math.sqrt(
                    Math.pow(that.getPosX() - this.getPosX(), 2)
                            + Math.pow(that.getPosY() - this.getPosY(), 2));
            if (distance <= ((this.getSize() + that.getSize())/2)) {
                // TODO put in here Magic Maths for kullerschubsen
            }
        } else {
            System.out.println("arrriba.model.Ball.checkCollision() JOOOOOOOOOOOOOOOO");
            double[] cornerPoints;
            cornerPoints=that.getCornerPoints();
            //System.out.println(obstacle.getSize());
            for(int c=0;c<=cornerPoints.length-3;c=c+2){
                double a=cornerPoints[c]-cornerPoints[c+2];
                double b=cornerPoints[c+1]-cornerPoints[c+3];
                gX.add(a);
                gY.add(b);                   
                ngX.add(-gY.get(gY.size()-1));
                ngY.add(gX.get(gX.size()-1));
                double e= VectorCalculation.times(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1), getPosX()-cornerPoints[c], getPosY()-cornerPoints[c+1]);
                double d= Math.abs(e)/VectorCalculation.abs(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1));

                RealMatrix coefficients =
                new Array2DRowRealMatrix(new double[][] { { (getVX()+getPosX())-getPosX(),-(cornerPoints[c+2]-cornerPoints[c])}, 
                    { (getVY()+getPosY())-getPosY(),-(cornerPoints[c+3]-cornerPoints[c+1])} },
                   false);
                DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();

                RealVector constants = new ArrayRealVector(new double[] { cornerPoints[c]-getPosX(),cornerPoints[c+1]-getPosY()}, false);
                RealVector solution = solver.solve(constants); 

                double collX=cornerPoints[c]+solution.getEntry(1)*(cornerPoints[c+2]-cornerPoints[c]);
                //double collX=getStartX()+solution.getEntry(0)*((getVX()+200)-getStartX());

                double collY=cornerPoints[c+1]+solution.getEntry(1)*(cornerPoints[c+3]-cornerPoints[c+1]);
                //double collY=getStartY()+solution.getEntry(0)*((getVY()+200)-getStartY());

                if(collX>=cornerPoints[c+2] && collX<=cornerPoints[c] && collY>=cornerPoints[c+3] && collY<=cornerPoints[c+1]){
                    System.out.println(d+"distance");

                    if(d<=getSize()/2){
                        //punch=true;
                        System.out.println(vX+"vor");
                        double gamma = Math.toDegrees(Math.atan(getVY()/getVX()))-(2*Math.toDegrees(Math.atan(ngY.get(ngY.size()-1)/ngX.get(ngX.size()-1))));
                        setVX(VectorCalculation.abs(getVX(),getVY())*Math.cos(gamma));
                        setVY(VectorCalculation.abs(getVX(),getVY())*Math.sin(Math.toRadians(gamma)));
                        System.out.println(vX+"nach");
                    }
                }
            }
        }
    }
    
    /** Bewegt die Kugel.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    public void rollin(final double elapsedTime) {
        double x = ONE_HALF*FRICTION*elapsedTime*elapsedTime+(elapsedTime)*vX+this.getPosX();
        double y = ONE_HALF*FRICTION*elapsedTime*elapsedTime+(elapsedTime)*vY+this.getPosY();
        setPosX(x);
        setPosY(y);
        callListener();
    }
}
