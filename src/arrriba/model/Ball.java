/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.model;

import arrriba.model.material.Wood;
import arrriba.model.material.Material;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
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
    private double equalizer;

    private double volume;
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
        double density = material.getDensity();
        
        this.setShape(shape);
        
        this.setSize(size);
        this.setPosX(posX);
        this.setPosY(posY);
        this.setRotation(rotation);
        this.setVelocity(velocity);
        this.setStartX(posX);
        this.setStartY(posY);
        this.ground = ground;
        this.setMaterial(material);
//        cos= Math.cos(Math.toRadians(getRotation()));
//        sin= Math.sin(Math.toRadians(getRotation()));
        //double acceleration = ONE_HALF*(FRICTION*factor)*elapsedTime*elapsedTime;
        //vX += acceleration - FRICTION*velocity;
        //vY += acceleration - FRICTION*velocity;
//        setvX(getVelocity()*cos);
//        setvY(getVelocity()*sin);
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

    public Material getMaterial() {
        return material;
    }
    
    public boolean isFinished() {
        return finish;
    }
    
    @Override
    public void setSize(final double size) {
        super.setSize(size);
        volume = (getSize()*getSize()*Math.PI)/4;
    }
    
    public void setVelocity(final double velocity) {
        this.velocity = velocity;
        //System.out.println(velocity);
        if(velocity<=0){
          //  System.out.println(velocity + "if");
            setvX(0);
            setvY(0);
        }else{
            //System.out.println(velocity + "else");
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            setvX(velocity*cos);
            setvY(velocity*sin);
        }
        
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
        setMass(density*volume);
        weightforce = getMass()*gravitation;
        double frictionCoefficient = this.ground.getFrictionCoefficient();
        friction = frictionCoefficient*weightforce*0.00005;
        String texturePath = material.getTexturePath();
        Image texture = new Image(texturePath);
        getShape().setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
    }
    
    private void setFinished() {
        finish = true;
        this.setSize(120); // Zur Anschauung
        this.setVelocity(0);
        this.setChanged();
        this.notifyObservers();
    }
    
    public void checkCollision(final GameModel that, final double time) {
        if (that.isCircle() && !isFinished()) {
            collideBalls2(that);
//            double distance = Math.sqrt(
//                    Math.pow(that.getPosX() - this.getPosX(), 2)
//                            + Math.pow(that.getPosY() - this.getPosY(), 2));
//            if (distance < ((this.getSize() + that.getSize())/2)) {
//                if (that.toString().contains("Hole")) {
//                    this.setPosX(that.getPosX());
//                    this.setPosY(that.getPosY());
//                    this.setFinished();
//                }
//                double[][] intersectPoints = intersectCircles(that);
//                if (intersectPoints == null) {
//                    System.err.println("Keine Intersection");
//                }
//                else {
//                    System.out.println(intersectPoints[0][0] + " " + intersectPoints[0][1] + " " + intersectPoints[1][0] + " " + intersectPoints[1][1]);
//                }
//            }
        } else if (!isFinished()) {
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
                new Array2DRowRealMatrix(new double[][] { { (getvX()+getPosX())-getPosX(),-(cornerPoints[c+2]-cornerPoints[c])}, 
                    { (getvY()+getPosY())-getPosY(),-(cornerPoints[c+3]-cornerPoints[c+1])} },
                   false);
                DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();

                RealVector constants = new ArrayRealVector(new double[] { cornerPoints[c]-getPosX(),cornerPoints[c+1]-getPosY()}, false);
                RealVector solution = solver.solve(constants); 

                double collX=cornerPoints[c]+solution.getEntry(1)*(cornerPoints[c+2]-cornerPoints[c]);
                
                //double collX=getStartX()+solution.getEntry(0)*((getVX()+200)-getStartX());

                double collY=cornerPoints[c+1]+solution.getEntry(1)*(cornerPoints[c+3]-cornerPoints[c+1]);
                //double collY=getStartY()+solution.getEntry(0)*((getVY()+200)-getStartY());
                 
                if(cornerPoints[c]<=cornerPoints[c+2] && cornerPoints[c+1]<=cornerPoints[c+3]){
                    if(collX>=c && collX<=cornerPoints[c+2] && collY>cornerPoints[c+1] && collY<=cornerPoints[c+3]){
//                        System.out.println(d+"distance");
                        collide(d);
                    }
                }
                
                if(cornerPoints[c]>=cornerPoints[c+2] && cornerPoints[c+1]<=cornerPoints[c+3]){
                    if(collX<=cornerPoints[c] && collX>=cornerPoints[c+2] && collY>=cornerPoints[c+1] && collY<=cornerPoints[c+3]){
//                        System.out.println(d+"distance");
                        collide(d);                 
                    }
                }
                
                if(cornerPoints[c]>=cornerPoints[c+2] && cornerPoints[c+1]>=cornerPoints[c+3]){
                    if(collX<=cornerPoints[c] && collX>=cornerPoints[c+2] && collY<=cornerPoints[c+1] && collY>=cornerPoints[c+3]){
//                        System.out.println(d+"distance");
                        collide(d);
                    }
                }
                
                if(cornerPoints[c]<=cornerPoints[c+2] && cornerPoints[c+1]>=cornerPoints[c+3]){
//                    System.out.println("AHHH");
                    if(collX<=cornerPoints[c+2] && collX>=cornerPoints[c] && collY>=cornerPoints[c+3] && collY<=cornerPoints[c+1]){
//                        System.out.println(d+"distance");
                    
                        collide(d);
                    }
                }
            }
        }
        
    }

    private void collide(double d) {
        if(d<=getSize()/2){
            double alpha= Math.toDegrees(Math.atan(getvY()/getvX()));
            double beta= Math.toDegrees(Math.atan(ngY.get(ngY.size()-1)/ngX.get(ngX.size()-1)));
            double gamma = alpha-(2*beta);
            double delta= 180-getRotation()-gamma;
            setvX(VectorCalculation.abs(getvX(),getvY())*Math.cos(Math.toRadians(delta)));
            setvY(VectorCalculation.abs(getvX(),getvY())*Math.sin(Math.toRadians(delta)));
            System.out.println("gamma: " + gamma + " delta: " + delta);
        }
    }
    
    /** Schneidet zwei Kreise miteinander und gibt die Schnittpunkte zurueck.
     * @param that Kreis mit dem this geschnitten werden soll.
     * @return Die Schnittpunkte ([Ax, Ay],[Bx, By]).
     */
