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

    private double volume;
    private Ground ground;
    private double timeline;
    private boolean finish = false;
    private double aX=0;
    private double aY=0;
    
    private double springHitTime=0;
    private double springVel=0;
    private double s=0;

    private ArrayList<Double> gX=new ArrayList<Double>();
    private ArrayList<Double> gY=new ArrayList<Double>();
    private ArrayList<Double> ngX=new ArrayList<Double>();
    private ArrayList<Double> ngY=new ArrayList<Double>();
    private ArrayList<Double> hit=new ArrayList<Double>();
    
    /** Speichert aktuell kollidierte Objekte, solange diese in der Naehe sind. */
    private ArrayList<GameModel> collided = new ArrayList<GameModel>();
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
        double sizeD=size;
        this.setShape(shape);
        this.setSize(sizeD/1000);
        this.setPosX(posX/1000);
        this.setPosY(posY/1000);
        this.setRotation(rotation);
        this.setVelocity(velocity/1000);
        this.setStartX(posX/1000);
        this.setStartY(posY/1000);
        this.ground = ground;
        this.setMaterial(material);
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
    
    /** Gibt zurueck ob die Kugel im Ziel ist oder nicht.
     * @return True wenn der Ball im Ziel ist, sonst false.
     */
    public boolean isFinished() {
        return finish;
    }
    
    @Override
    public String toString() {
        return "Ball";
    }
    
    @Override
    public void setSize(final double size) {
        super.setSize(size);
        volume = (getSize()*getSize()*Math.PI)/4;
    }
    
    public void setVelocity(double velocity){
        this.velocity=velocity;
    }
    
    public void setVelocityVector(final double elapsedTime) {
        timeline += elapsedTime;
        if (timeline==0+elapsedTime){           
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            setvX(getVelocity()*cos);
            setvY(getVelocity()*sin);
            aX = (-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
            aY = (-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
        }else if(VectorCalculation.abs(getvX(), getvY())<=0){
            setvX(0);
            setvY(0);
        }else{
            setvX(getvX()+aX*elapsedTime);
            setvY(getvY()+aY*elapsedTime);
        }  
    }
    
    public void setStartX(final double startX) {
        this.startX = startX;
    }
    
    public void setStartY(final double startY) {
        this.startY = startY;
    }

    public void setMaterial(Material material) {
        this.material=material;
        double frictionCoefficient = this.ground.getFrictionCoefficient();
        double density = material.getDensity();
        setMass(density*volume);
         
        
        /* Brauchen wir (ertmal) doch nicht
        this.material = material;
        double density = material.getDensity();
        setMass(density*volume);
        weightForce = getMass()*gravitation; 
        double frictionForce = frictionCoefficient*weightForce;
        friction = frictionForce/getMass();*/
         
        String texturePath = material.getTexturePath();
        Image texture = new Image(texturePath);
        getShape().setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
    }
    
    /** Setzt diesen Ball als beendet und legt ihn still.
     */
    private void setFinished() {
        finish = true;
        this.setSize(120); // Zur Anschauung
        this.setVelocityVector(0);
        this.setChanged();
        this.notifyObservers();
    }
    
    /** Entfernt den uebergebenen Ball aus der Kollisionsliste.
     * @param that Der zu entfernende Ball.
     */
    protected void removeCollided(final GameModel that) {
        collided.remove(that);
    }
    
    public void checkCollision(final GameModel that, final double time) {
        if (!isFinished()) {
            setVelocityVector(time);
            // Je nach Objekt welches ueber that uebergeben wird
            String name = that.toString();
            if(VectorCalculation.abs(getvX(), getvY())*time<=s && springHitTime > 0 && (s/VectorCalculation.abs(getvX(), getvY()))-(timeline-springHitTime)>0){
                collideSpring(that);
            }
            switch (name) {
                case "Hole": 
                    this.setPosX(that.getPosX());
                    this.setPosX(that.getPosY());
                    this.setFinished();
                    break;
                case "Barrel": collideBarrel(that);
                break;
                case "Ball": collideBall(that);
                break;                               
                default: checkCollideBoxShapes(that, time);
                
            }
//            if (that.isCircle()) {
//                collideBarrel(that);
//                double distance = Math.sqrt(
//                        Math.pow(that.getPosX() - this.getPosX(), 2)
//                                + Math.pow(that.getPosY() - this.getPosY(), 2));
//                if (distance < ((this.getSize() + that.getSize())/2)) {
//                    if (that.toString().contains("Hole")) {
//                        this.setPosX(that.getPosX());
//                        this.setPosY(that.getPosY());
//                        this.setFinished();
//                    }
//                    double[][] intersectPoints = intersectCircles(that);
//                    if (intersectPoints == null) {
//                        System.err.println("Keine Intersection");
//                    }
//                    else {
//                        System.out.println(intersectPoints[0][0] + " " + intersectPoints[0][1] + " " + intersectPoints[1][0] + " " + intersectPoints[1][1]);
//                    }
//                }
//            }
        }                
    }

    private void collideBoxShapes(GameModel that,String name) {
            double alpha= Math.toDegrees(Math.atan(getvY()/getvX()));
            double beta= Math.toDegrees(Math.atan(ngY.get(ngY.size()-1)/ngX.get(ngX.size()-1)));
            double gamma = alpha-(2*beta);
            double delta= 180-getRotation()-gamma;
            setRotation(delta);
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            if(name.equals("Spring")){
                springHitTime=timeline;
                s = that.getSize()/2;
                double D = 20;
                springVel=Math.sqrt((D*s*s/getMass()));
                collideSpring(that);
            }else{
                setvX(VectorCalculation.abs(getvX(),getvY())*cos);
                setvY(VectorCalculation.abs(getvX(),getvY())*sin);
                aX = (-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
                aY = (-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
            }
            System.out.println("gamma: " + gamma + " delta: " + delta+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");                
    }

    private void collidePuffer(GameModel that) {
        double[] cornerPoints=that.getCornerPoints();
        ArrayList<Double> distance=new ArrayList();
        for(int c=0;c<=cornerPoints.length-3;c=c+2){
            double a=cornerPoints[c]-cornerPoints[c+2];
            double b=cornerPoints[c+1]-cornerPoints[c+3];
            double nX=-b;
            double nY=a;
            double e= VectorCalculation.times(nX, nY, getPosX()-cornerPoints[c], getPosY()-cornerPoints[c+1]);
            distance.add(Math.round(Math.abs(e)/VectorCalculation.abs(nX, nY)*1000)/1000.0);
        }
        if(distance.get(0)+distance.get(2)==that.getSize() && distance.get(1)+distance.get(3)==(that.getSize()*2)){
            double cosPuf= Math.cos(Math.toRadians(that.getRotation()));
            double sinPuf= Math.sin(Math.toRadians(that.getRotation()));
            double a=0.1;
            double vel=0.5*a*timeline*timeline;
            setvX(getvX()+vel*cosPuf);
            setvY(getvY()+vel*sinPuf);
            aX = (-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
            aY = (-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
        }
    }
    
    
    /** Bewegt die Kugel pro Zeitabschnitt weiter.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    public void move(final double elapsedTime) {
        
        if (!isFinished()) {
            setVelocityVector(elapsedTime);
//            double x = ONE_HALF*(-friction * equalizer)*timeline*timeline+elapsedTime*vX+this.getPosX();
//            System.out.println("arrriba.model.Ball.move()");
//            double x = elapsedTime*getvX()+this.getPosX();
            
            double x = ONE_HALF*aX*elapsedTime*elapsedTime+elapsedTime*getvX()+this.getPosX();
            double y = ONE_HALF*aY*elapsedTime*elapsedTime+elapsedTime*getvY()+this.getPosY();

//           System.out.println("X:"+getvX()+"         y;"+getvY());
//           System.out.println("HIER:     "+((VectorCalculation.abs(getvX(), getvY()))-(VectorCalculation.abs(aX, aY)*timeline)));
            if((VectorCalculation.abs(getvX(), getvY()))-(VectorCalculation.abs(aX, aY)*timeline)<=-0.3 && elapsedTime != 0);
//            if (this.getPosX()-x>=0 && this.getPosY()-y>=0 && elapsedTime != 0);
            else{
            setPosX(x);
            setPosY(y);
            }
            callListener();
        }
    }
    
    /** Aufruf falls ein Barrel kollidiert werden soll.
     * @param that Das Barrel mit dem die Kollision berechnet werden soll.
     * @param time Vergangene Zeit seit dem letzten Aufruf.
     */
    private void collideBarrel(final GameModel that) {
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - that.getPosX(), 2)
                        + Math.pow(this.getPosY() - that.getPosY(), 2));
        
        // Wenn sich die Kreise beruehren (Distanz <= der Radien)
        // Sonst entfernen aus der Kollisionsliste
        if (distance <= this.getSize()/2 + that.getSize()/2) {
            // Berechnung des neuen Bewegungsvektors der Kugel (this)
            collideCircle(this, that);
        } else {
            collided.remove(that);
        }
    }
    
    /** Aufruf falls ein Ball kollidiert werden soll.
     * @param that Der Ball mit dem kollidiert werden soll.
     * @param time Vergangene Zeit seit dem letzten Aufruf.
     */
    private void collideBall(final GameModel that) {
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - that.getPosX(), 2)
                        + Math.pow(this.getPosY() - that.getPosY(), 2));
        
        // Wenn sich die Kreise beruehren (Distanz <= der Radien)
        // Sonst entfernen aus der Kollisionsliste
        if (distance <= this.getSize()/2 + that.getSize()/2) {
            // Berechnung der neuen Bewegungsvektoren der Bälle
            collideCircle(this, that);
            collideCircle(that, this);
        } else {
            collided.remove(that);
            ((Ball) that).removeCollided(this);
        }
    }
    
    /** Berechnet den aus der Kollision zweier Kreise resultierenden Geschwindigkeitsvektor.
     * @param first Kreis mit Bewegung.
     * @param second Kreis mit dem kollidiert wird.
     */
    private void collideCircle(final GameModel first, final GameModel second) {
        if (!collided.contains(second)) {
            // Vektor zwischen den Mittelpunkten der Kreise -> Distanzvektor
            double distanceX = second.getPosX() - first.getPosX();
            double distanceY = second.getPosY() - first.getPosY();
            
            // Winkel zwischen Distanzvektor und x-Achse
            double phi = Math.atan2(distanceY, distanceX);

            // Rotation
            double rotVeloX = Math.cos(phi) * first.getvX() - Math.sin(phi) * first.getvY();
            double rotVeloY = Math.sin(phi) * first.getvX() + Math.cos(phi) * first.getvY();

            // an der y-Achse spiegeln
            rotVeloX = -rotVeloX;

            // Rueckrotation des Geschwindigkeitsvektors und anpassen des y-Vektors
            double newVeloX = Math.cos(-phi) * rotVeloX - Math.sin(-phi) * rotVeloY;
            double newVeloY = Math.sin(-phi) * rotVeloX + Math.cos(-phi) * rotVeloY;
            newVeloX = -newVeloX;
            
            // Setzen des neuen Richtungswinkels
            double newPhi = Math.atan2(newVeloY, newVeloX);
            first.setRotation(Math.toDegrees(newPhi));

            // Setzen des neuen Geschwindkeikeitsvektors
            setvX(VectorCalculation.abs(getvX(), getvY()) * Math.cos(newPhi));
            setvY(VectorCalculation.abs(getvX(), getvY()) * Math.sin(newPhi));
            
            // Aktualisieren der Reibung
            aX = (-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
            aY = (-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
            
            // Als bearbeitet listen
            collided.add(second);
        }
    }
    
    private void checkCollideBoxShapes(final GameModel that, double time) {
        if(that.toString().equals("Puffer")){
            System.out.print("puff");
            collidePuffer(that);
        }else{
            double[] cornerPoints=that.getCornerPoints();
            for(int c=0;c<=cornerPoints.length-3;c=c+2){
                double a=cornerPoints[c]-cornerPoints[c+2];
                double b=cornerPoints[c+1]-cornerPoints[c+3];
                gX.add(a);
                gY.add(b);
                ngX.add(-gY.get(gY.size()-1));
                ngY.add(gX.get(gX.size()-1));
                double e= VectorCalculation.times(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1), getPosX()-cornerPoints[c], getPosY()-cornerPoints[c+1]);
                double d= Math.abs(e)/VectorCalculation.abs(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1));
                if(!checkCollisionCorner(cornerPoints, c)){                   
                    if(d<=getSize()/2&&getPosX()>cornerPoints[c]&&getPosX()<cornerPoints[c+2]){
                        System.out.println(d+"distanceA");
                        collideBoxShapes(that,that.toString());
                    }
                    if(d<=getSize()/2&&getPosY()>cornerPoints[c+1]&&getPosY()<cornerPoints[c+3]){
                        System.out.println(d+"distanceB");
                        collideBoxShapes(that,that.toString());                 
                    }
                    if(d<=getSize()/2&&getPosX()<cornerPoints[c]&&getPosX()>cornerPoints[c+2]){
                        System.out.println(d+"distanceC");
                        collideBoxShapes(that,that.toString());
                    }
                    if(d<=getSize()/2&&getPosY()<cornerPoints[c+1]&&getPosY()>cornerPoints[c+3]){
                        System.out.println(d+"distanceD");
                        collideBoxShapes(that,that.toString());
                    }
                }
            }
        }
    }

    private Boolean checkCollisionCorner(double[] cornerPoints, int c) {
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - cornerPoints[c], 2)
                        + Math.pow(this.getPosY() - cornerPoints[c+1], 2));
        if(distance<=getSize()/2){         
            System.out.println(distance+" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            setRotation(180+getRotation());
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            setvX(VectorCalculation.abs(getvX(),getvY())*cos);
            setvY(VectorCalculation.abs(getvX(),getvY())*sin);
            aX = (-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
            aY = (-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
            return true;
        }
        return false;
    }
    
    private void collideSpring(GameModel that) {                 
                    //System.out.println(springVel + "sp");
                    setvX((VectorCalculation.abs(getvX(),getvY())+springVel)*cos);
                    setvY((VectorCalculation.abs(getvX(),getvY())+springVel)*sin);
                    aX = (-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
                    aY = (-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
    }

    public void checkCollisionBoundary(Config config) {
       double[][] upperBoundaries=config.getUpperBoundary();
       double[][] lowerBoundaries=config.getLowerBoundary();
       
       for(int k=0;k<2;k++){
            double[][] activeDouble;
            if(k==0){
               activeDouble=upperBoundaries;
            }else{
               activeDouble=lowerBoundaries;
            }
       
            for(int c=0;c<activeDouble.length-1;c++){
                double a=activeDouble[c][0]-activeDouble[c+1][0];
                double b=activeDouble[c][1]-activeDouble[c+1][1];

                double nX=(-b);
                double nY=(a);
                double e= VectorCalculation.times(nX, nY, getPosX()-upperBoundaries[c][0], getPosY()-upperBoundaries[c][1]);
                double d= Math.abs(e)/VectorCalculation.abs(nX, nY);
                if(k==0){
                    if(d<=getSize()/2 && getPosX()<upperBoundaries[c+1][0] && getPosX()>upperBoundaries[c][0]){
                                collisionBoundary(d, nY, nX);
                    }
                }else{
                     if(d<=getSize()/2 && getPosX()<lowerBoundaries[c+1][0] && getPosX()>lowerBoundaries[c][0]){
                         collisionBoundary(d, nY, nX);
                     }
                }
            }
        }          
        for(int i = 0;i<7;i=i+6){
            double a=upperBoundaries[i][0]-lowerBoundaries[i][0];
            double b=upperBoundaries[i][1]-lowerBoundaries[i][1];
                
            double nX=(-b);
            double nY=(a);
            double e= VectorCalculation.times(nX, nY, getPosX()-upperBoundaries[i][0], getPosY()-upperBoundaries[i][1]);
            double d= Math.abs(e)/VectorCalculation.abs(nX, nY);
            if(d<=getSize()/2 && getPosY()<lowerBoundaries[i][1] && getPosY()>upperBoundaries[i][1]){
                collisionBoundary(d, nY, nX);
            }
        }
    }

    private void collisionBoundary(double d, double nY, double nX) {
        System.out.println(d);
        double alpha= Math.toDegrees(Math.atan(getvY()/getvX()));
        double beta= Math.toDegrees(Math.atan(nY/nX));
        double gamma = alpha-(2*beta);
        double delta= 180-getRotation()-gamma;
        System.out.println("gamma: " + gamma + " delta: " + delta+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        setRotation(delta);
        cos= Math.cos(Math.toRadians(getRotation()));
        sin= Math.sin(Math.toRadians(getRotation()));
        setvX(VectorCalculation.abs(getvX(),getvY())*cos);
        setvY(VectorCalculation.abs(getvX(),getvY())*sin);
        aX = (-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
        aY = (-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient();
    }
}
