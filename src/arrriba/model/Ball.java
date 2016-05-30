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
    private double velocityX;
    private double startX;
    private double velocityY;
    private double startY;
    private static final double FRICTION= 0.0;
    private static final double NUMBER = 0.5;
    //private double gX=1560-1560;
    //private double gY=40-960;
    //private double ngX=-gY;
    //private double ngY=gX;
    private double grad=15;
    private double cos= Math.cos(Math.toRadians(grad));
    private double sin= Math.sin(Math.toRadians(grad));
    private double vX;
    private double vY;
    //private double hit;
    private double lastHit=0;
    private ArrayList<Double> gX=new ArrayList<Double>();
    private ArrayList<Double> gY=new ArrayList<Double>();
    private ArrayList<Double> ngX=new ArrayList<Double>();
    private ArrayList<Double> ngY=new ArrayList<Double>();
    private ArrayList<Double> hit=new ArrayList<Double>();
    private int zeroCounter=0; 
    private Boolean punch=true;
    private int lowIndex=0;

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
        //double e= VectorCalculation.times(ngX, ngY, startX-1560, startY-0);
        //double d= Math.abs(e)/VectorCalculation.abs(ngX, ngY);               
        //hit =d/vX;
    }

    public double getVelocityX() {
        return velocityX;
    }
    
    public double getStartX(){
        return startX;
    }
    
    public double getStartY(){
        return startY;
    }
    
    public double getVelocityY() {
        return velocityY;
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
                    double x=-(cornerPoints[c+2]-cornerPoints[c]);
                    if((cornerPoints[c+2]-cornerPoints[c])==0){
                        x=(cornerPoints[c+2]-cornerPoints[c]);
                    }
                    RealMatrix coefficients =
                    new Array2DRowRealMatrix(new double[][] { { getVX()-getStartX(),x}, { getVY()-getStartY(),-(cornerPoints[c+3]-cornerPoints[c+1])} },
                       false);
                    DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
                    RealVector constants = new ArrayRealVector(new double[] { cornerPoints[c]-getStartX(),cornerPoints[c+1]-getStartY()}, false);
                    RealVector solution = solver.solve(constants);                    
                    //double collX=cornerPoints[c]+solution.getEntry(1)*(cornerPoints[c+2]-cornerPoints[c]);
                    double collX=getStartX()+solution.getEntry(0)*(getVX()-getStartX());
                    System.out.println(collX + "collX");
                    System.out.println(cornerPoints[c+2]);
                    System.out.println(cornerPoints[c]);
                    //double collY=cornerPoints[c+1]+solution.getEntry(1)*(cornerPoints[c+3]-cornerPoints[c+1]);
                    double collY=getStartY()+solution.getEntry(0)*(getVY()-getStartY());
                    System.out.println(constants.getEntry(0)+ " " + constants.getEntry(1));
                    System.out.println(solution.getEntry(0)+"w0");
                    System.out.println(solution.getEntry(1));
                    System.out.println(collY + "collY");
                    System.out.println(cornerPoints[c+3]);
                    System.out.println(cornerPoints[c+1]);
                    System.out.println(getVX() + "vx");
                    System.out.println(getVY() + "vy");
                    System.out.println(getStartX() + "sx");
                    System.out.println(getStartY() + "sy");
                    for(int k=0;k<=1;k++){
                        for(int i=0;i<=1;i++){
                            System.out.println(coefficients.getRowVector(k).getEntry(i));
                        }
                    }
                    
                    
                    if(collX>=cornerPoints[c+2] && collX<=cornerPoints[c] && collY>=cornerPoints[c+3] && collY<=cornerPoints[c+1]){
                       hit.add(d/Math.abs(getVX())); 
                       System.out.println(hit.get(hit.size()-1));
                    }       
                    //System.out.println(hit.get(hit.size()-1));
                    
                }
            if(hit.size()>0){    
                double low=hit.get(0);          
                for(int i=0; i<=hit.size()-1; i++){
                    if(hit.get(i)<low){
                        low=hit.get(i);
                        lowIndex=i;
                    }
                }
                lowIndex=hit.size()-1;
            System.out.println(VectorCalculation.abs(gX.get(lowIndex), gY.get(lowIndex))+"bumm");
            }                        
            punch=false;
        }        
        
        if(zeroCounter>=1 && hit.size()>0){
            if(t>=hit.get(lowIndex)){
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
