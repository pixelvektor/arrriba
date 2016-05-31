/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import arrriba.model.material.Wood;
import arrriba.model.material.Material;
import java.util.ArrayList;
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

    private static final double ONE_HALF = 0.5;
    private double cos;
    private double sin;
    private double vX;
    private double vY;

    private double volume;
    private double mass;
    private double gravitation = 9.81;
    private double weightforce;
    private double friction;
    private Ground ground;
 

    private double timeline;
    private boolean finish = false;

    private ArrayList<Double> gX=new ArrayList<Double>();
    private ArrayList<Double> gY=new ArrayList<Double>();
    private ArrayList<Double> ngX=new ArrayList<Double>();
    private ArrayList<Double> ngY=new ArrayList<Double>();
    private ArrayList<Double> hit=new ArrayList<Double>();
    private Material material;
    
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double rotation) {
        this(size, posX, posY, velocity, rotation, new Wood(), new Ground());
    }
    
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double rotation, final Material material, final Ground ground) {
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
        this.ground = ground;
        this.setMaterial(material);
        cos= Math.cos(Math.toRadians(getRotation()));
        sin= Math.sin(Math.toRadians(getRotation()));
        //double acceleration = ONE_HALF*(FRICTION*factor)*elapsedTime*elapsedTime;
        //vX += acceleration - FRICTION*velocity;
        //vY += acceleration - FRICTION*velocity;
        vX=getVelocity()*cos;
        vY=getVelocity()*sin;
        volume = (getSize()*getSize()*Math.PI)/4;

             
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
        double density = material.getDensity();
        mass = density*volume;
        weightforce = mass*gravitation;
        double frictionCoefficient = this.ground.getFrictionCoefficient();
        friction = frictionCoefficient*weightforce;
    }
    
    public void setVX(double vX){
    this.vX=vX;
    }
    
    public void setVY(double vY){
    this.vY=vY;
    }
    
    private void setFinish() {
        finish = true;
    }
    
    public void checkCollision(final GameModel that, final double time) {
        if (that.isCircle()) {
            double distance = Math.sqrt(
                    Math.pow(that.getPosX() - this.getPosX(), 2)
                            + Math.pow(that.getPosY() - this.getPosY(), 2));
            if (distance < ((this.getSize() + that.getSize())/2)) {
                if (that.toString().contains("Hole")) {
                    this.setFinish();
                }
                double[][] intersectPoints = intersectCircles(that);
                if (intersectPoints == null) {
                    System.err.println("Keine Intersection");
                }
//                else {
//                    System.out.println(intersectPoints[0][0] + " " + intersectPoints[0][1] + " " + intersectPoints[1][0] + " " + intersectPoints[1][1]);
//                }
            }
        } else {
            
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

                if(c==6){
                    System.out.println(collX+"colx");
                    System.out.println(collY+"collY");
                    System.out.println(cornerPoints[c+2]+"kleiner als colX");
                    System.out.println(cornerPoints[c]+"größer als colX");
                    System.out.println(cornerPoints[c+3]+"kleiner als colY");
                    System.out.println(cornerPoints[c+1]+"größer als colY");
                    if(getPosX()<=collX){
                        System.out.println(getPosX()+ " " + c+ "posx!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        //System.out.println(getPosY()+ " " + c+ "posy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                     if(getPosY()<=collY){
                        //System.out.println(getPosX()+ " " + c+ "posx!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        System.out.println(getPosY()+ " " + c+ "posy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }
                
                
                if(collX>=cornerPoints[c+2] && collX<=cornerPoints[c] && collY>=cornerPoints[c+3] && collY<=cornerPoints[c+1]){
                    System.out.println(d+"distance");

                    if(d<=getSize()/2){
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        //punch=true;
                        System.out.println(VectorCalculation.abs(vX,vY)+"vor");
                        double alpha= Math.toDegrees(Math.atan(getVY()/getVX()));
                        double beta= Math.toDegrees(Math.atan(ngY.get(ngY.size()-1)/ngX.get(ngX.size()-1)));
                        double gamma = alpha-(2*beta);
                        double delta= 180-2*gamma;
                        System.out.println(gamma);
                        setVX(VectorCalculation.abs(getVX(),getVY())*Math.cos(Math.toRadians(gamma+delta)));
                        setVY(VectorCalculation.abs(getVX(),getVY())*Math.sin(Math.toRadians(gamma+delta)));
                        System.out.println(VectorCalculation.abs(vX,vY)+"nach");
                    }
                }
            }
        }
    }
    
    private double[][] intersectCircles(final GameModel that) {
        double lineX = that.getPosX() - this.getPosX();
        double lineY = that.getPosY() - this.getPosY();
        double distanceCenter = Math.sqrt(lineX * lineX + lineY * lineY);
        if (distanceCenter == 0) {
//            System.err.println("on center");
            return null;
        }
        
        double thisRadius = this.getSize() / 2;
        double thatRadius = that.getSize() / 2;
        double x = (Math.pow(thisRadius, 2) + Math.pow(distanceCenter, 2) - Math.pow(thatRadius, 2)) / (2 * distanceCenter);
        double y = thisRadius * thisRadius - x * x;
        if (y < 0) {
//            System.err.println("no intersection");
            return null;
        }
        
        if (y > 0) {
            y = Math.sqrt(y);
        }
        
        double ex0 = lineX / distanceCenter;
        double ex1 = lineY / distanceCenter;
        double ey0 = -ex1;
        double ey1 = ex0;
        
        double intersect0X = this.getPosX() + x * ex0;
        double intersect0Y = this.getPosY() + x * ex1;
        // Bei einem Shcnittpunkt (evtl rauswerfen, da wir nur zwei haben beim Aufruf)
        if (y == 0) {
            double[][] intersect0 = {{intersect0X, intersect0Y}};
            return intersect0;
        }
        
        // Zwei Schnittpunkte
        double intersect1X = intersect0X - y * ey0;
        double intersect1Y = intersect0Y - y * ey1;
        double[][] intersections = {{intersect0X, intersect0Y}, {intersect1X, intersect1Y}};
        return intersections;
    }
    
    /** Bewegt die Kugel.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */

    public void move(final double elapsedTime) {
        timeline += elapsedTime;
        double x = ONE_HALF*(friction * (vX/vY))*timeline*timeline+elapsedTime*vX+this.getPosX();
        double y = ONE_HALF*friction*timeline*timeline+elapsedTime*vY+this.getPosY();
        setPosX(x);
        setPosY(y);
        callListener();
    }
}
