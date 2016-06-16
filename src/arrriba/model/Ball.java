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
    /** Startgeschwindigkeit des Balles. */
    private double velocity;
    /** Startposition des Balles. */
    private double startX;
    private double startY;
    
    /** Ob im letzten Frame eine Kollision stattgefunden hat. */
    private Boolean justHit=false;
    /** Der Zeitpunkt ab dem wieder eine Kollision stattfinden kann. */
    private double allowedHitTime=0;
    /** Cosinus und Sinus. */
    private double cos;
    private double sin;

    /** Volumen des Balls. */
    private double volume;
    /** Die Zeit die vergangen ist. */
    private double timeline;
    /** Ob das Spiel beendet ist. */
    private boolean finish = false;
    /** Die Reibung. */
//    private double aX=0;
//    private double aY=0;
    
    /** Zeitpunkt an dem der Ball eine Feder trifft. */
    private double springHitTime=0;
    /** Geschwindigkeit nach dem Treffen der Feder. */
    private double springVel=0;
    /** Auslenkung der Feder aus der Ruhelage. */
    private double s=0;

    /** Geraden des Rechtecks mit dem kollidiert wird. */
    private ArrayList<Double> gX=new ArrayList<Double>();
    private ArrayList<Double> gY=new ArrayList<Double>();
    /** Normalen der Geraden mit denen kollidiert wird. */
    private ArrayList<Double> ngX=new ArrayList<Double>();
    private ArrayList<Double> ngY=new ArrayList<Double>();
    
    /** Speichert aktuell kollidierte Objekte, solange diese in der Naehe sind. */
    private ArrayList<GameModel> collided = new ArrayList<GameModel>();
    /** Das Material der Kugel. */
    private Material material;  
    
    /** Konstruktor des Balls.
     * @param size Durchmesser des Balls.
     * @param posX X-Position des Balls.
     * @param posY Y-Ppsition des Balls.
     * @param velocity Geschwindigkeit des Balls.
     * @param rotation Rotation des Balls.
     */
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double rotation) {
        this(size, posX, posY, velocity, rotation, new Wood());
    }
    
    /** 
     * @param size Durchmesser des Balls.
     * @param posX X-Position des Balls.
     * @param posY Y-Ppsition des Balls.
     * @param velocity Geschwindigkeit des Balls.
     * @param rotation Rotation des Balls.
     * @param material Material des Balls.
     */
    public Ball(final int size, final double posX, final double posY,
            final double velocity, final double rotation, final Material material) {
        // Erstellt das Shape
        Circle shape = new Circle(posX, posY, size / 2);
        shape.setFill(Paint.valueOf("RED"));
        double density = material.getDensity();
        double sizeD=size;
        this.setShape(shape);
        // Skalierungsfaktor 100 Pixel = 0.1 Meter
        this.setSize(sizeD/scaleFactor);
        this.setPosX(posX/scaleFactor);
        this.setPosY(posY/scaleFactor);
        this.setRotation(rotation);
        this.setVelocity(velocity/scaleFactor);
        this.setStartX(posX/scaleFactor);
        this.setStartY(posY/scaleFactor);
        this.setMaterial(material);
        this.setaX(0);
        this.setaY(0);
    }

    /** Getter der Geschwindigkeit.
     * @return Die Geschwindigkeit.
     */
    public double getVelocity() {
        return velocity;
    }
    
    /** Getter der Startposition des Balls.
     * @return Die Startposition.
     */
    public double getStartX(){
        return startX;
    }
    
    /** Getter der Startposition des Balls.
     * @return Die Startposition.
     */
    public double getStartY(){
        return startY;
    }

    /** Getter für das Material des Balls
     * @return Das Material des Balls
     */
    public Material getMaterial() {
        return material;
    }
    
    /** Gibt zurueck ob die Kugel im Ziel ist oder nicht.
     * @return True wenn der Ball im Ziel ist, sonst false.
     */
    public boolean isFinished() {
        return finish;
    }
    
    /** Typ des Objekts.
     * @return Der Typ des Objekts.
     */
    @Override
    public String toString() {
        return "Ball";
    }
    
    /** Setter des Durchmessers des Balls.
     * @param size Der Durchmesser des Balls.
     */
    @Override
    public void setSize(final double size) {
        super.setSize(size);
        volume = (getSize()*getSize()*Math.PI)/4;
    }
    
    /** Setter für die Geschwindigkeit des Balls.
     * @param velocity Die Geschwindigkeit des Balls.
     */
    public void setVelocity(double velocity){
        this.velocity=velocity;
    }
    
    /** Berechnet den Geschwindigkeitsvektor.
     * @param elapsedTime Zeit die seit dem letztem Aufruf vergangen ist.
     */
    public void setVelocityVector(final double elapsedTime) {
        // Die insgesamt vergangene Zeit.
        timeline += elapsedTime;
        // Zu Beginn des Spiels.
        if (timeline==0+elapsedTime){
            // Berechnung des Cosinus und Sinus des Rotationswinkels des Balls.
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            // Berechnung des Geschwindigkeitsvektors.
            setvX(getVelocity()*cos);
            setvY(getVelocity()*sin);
            // Berechnung der Reibung.
            setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
        }else if(VectorCalculation.abs(getvX(), getvY())<=0){
            setvX(0);
            setvY(0);
        // Der normale Fall wöhrend das Spiel läuft.    
        }else{
            // Berechnung des Geschwindigkeitsvektors.
            setvX(getvX()+getaX()*elapsedTime);
            setvY(getvY()+getaY()*elapsedTime);
        }  
    }
    
    /** Setter für die Startposition des Balls.
     * @param startX Die Startposition des Balls.
     */
    public void setStartX(final double startX) {
        this.startX = startX;
    }
    
    /** Setter für die Startposition des Balls.
     * @param startY Die Startposition des Balls.
     */
    public void setStartY(final double startY) {
        this.startY = startY;
    }

    /** Setter für das Material der Kugel.
     * @param material Das Material der Kugel.
     */
    public void setMaterial(Material material) {
        this.material=material;
        // Die Dichte des Materials.
        double density = material.getDensity();
        // Die Masse des Materials.
        setMass(density*volume);
        // Die Textur des Balls abhängig vom Materaial. 
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
    }
    
    /** Entfernt den uebergebenen Ball aus der Kollisionsliste.
     * @param that Der zu entfernende Ball.
     */
    protected void removeCollided(final GameModel that) {
        collided.remove(that);
    }
    
    /** Überprüft ob eine Kollision stattfindet.
     * @param that Das Objekt das überprüft wird.
     * @param elapsedTime Die vergangene Zeit seit dem letzten Aufruf.
     */
    public void checkCollision(final GameModel that, final double elapsedTime) {
        // Wenn das Spiel nicht beendet ist.
        // Wenn im letzten Frame keine Kollision stattgefunden hat.
        if (!isFinished() && !justHit && allowedHitTime <= timeline) {
            // Wenn im letzten Frame keine Kollision stattgefunden hat.
                // Berechnung des Geschwindigkeitsvektors.
                setVelocityVector(elapsedTime);
                // Je nach Objekt welches ueber that uebergeben wird.
                String name = that.toString();
                // Wenn der Ball immer noch von der Feder beschleunigt wird.
                if(VectorCalculation.abs(getvX(), getvY())*elapsedTime<=s && springHitTime > 0 && (s/VectorCalculation.abs(getvX(), getvY()))-(timeline-springHitTime)>0){
                    collideSpring();
                }
                switch (name) {
                    // Wenn das Objekt ein Loch ist.
                    case "Hole": collideHole(that);
                    break;
                    // Wenn das Objekt eine Tonne ist.    
                    case "Barrel": collideBarrel(that);
                    break;
                    // Wenn das Objekt ein andere Ball ist.
                    case "Ball": collideBall(that);
                    break;
                    // Wenn das Objekt ein Rechteck ist.
                    default: checkCollideBoxShapes(that, elapsedTime);
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
    }

    /** Berechnet den Abprallwinkel bei Objekten mit einer rechteckigen Boundingbox.
     * @param that Das Rechteck.
     * @param name Typ des Rechtecks.
     */
    private void collideBoxShapes(GameModel that,String name) {
            // Berechnung des Aufprallwinkels.
            double alpha= Math.toDegrees(Math.atan(getvY()/getvX()));
            double beta= Math.toDegrees(Math.atan(ngY.get(ngY.size()-1)/ngX.get(ngX.size()-1)));
            double gamma = alpha-(2*beta);
            // Berechnung des Abprallwinkels.
            double delta= 180-getRotation()-gamma;
            setRotation(delta);
            // Berechnung des Cosinus und Sinus des Abprallwinkels.
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            // Wenn das Rechteck eine Feder ist.
            if(name.equals("Spring")){
                // Zeit bei der der Ball die Feder trifft. 
                springHitTime=timeline;
                // Auslenkung aus der Ruhelage.
                s = that.getSize()/2;
                // Die Federkonstante.
                double D = 20;
                // Geschwindigkeit nach dem Treffer.
                springVel=Math.sqrt((D*s*s/getMass()));
                collideSpring();
            // Normales Rechteck    
            }else{
                // Berechnung des Geschwindigkeitsvektors.
                setvX(VectorCalculation.abs(getvX(),getvY())*cos);
                setvY(VectorCalculation.abs(getvX(),getvY())*sin);
                // Neuberechnung der Reibung.
                setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
                setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
            }
            System.out.println("gamma: " + gamma + " delta: " + delta+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");                
    }

    /** Überprüft ob eine Kollision stattfindet und berechnet die Beschleunigung im Luftstrom.
     * @param that Der Kugelfisch
     */
    private void collidePuffer(GameModel that) {
        // Die Eckpunkte des Rechtecks.
        double[] cornerPoints=that.getCornerPoints();
        // Die Abstände zu den Seiten des Rechtecks.
        ArrayList<Double> distance=new ArrayList();
        // Für jede Seite.
        for(int c=0;c<=cornerPoints.length-3;c=c+2){
            // Geraden aufstellen
            double a=cornerPoints[c]-cornerPoints[c+2];
            double b=cornerPoints[c+1]-cornerPoints[c+3];
            // Normale
            double nX=-b;
            double nY=a;
            // Abstandsberechnung
            double e= VectorCalculation.times(nX, nY, getPosX()-cornerPoints[c], getPosY()-cornerPoints[c+1]);
            distance.add(Math.round(Math.abs(e)/VectorCalculation.abs(nX, nY)*1000)/1000.0);
        }
        // Wenn der Ballmittelpunkt innerhalb des Rechtecks ist.
        if(distance.get(0)+distance.get(2)==that.getSize() && distance.get(1)+distance.get(3)==(that.getSize()*2)){
            // Berechnung des Cosinus und Sinus der Rotation des Kugelfischs. 
            double cosPuf= Math.cos(Math.toRadians(that.getRotation()));
            double sinPuf= Math.sin(Math.toRadians(that.getRotation()));
            // Die Beschleunigung.
            double a=0.1;
            // Die Geschwindigkeit.
            double vel=0.5*a*timeline*timeline;
            // Berechnung des Geschwindigkeitsvektors.
            setvX(getvX()+vel*cosPuf);
            setvY(getvY()+vel*sinPuf);
            // Neuberechnung der Reibung
            setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
        }
    }
    
    
    /** Bewegt die Kugel pro Zeitabschnitt weiter.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    public void move(final double elapsedTime) {
        
        if (!isFinished()) {
            setVelocityVector(elapsedTime);
            // Die Position nach der Bewegung.
            double x = 0.5*getaX()*elapsedTime*elapsedTime+elapsedTime*getvX()+this.getPosX();
            double y = 0.5*getaY()*elapsedTime*elapsedTime+elapsedTime*getvY()+this.getPosY();
            System.out.println(Math.round(x*1000)/1000.0);
            System.out.println(getPosX());
            if(Math.round(x*1000)/1000.0==Math.round(this.getPosX()*1000)/1000.0 && Math.round(y*1000)/1000.0==Math.round(this.getPosY()*1000)/1000.0 && timeline != 0){
                setaX(0);
                setaY(0);
                setvX(0);
                setvY(0);
            }
//            if (this.getPosX()-x>=0 && this.getPosY()-y>=0 && elapsedTime != 0);
            else{
            setPosX(x);
            setPosY(y);
            }
        }
    }
    
    /** Aufruf falls ein Loch kollidiert werden soll.
     * Legt den Ball bei einer Kollision still.
     * @param that Das Loch mit dem die Kollision berechnet werden soll.
     */
    private void collideHole(final GameModel that) {
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - that.getPosX(), 2)
                        + Math.pow(this.getPosY() - that.getPosY(), 2));
        
        if (distance <= this.getSize() / 2 * that.getSize() / 2) {
            this.setPosX(that.getPosX());
            this.setPosX(that.getPosY());
            this.setFinished();
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
        
//            System.out.println("1. d: " + Math.pow(this.getPosX() - that.getPosX(), 2));
//            System.out.println("2. d: " + Math.pow(this.getPosY() - that.getPosY(), 2));
        // Wenn sich die Kreise beruehren (Distanz <= der Radien)
        // Sonst entfernen aus der Kollisionsliste
        if (distance <= this.getSize()/2 + that.getSize()/2) {
//            System.out.println("Dist: " + distance);
//            System.out.println("Radi: " + (this.getSize()/2 + that.getSize()/2));
            // Berechnung der neuen Bewegungsvektoren der Bälle
            
            // Normalenvektor zwischen den Kugeln
            double normX = that.getPosX() - this.getPosX();
            double normY = that.getPosY() - this.getPosY();
            
            // Relative Geschwindigkeit von this zu that
            double vRelX = that.getvX() - this.getvX();
            double vRelY = that.getvY() - this.getvY();
            
            double scalar = VectorCalculation.times(vRelX, vRelY, normX, normY);
//            System.err.println("Scalar: " + scalar);
            if (scalar < 0) {
//                System.err.println("arrriba.model.Ball.collideBall()");
                double[] resultThis = calculateBallCollisionVelocity(this, normX, normY);
                double[] resultThat = calculateBallCollisionVelocity(that, normX, normY);
                
                // Komponente fuer die andere Kugel
                double[] transferVeloAtoB = {resultThis[0], resultThis[1]};
                
                // Komponente fuer diese Kugel
                double[] ownVeloA = {resultThis[2], resultThis[3]};
                
                // Komponente fuer die andere Kugel
                double[] transferVeloBtoA = {resultThat[0] ,resultThat[1]};
                
                // Komponente fuer diese Kugel
                double[] ownVeloB = {resultThat[2], resultThat[3]};
                
                // Zusammenbauen der neuen Vektoren
                
                // Geschwindigkeit fuer this
                double newVeloAX = ownVeloA[0] + transferVeloBtoA[0];
                double newVeloAY = ownVeloA[1] + transferVeloBtoA[1];
                
                // Geschwindigkeit fuer that
                double newVeloBX = ownVeloB[0] + transferVeloAtoB[0];
                double newVeloBY = ownVeloB[1] + transferVeloAtoB[1];
                
                // Setzen der neuen Geschwindigkeiten
                this.setvX(newVeloAX);
                this.setvY(newVeloAY);
                that.setvX(newVeloBX);
                that.setvY(newVeloBY);
                
                // Setzen der neuen Rotation
                this.setRotation(Math.toDegrees(Math.atan2(newVeloAY, newVeloAX)));
                that.setRotation(Math.toDegrees(Math.atan2(newVeloBY, newVeloBX)));
                
                // Aktualisieren der Reibung
                this.setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
                this.setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
                that.setaX((-that.getvX()/VectorCalculation.abs(that.getvX(), that.getvY()))*material.getFrictionCoefficient());
                that.setaY((-that.getvY()/VectorCalculation.abs(that.getvX(), that.getvY()))*material.getFrictionCoefficient());
            }
        } else {
            collided.remove(that);
            ((Ball) that).removeCollided(this);
        }
    }
    
    /** Berechnet die Komponenten des Geschwindigkeitsvektors von viewpoint.
     * @param viewpoint Kugel von der aus die Kollision betrachtet wird.
     * @param normX Normalenvektor zwischen den Kugeln (x).
     * @param normY Normalenvektor zwischen den Kugeln (y).
     * @return Transferkomponente an die andere Kugel, eigene Komponente (tX, tY, eX, eY).
     */
    private double[] calculateBallCollisionVelocity(final GameModel viewpoint, final double normX, final double normY) {
        // Winkel zwischen x-Achse und Geschwindigkeitsvektor
        double phi = Math.atan2(viewpoint.getvY(), viewpoint.getvX());

        // Drehen des Normalenvektors
        double rotNormX = Math.cos(phi) * normX - Math.sin(phi) * normX;
        double rotNormY = Math.sin(phi) * normY + Math.cos(phi) * normY;

        // Winkel zwischen Normalenvektor und Geschwindigkeit
        double alpha = Math.atan2(rotNormY, rotNormX);

        // dritter Winkel des Dreiecks
        double gamma = Math.PI - (Math.PI / 2) - alpha;

        // Betrag der ersten uebertragenen Geschwindigkeit
        double transferVeloAtoB = (VectorCalculation.abs(viewpoint.getvX(), viewpoint.getvY()) * Math.sin(gamma)) / Math.sin(Math.PI / 2);
        double factorTransferVeloAtoB = transferVeloAtoB / VectorCalculation.abs(normX, normY);

        // Komponente fuer die andere Kugel
        double transferVeloAtoBX = normX * factorTransferVeloAtoB;
        double transferVeloAtoBY = normY * factorTransferVeloAtoB;

        // Komponente fuer diese Kugel
        double ownVeloAX = viewpoint.getvX() - transferVeloAtoBX;
        double ownVeloAY = viewpoint.getvY() - transferVeloAtoBY;
//        System.out.println("x: " + viewpoint.getvX() + " y: " + viewpoint.getvY());
        double[] returnValues = {transferVeloAtoBX, transferVeloAtoBY, ownVeloAX, ownVeloAY};
//        System.out.println("transX: " + returnValues[0] + " transY: " + returnValues[1]
//                          + " ownX: " + returnValues[2] + " ownY: " + returnValues[3]);
        return returnValues;
    }
    
    /** Berechnet den aus der Kollision zweier Kreise resultierenden Geschwindigkeitsvektor.
     * @param first Kreis mit Bewegung.
     * @param second Kreis mit dem kollidiert wird.
     */
    private void collideCircle(final GameModel first, final GameModel second) {
        if (!collided.contains(second)) {
            System.out.println("Ball coll");
            // Vektor zwischen den Mittelpunkten der Kreise -> Distanzvektor
            double distanceX = second.getPosX() - first.getPosX();
            double distanceY = second.getPosY() - first.getPosY();
            
            // Winkel zwischen Distanzvektor und x-Achse
            double phi = Math.atan2(distanceY, distanceX);

            // ############################################################################################ minus phi?
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
            setaY((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
            
            // Als bearbeitet listen
            collided.add(second);
        }
    }
    
    /** Prüft ob eine Kollision mit einem Rechteck vorliegt.
     * @param that Das Rechteck das überprüft wird.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    private void checkCollideBoxShapes(final GameModel that, double elapsedTime) {
        // Wenn das Rechteck ein Kugelfisch ist.
        if(that.toString().equals("Puffer")){
            collidePuffer(that);
        }else{
            // Die Eckpunkte des Rechtecks.
            double[] cornerPoints=that.getCornerPoints();
            // Für jede Seite des Rechtecks.
            for(int c=0;c<=cornerPoints.length-3;c=c+2){
                double a=cornerPoints[c]-cornerPoints[c+2];
                double b=cornerPoints[c+1]-cornerPoints[c+3];
                gX.add(a);
                gY.add(b);
                ngX.add(-gY.get(gY.size()-1));
                ngY.add(gX.get(gX.size()-1));
                // Abstandsberchnung.
                double e= VectorCalculation.times(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1), getPosX()-cornerPoints[c], getPosY()-cornerPoints[c+1]);
                double d= Math.abs(e)/VectorCalculation.abs(ngX.get(ngX.size()-1), ngY.get(ngY.size()-1));
                // Wenn der Ball keine Ecke trifft.
                if(!checkCollisionCorner(cornerPoints, c, elapsedTime,that)){
                    // Wenn der Ball mit der Bande kollidiert.
                    if(d<=getSize()/2&&getPosX()>cornerPoints[c]&&getPosX()<cornerPoints[c+2]){
                        System.out.println(d+"distanceA");
                        justHit=true;
                        allowedHitTime=timeline+2*elapsedTime;
                        collideBoxShapes(that,that.toString());
                    }else if(d<=getSize()/2&&getPosY()>cornerPoints[c+1]&&getPosY()<cornerPoints[c+3]){
                        System.out.println(d+"distanceB");
                        justHit=true;
                        allowedHitTime=timeline+2*elapsedTime;
                        collideBoxShapes(that,that.toString());                 
                    }else if(d<=getSize()/2&&getPosX()<cornerPoints[c]&&getPosX()>cornerPoints[c+2]){
                        System.out.println(d+"distanceC");
                        justHit=true;
                        allowedHitTime=timeline+2*elapsedTime;
                        collideBoxShapes(that,that.toString());
                    }else if(d<=getSize()/2&&getPosY()<cornerPoints[c+1]&&getPosY()>cornerPoints[c+3]){
                        System.out.println(d+"distanceD");
                        justHit=true;
                        allowedHitTime=timeline+2*elapsedTime;
                        collideBoxShapes(that,that.toString());
                    }
                }
            }
        }
    }

    /** Überprüft ob der Ball eine Ecke des Rechtecks trifft und berechnet danach den Abprallwinkel.
     * @param cornerPoints Die Eckpunkte.
     * @param c Index des X-Wertes des Eckpunkts.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     * @param that Das Rechteck.
     * @return True, wenn eine Kollision stattgefunden hat.
     */
    private Boolean checkCollisionCorner(double[] cornerPoints, int c,double elapsedTime,GameModel that) {
        // Abstand zwischen Ballmittelpunkt und dem Eckpunkt.
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - cornerPoints[c], 2)
                        + Math.pow(this.getPosY() - cornerPoints[c+1], 2));
        // Wenn der Abstand kleiner oder gleich dem Radius des Balls ist.
        if(distance<=getSize()/2){
            justHit=true;
            allowedHitTime=timeline+2*elapsedTime;
            System.out.println(distance+" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            switch (c) {
                // Abprall obere linke Ecke.
                case 0:
                    setRotation(225+that.getRotation());
                    break;
                // Abprall obere rechte Ecke.    
                case 2:
                    setRotation(315+that.getRotation());
                    break;
                // Abprall untere rechte Ecke.
                case 4:
                    setRotation(45+that.getRotation());
                    break;
                // Abprall untere linke Ecke.
                case 6:
                    setRotation(135+that.getRotation());
                    break;
                default:
                    break;
            }
            System.out.println(c);
            // Berechnung des Cosinus und Sinus des Abprallwinkels.
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            // Berechnung des Geschwindigkeitsvektors.
            setvX(VectorCalculation.abs(getvX(),getvY())*cos);
            setvY(VectorCalculation.abs(getvX(),getvY())*sin);
            // Neuberechnung der Reibung.
            setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
            return true;
        }
        return false;
    }
    
    /**Berechnung des Abprallwinkels bei einer Kollision mit einer Feder.
     */
    private void collideSpring() {
                    // Berechnung des Geschwindigkeitsvektors.
                    setvX((VectorCalculation.abs(getvX(),getvY())+springVel)*cos);
                    setvY((VectorCalculation.abs(getvX(),getvY())+springVel)*sin);
                    // Neuberechnung der Reibung.
                    setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
                    setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
    }

    /** Überprüft ob der Ball mit den Banden kollidiert.
     * @param config Die Bande.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    public void checkCollisionBoundary(Config config , double elapsedTime) {
        // Die obere Hälfte der Bande.
        double[][] upperBoundaries=config.getUpperBoundary();
        // Die untere Hälfte der Bande.
        double[][] lowerBoundaries=config.getLowerBoundary();
        
        // Wenn der Kollisionszeitpunkt früher als die jetzige Zeit ist.
        if(allowedHitTime<=timeline){
            justHit=false;
        }
        
        // Wenn im letzten Frame keine Kollision stattgefunden hat.
        if(!justHit&&allowedHitTime<=timeline){
            // Für die untere und obere Hälfte.
            for(int k=0;k<2;k++){
                double[][] activeDouble;
                if(k==0){
                    activeDouble=upperBoundaries;
                }else{
                    activeDouble=lowerBoundaries;
                }              
                // Für jede Gerade
                for(int c=0;c<activeDouble.length-1;c++){
                    // Geraden aufstellen
                    double a=activeDouble[c][0]-activeDouble[c+1][0];
                    double b=activeDouble[c][1]-activeDouble[c+1][1];
                    // Normale
                    double nX=(-b);
                    double nY=(a);
                    // Abstandsberechnung.
                    double e= VectorCalculation.times(nX, nY, getPosX()-activeDouble[c][0], getPosY()-activeDouble[c][1]);
                    double d= Math.abs(e)/VectorCalculation.abs(nX, nY);
                    // Wenn die obere Hälfte überprüft wird.
                    if(k==0){
                        // Wenn der Ball mit der Bande kollidiert.
                        if(d<=getSize()/2 && getPosX()<upperBoundaries[c+1][0] && getPosX()>upperBoundaries[c][0]){
                            collisionBoundary(d, nY, nX);
                            justHit=true;
                            allowedHitTime=timeline+2*elapsedTime;
                        }
                    // Wenn die untere Hälfte überprüft wird.    
                    }else{
                        // Wenn der Ball mit der Bande kollidiert.
                        if(d<=getSize()/2 && getPosX()<lowerBoundaries[c+1][0] && getPosX()>lowerBoundaries[c][0]){
                            collisionBoundary(d, nY, nX);
                            justHit=true;
                            allowedHitTime=timeline+2*elapsedTime;
                        }
                    }
                }
            }
            // Für die linke und rechte Seite der Bande.
            for(int i = 0;i<7;i=i+6){
                // Geraden aufstellen.
                double a=upperBoundaries[i][0]-lowerBoundaries[i][0];
                double b=upperBoundaries[i][1]-lowerBoundaries[i][1];
                // Normale.
                double nX=(-b);
                double nY=(a);
                // Abstandsberchnung
                double e= VectorCalculation.times(nX, nY, getPosX()-upperBoundaries[i][0], getPosY()-upperBoundaries[i][1]);
                double d= Math.abs(e)/VectorCalculation.abs(nX, nY);
                // Wenn der Ballmittelpunkt mit der Bande kollidiert.
                if(d<=getSize()/2 && getPosY()<lowerBoundaries[i][1] && getPosY()>upperBoundaries[i][1]){
                    collisionBoundary(d, nY, nX);
                    justHit=true;
                    allowedHitTime=timeline+2*elapsedTime;
                }
            }
        }
    }

    /** Berechnet den Abprallwinkel des Balls nach Kollision mit der Bande.
     * @param d Der Abstand des Balls zum Eckpunkt.
     * @param nY Der Y_Wert der Normalen. 
     * @param nX Der X-Wert der Normalen.
     */
    private void collisionBoundary(double d, double nY, double nX) {
        System.out.println(d);
        // Berechnung des Aufprallwinkels.
        double alpha= Math.toDegrees(Math.atan(getvY()/getvX()));
        double beta= Math.toDegrees(Math.atan(nY/nX));
        double gamma = alpha-(2*beta);
        // Berechnung des Abprallwinkels.
        double delta= 180-getRotation()-gamma;
        System.out.println("gamma: " + gamma + " delta: " + delta+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // Setzen des Rotationswinkels.
        setRotation(delta);
        // Berechnung des Cosinus und Sinus des Abprallwinkels.
        cos= Math.cos(Math.toRadians(getRotation()));
        sin= Math.sin(Math.toRadians(getRotation()));
        // Berechnung des Geschwindigkeitsvektors.
        setvX(VectorCalculation.abs(getvX(),getvY())*cos);
        setvY(VectorCalculation.abs(getvX(),getvY())*sin);
        // Neuberechning der Reibung.
        setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
        setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*material.getFrictionCoefficient());
    }
}
