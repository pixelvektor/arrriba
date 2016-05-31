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
    private static final double NUMBER = 0.5;
    private double cos;
    private double sin;
    private double vX;
    private double vY;
    private double lastHit=0;
    private ArrayList<Double> gX=new ArrayList<Double>();
    private ArrayList<Double> gY=new ArrayList<Double>();
    private ArrayList<Double> ngX=new ArrayList<Double>();
    private ArrayList<Double> ngY=new ArrayList<Double>();
    private ArrayList<Double> hit=new ArrayList<Double>();
    private int zeroCounter=0; 
    private Boolean punch=true;
    private int lowIndex=0;
    private ArrayList<Integer> hitIndex=new ArrayList<Integer>();
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
    
   public void setLastHit(double lastHit) {
        this.lastHit=lastHit;
    }
    
    public void checkCollision(GameModel obstacle, double time) {
                
        double t=time-lastHit;
        if(punch){                     
                double[] cornerPoints;
                cornerPoints=obstacle.getCornerPoints();
                
                for(int c=0;c<=cornerPoints.length-3;c=c+2){
                    double a=cornerPoints[c]-cornerPoints[c+2];
                    double b=cornerPoints[c+1]-cornerPoints[c+3];
                    gX.add(a);
                    gY.add(b);                   
                    ngX.add(-gY.get(gY.size()-1));
                    ngY.add(gX.get(gX.size()-1));
                    double e= VectorCalculation.times(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1), getStartX()-cornerPoints[c], getStartY()-cornerPoints[c+1]);
                    double d= Math.abs(e)/VectorCalculation.abs(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1));
                    
                    RealMatrix coefficients =
                    new Array2DRowRealMatrix(new double[][] { { (getVX()+200)-getStartX(),-(cornerPoints[c+2]-cornerPoints[c])}, 
                        { (getVY()+200)-getStartY(),-(cornerPoints[c+3]-cornerPoints[c+1])} },
                       false);
                    DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
                    
                    RealVector constants = new ArrayRealVector(new double[] { cornerPoints[c]-getStartX(),cornerPoints[c+1]-getStartY()}, false);
                    RealVector solution = solver.solve(constants); 
                    
                    double collX=cornerPoints[c]+solution.getEntry(1)*(cornerPoints[c+2]-cornerPoints[c]);
                    //double collX=getStartX()+solution.getEntry(0)*((getVX()+200)-getStartX());
                    
                    double collY=cornerPoints[c+1]+solution.getEntry(1)*(cornerPoints[c+3]-cornerPoints[c+1]);
                    //double collY=getStartY()+solution.getEntry(0)*((getVY()+200)-getStartY());
                                      
                    if(collX>=cornerPoints[c+2] && collX<=cornerPoints[c] && collY>=cornerPoints[c+3] && collY<=cornerPoints[c+1]){
                       hit.add(d/Math.abs(getVX())); 
                       hitIndex.add(c);
                       System.out.println(hit.get(hit.size()-1));
                    }       
                    
                }
            if(hit.size()>0){    
                double low=hit.get(0);          
                for(int i=0; i<=hit.size()-1; i++){
                    if(hit.get(i)<=low){
                        low=hit.get(i);
                        if(hitIndex.get(i)==0){
                           lowIndex=0; 
                        }else if(hitIndex.get(i)==2){
                           lowIndex=1; 
                        }else if(hitIndex.get(i)==4){
                           lowIndex=2; 
                        }else if(hitIndex.get(i)==6){
                           lowIndex=3; 
                        }
                        
                        
                    }
                }
                 
                System.out.println(hit.size());
                System.out.println(lowIndex);
            System.out.println(VectorCalculation.abs(gX.get(lowIndex), gY.get(lowIndex))+"bumm");
            }                        
            punch=false;
        }        
        
        if(zeroCounter>=1 && hit.size()>0){
            if(t>=hit.get(0)){
                setLastHit(t);
                //punch=true;
                double gamma = Math.toDegrees(Math.atan(getVY()/getVX()))-(2*Math.toDegrees(Math.atan(ngY.get(lowIndex)/ngX.get(lowIndex))));
                setVX(VectorCalculation.abs(getVX(),getVY())*Math.cos(gamma));
                setVY(VectorCalculation.abs(getVX(),getVY())*Math.sin(Math.toRadians(gamma)));
                
                setStartX(getPosX());
                setStartY(getPosY());
                
            }
        }
        if(t==0){
            zeroCounter=zeroCounter+1;
        }
    }
            
    
    // TODO Runnable austauschen?
    public void rollin(double t) {                            
                    double x = NUMBER*FRICTION*t*t+(t-lastHit)*vX+startX;
                    double y = NUMBER*FRICTION*t*t+(t-lastHit)*vY+startY;
                    setPosX(x);
                    setPosY(y);
                    callListener();
                                
    }
}