//    private double[][] intersectCircles(final GameModel that) {
//        double lineX = that.getPosX() - this.getPosX();
//        double lineY = that.getPosY() - this.getPosY();
//        double distanceCenter = Math.sqrt(lineX * lineX + lineY * lineY);
//        if (distanceCenter == 0) {
////            System.err.println("on center");
//            return null;
//        }
//        
//        double thisRadius = this.getSize() / 2;
//        double thatRadius = that.getSize() / 2;
//        double x = (Math.pow(thisRadius, 2) + Math.pow(distanceCenter, 2) - Math.pow(thatRadius, 2)) / (2 * distanceCenter);
//        double y = thisRadius * thisRadius - x * x;
//        if (y < 0) {
////            System.out.println("thisradius: " + thisRadius);
////            System.out.println("x: " + x);
//            System.err.println("no intersection");
//            return null;
//        }
//        
//        if (y > 0) {
//            y = Math.sqrt(y);
//        }
//        
//        double ex0 = lineX / distanceCenter;
//        double ex1 = lineY / distanceCenter;
//        double ey0 = -ex1;
//        double ey1 = ex0;
//        
//        double intersect0X = this.getPosX() + x * ex0;
//        double intersect0Y = this.getPosY() + x * ex1;
//        // Bei einem Shcnittpunkt (evtl rauswerfen, da wir nur zwei haben beim Aufruf)
//        if (y == 0) {
//            double[][] intersect0 = {{intersect0X, intersect0Y}};
//            return intersect0;
//        }
//        
//        // Zwei Schnittpunkte
//        double intersect1X = intersect0X - y * ey0;
//        double intersect1Y = intersect0Y - y * ey1;
//        double[][] intersections = {{intersect0X, intersect0Y}, {intersect1X, intersect1Y}};
//        return intersections;
//    }
    
    /** Bewegt die Kugel.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */

    public void move(final double elapsedTime) {
        timeline += elapsedTime;
        if (!isFinished()) {
//            setVelocity(velocity+ONE_HALF*-friction*timeline*timeline); //<- auskommentieren wenn kollision
//            double x = ONE_HALF*(-friction * equalizer)*timeline*timeline+elapsedTime*vX+this.getPosX();
//            System.out.println("arrriba.model.Ball.move()");
            double x = elapsedTime*getvX()+this.getPosX();
            double y = elapsedTime*getvY()+this.getPosY();
            setPosX(x);
            setPosY(y);
            callListener();
        }
    }
    
    private void collideBalls2(final GameModel that) {
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - that.getPosX(), 2)
                        + Math.pow(this.getPosY() - that.getPosY(), 2));
        if (distance <= this.getSize()/2 + that.getSize()/2) {
            System.out.println("### Kollision");
            System.out.println("thisVX: " + this.getvX());
            System.out.println("thisVY: " + this.getvY());
            System.out.println("thisMass: " + this.getMass());
            System.out.println("thatVX: " + that.getvX());
            System.out.println("thatVY: " + that.getvY());
            System.out.println("thatMass: " + that.getMass());
            System.out.println("thisVelocity: " + this.getVelocity());
            double thisNewVX = (this.getvX() * (this.getMass() - that.getMass()) + (2 * that.getMass() * that.getvX())) / (this.getMass() + that.getMass());
            double thisNewVY = (this.getvY() * (this.getMass() - that.getMass()) + (2 * that.getMass() * that.getvY())) / (this.getMass() + that.getMass());
            double thatNewVX = (that.getvX() * (that.getMass() - this.getMass()) + (2 * this.getMass() * this.getvX())) / (that.getMass() + this.getMass());
            double thatNewVY = (that.getvY() * (that.getMass() - this.getMass()) + (2 * this.getMass() * this.getvY())) / (that.getMass() + this.getMass());
            this.setvX(thisNewVX);
            this.setvY(thisNewVY);
            that.setvX(thatNewVX);
            that.setvY(thatNewVY);
//            this.setPosX(this.getPosX() + thisNewVX);
//            this.setPosY(this.getPosY() + thisNewVY);
//            that.setPosX(that.getPosX() + thatNewVX);
//            that.setPosY(that.getPosY() + thatNewVY);
            System.out.println(thisNewVX + " " + thisNewVY + " " + thatNewVX + " " + thatNewVY);
        }
    }
}
