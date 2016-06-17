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

public class Ball extends GameModel {
    /** Startgeschwindigkeit des Balles. */
    private double velocity;
    /** Startposition des Balles (x). */
    private double startX;
    /** Startposition des Balles (y). */
    private double startY;
    

    /** Cosinus. */
    private double cos;
    /** Sinus. */
    private double sin;

    /** Volumen des Balls. */
    private double volume;
    /** Die Zeit die vergangen ist. */
    private double timeline;
    /** Ob das Spiel beendet ist. */
    private boolean finish = false;
    
    /** Geschwindigkeit nach dem Treffen der Feder. */
    private double springVel=0;
    /** Auslenkung der Feder aus der Ruhelage. */
    private double s=0;
    
    /** Speichert aktuell kollidierte Objekte, solange diese in der Naehe sind. */
    private ArrayList<GameModel> collided = new ArrayList<GameModel>();
    
    /** Konstruktor des Balls.
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
        this.setSize(sizeD/SCALE_FACTOR);
        this.setPosX(posX/SCALE_FACTOR);
        this.setPosY(posY/SCALE_FACTOR);
        this.setRotation(rotation);
        this.setVelocity(velocity/SCALE_FACTOR);
        this.setStartX(posX/SCALE_FACTOR);
        this.setStartY(posY/SCALE_FACTOR);
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
    
    /** Gibt zurueck ob die Kugel im Ziel ist oder nicht.
     * @return True wenn der Ball im Ziel ist, sonst false.
     */
    public boolean isFinished() {
        return finish;
    }
    
    /** Gibt den Typ zurueck.
     * @return Gibt "Ball" zurueck
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
    
    /** Setter fuer die Geschwindigkeit des Balls.
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
            setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
        }else if(VectorCalculation.abs(getvX(), getvY())<=0){
            setvX(0);
            setvY(0);
        // Der normale Fall woehrend das Spiel laeuft.    
        }else{
            // Berechnung des Geschwindigkeitsvektors.
            setvX(getvX()+getaX()*elapsedTime);
            setvY(getvY()+getaY()*elapsedTime);
        }
    }
    
    /** Setter fuer die Startposition des Balls.
     * @param startX Die Startposition des Balls.
     */
    public void setStartX(final double startX) {
        this.startX = startX;
    }
    
    /** Setter fuer die Startposition des Balls.
     * @param startY Die Startposition des Balls.
     */
    public void setStartY(final double startY) {
        this.startY = startY;
    }

    /** Setter fuer das Material der Kugel.
     * @param material Das Material der Kugel.
     */
    public void setMaterial(Material material) {
        super.setMaterial(material);
        // Die Dichte des Materials.
        double density = material.getDensity();
        // Die Masse des Materials.
        setMass(density*volume);
        // Die Textur des Balls abhaengig vom Materaial. 
        String texturePath = material.getTexturePath();
        Image texture = new Image(texturePath);
        getShape().setFill(new ImagePattern(texture, 0, 0, 1, 1, true));
    }
    
    /** Setzt diesen Ball als beendet und legt ihn still.
     */
    private void setFinished() {
        finish = true;
        this.setSize(50/SCALE_FACTOR);
        this.setvX(0);
        this.setvY(0);
        this.setaX(0);
        this.setaY(0);
    }
    
//    public void setvX(final double vX) {
//        System.out.println(this.getMaterial());
//        super.setvX(vX);
//    }
    
    /** Entfernt den uebergebenen Ball aus der Kollisionsliste.
     * @param that Der zu entfernende Ball.
     */
    protected void removeCollided(final GameModel that) {
        collided.remove(that);
    }
    
    /** Ueberprueft ob eine Kollision stattfindet.
     * @param that Das Objekt das ueberprueft wird.
     * @param elapsedTime Die vergangene Zeit seit dem letzten Aufruf.
     */
    public void checkCollision(final GameModel that, final double elapsedTime) {
        // Wenn das Spiel nicht beendet ist.
        // Wenn im letzten Frame keine Kollision stattgefunden hat.
        if (!isFinished()) {
                // Je nach Objekt welches ueber that uebergeben wird.
                String name = that.toString();
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
    private void collideBoxShapes(GameModel that,String name,double ngX,double ngY,double rot,double elapsedTime,double rotSpring,double d) {
            // Berechnung des Aufprallwinkels.
            System.out.print(ngX+" "+ngY);
            double alpha= Math.toDegrees(Math.atan(getvY()/getvX()));
            double beta= Math.toDegrees(Math.atan(ngY/ngX));
            double gamma = alpha-(2*beta);
//            if(gamma<0){
//                gamma=360+gamma;
//            }
//            if(gamma<180){
//            gamma=180+gamma;
//            }
            // Berechnung des Abprallwinkels.
            //System.out.println(rot+"rot");
            double delta= (180-getRotation()-gamma) % 360;
            //double delta= (180-rot-gamma) % 360;
//            if(delta<0){
//                delta=360+delta;
//            }
        
            // Wenn das Rechteck eine Feder ist.
            if(name.equals("Spring")&&that.getActive()){
                if(that.getOnFirstHit()){
                // Die Federkonstante.
                double D = 20;
                // Auslenkung aus der Ruhelage.
                s = that.getSize()/2;
                // Geschwindigkeit nach dem Treffer.
                springVel=Math.sqrt((D*s*s/getMass()));
                that.setOnFirstHit(false);
                }
                collideSpring(that,elapsedTime,rotSpring);
            // Normales Rechteck    
            }else if(isBoxCollision(ngX,ngY,d)){
                setRotation(delta);
                // Berechnung des Cosinus und Sinus des Abprallwinkels.
                cos= Math.cos(Math.toRadians(getRotation()));
                sin= Math.sin(Math.toRadians(getRotation()));
                // Berechnung des Geschwindigkeitsvektors.
                setvX(VectorCalculation.abs(getvX(),getvY())*cos);
                setvY(VectorCalculation.abs(getvX(),getvY())*sin);
                // Neuberechnung der Reibung.
                setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
                setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
                System.out.println("gamma: " + gamma + " delta: " + delta+ " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
                            
    }

    /** Ueberprueft ob eine Kollision stattfindet und berechnet die Beschleunigung im Luftstrom.
     * @param that Der Kugelfisch
     */
    private void collidePuffer(GameModel that) {
        // Die Eckpunkte des Rechtecks.
        double[] cornerPoints=that.getCornerPoints();
        // Die Abstaende zu den Seiten des Rechtecks.
        ArrayList<Double> distance=new ArrayList();
        // Fuer jede Seite.
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
            setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
        }
    }
    
    
    /** Bewegt die Kugel pro Zeitabschnitt weiter.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    public void move(final double elapsedTime) {
        if (!isFinished()) {
            // Die Position nach der Bewegung.
            double x = 0.5*getaX()*elapsedTime*elapsedTime+elapsedTime*getvX()+this.getPosX();
            double y = 0.5*getaY()*elapsedTime*elapsedTime+elapsedTime*getvY()+this.getPosY();

            // Stilllegen der Kugel sobald sie nicht mehr rollt.
            if(Math.round(x*1000)/1000.0==Math.round(this.getPosX()*1000)/1000.0
                    && Math.round(y*1000)/1000.0==Math.round(this.getPosY()*1000)/1000.0
                    && timeline != 0){
                setaX(0);
                setaY(0);
                setvX(0);
                setvY(0);
            }
            else{
            setPosX(x);
            setPosY(y);
            }
        }
    }
    
    /** Aufruf wenn ein Loch kollidiert werden soll.
     * Legt den Ball bei einer Kollision still.
     * @param that Das Loch mit dem die Kollision berechnet werden soll.
     */
    private void collideHole(final GameModel that) {
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - that.getPosX(), 2)
                        + Math.pow(this.getPosY() - that.getPosY(), 2));
        
        // Kugel soll in das Loch rutschen und es nicht nur beruehren.
        if (distance <= this.getSize() / 2 + that.getSize() / 2) {
            this.setPosX(that.getPosX());
            this.setPosY(that.getPosY());
            this.setFinished();
        }
    }
    
    /** Aufruf falls ein Barrel kollidiert werden soll.
     * @param that Das Barrel mit dem die Kollision berechnet werden soll.
     * @param time Vergangene Zeit seit dem letzten Aufruf.
     */
    private void collideBarrel(final GameModel that) {
//        double distance = Math.sqrt(
//                Math.pow(this.getPosX() - that.getPosX(), 2)
//                        + Math.pow(this.getPosY() - that.getPosY(), 2));
        
        // Wenn sich die Kreise beruehren (Distanz <= der Radien)
        // Sonst entfernen aus der Kollisionsliste
        if (isCircleCollision(that)) {
            // Berechnung des neuen Bewegungsvektors der Kugel (this)
//            collideCircle(this, that);

            System.out.println("Velo before: " + VectorCalculation.abs(this.getvX(), this.getvY()));
            // Normalenvektor zwischen den Kugeln
            double normX = that.getPosX() - this.getPosX();
            double normY = that.getPosY() - this.getPosY();
            System.err.println("arrriba.model.Ball.collideBarrel()");
            double[] result = splitBallVelocity(this, normX, normY);
            
            // Zusammenbauen der neuen Geschwindigkeit mit dem um gespiegelten Transfervektor
            double newVeloX = (-result[0]) + result[2];
            double newVeloY = (-result[1]) + result[3];
            
            // Setzen der neuen Geschwindigekeit
            this.setvX(newVeloX);
            this.setvY(newVeloY);
            
            System.err.println("Velo after: " + VectorCalculation.abs(this.getvX(), this.getvY()));
            
            // Setzen der neuen Rotation
            this.setRotation(Math.toDegrees(Math.atan2(newVeloY, newVeloX)));
            
            // Aktualisieren der Reibung
            this.setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
            this.setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
        }
//        else {
//            collided.remove(that);
//        }
    }
    
    /** Aufruf falls ein Ball kollidiert werden soll.
     * @param that Der Ball mit dem kollidiert werden soll.
     * @param time Vergangene Zeit seit dem letzten Aufruf.
     */
    private void collideBall(final GameModel that) {
        if (isCircleCollision(that)) {
            // Normalenvektor zwischen den Kugeln
            double normX = that.getPosX() - this.getPosX();
            double normY = that.getPosY() - this.getPosY();

            // Berechnen der Geschwindigkeitskomponenten
            double[] resultThis = splitBallVelocity(this, normX, normY);
            double[] resultThat = splitBallVelocity(that, -normX, -normY);

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
            
            // Berechnen der neuen Rotation
            double angleA = correctAngle(Math.atan2(newVeloAY, newVeloAX));
            double angleB = correctAngle(Math.atan2(newVeloBY, newVeloBX));

            // Setzen der neuen Rotation
            this.setRotation(Math.toDegrees(angleA));
            that.setRotation(Math.toDegrees(angleB));

            // Aktualisieren der Reibung
            this.setaX((-getvX() / VectorCalculation.abs(getvX(), getvY())) * getMaterial().getFrictionCoefficient());
            this.setaY((-getvY() / VectorCalculation.abs(getvX(), getvY())) * getMaterial().getFrictionCoefficient());
            that.setaX((-that.getvX() / VectorCalculation.abs(that.getvX(), that.getvY())) * that.getMaterial().getFrictionCoefficient());
            that.setaY((-that.getvY() / VectorCalculation.abs(that.getvX(), that.getvY())) * that.getMaterial().getFrictionCoefficient());
        }
    }

    /** Korrektur des Winkels auf einen Bereich von 0 .. 2*PI.
     * @param angle zu korrigierender Winkel
     * @return korrigierter Winkel
     */
    private double correctAngle(final double angle) {
        if (angle < 0) {
            return Math.abs(angle);
        } else {
            return (Math.PI * 2) - angle;
        }
    }
    
    /** Bestimmt ob die Kreise sich beruehren und aufeinander zu kommen.
     * @param that GameModel, welches ein Rundes Objekt darstellt (Shape = Circle).
     * @return True wenn sich die Objekte beruehren undaufeinander zu steuern.
     */
    private boolean isCircleCollision(final GameModel that) {
        double distance = Math.sqrt(
                Math.pow(this.getPosX() - that.getPosX(), 2)
                        + Math.pow(this.getPosY() - that.getPosY(), 2));
        
        // Wenn sich die Kreise beruehren (Distanz <= der Radien)
        // Sonst entfernen aus der Kollisionsliste
        double scalar = 1;
        if (distance <= this.getSize()/2 + that.getSize()/2) {
            // Berechnung der neuen Bewegungsvektoren der Baelle
            
            // Normalenvektor zwischen den Kugeln
            double normX = that.getPosX() - this.getPosX();
            double normY = that.getPosY() - this.getPosY();
            
            // Relative Geschwindigkeit von this zu that
            double vRelX = that.getvX() - this.getvX();
            double vRelY = that.getvY() - this.getvY();
            
            scalar = VectorCalculation.times(vRelX, vRelY, normX, normY);
        }
        // Wenn die Kugeln sich in einem spitzen Winkel annaehern (bis 90 Grad).
        return scalar <= 0;
    }
    
    /** Bestimmt ob die Kreise sich beruehren und aufeinander zu kommen.
     * @param that GameModel, welches ein Rundes Objekt darstellt (Shape = Circle).
     * @return True wenn sich die Objekte beruehren undaufeinander zu steuern.
     */
    private boolean isBoxCollision(double nX, double nY,double d) {
        //System.out.println("arrriba.model.Ball.isBoxCollision()");
        
        // Wenn sich die Kreise beruehren (Distanz <= der Radien)
        double scalar = -1;
            if(d<=getSize()/2){
            // Normalenvektor zwischen den Kugeln
            double normX = -nX;
            double normY = -nY;
            
            scalar = VectorCalculation.times(this.getvX(), this.getvY(), normX, normY);
                System.out.println(scalar+" scal!!!!");
            }
        // Wenn die Kugeln sich in einem spitzen Winkel annaehern (bis 90 Grad).
//        if(relation==1){
//        return scalar >= 0;
//        }else{
           return scalar >= 0; 
//        }
    }
    
    /** Berechnet die Komponenten des Geschwindigkeitsvektors von viewpoint.
     * @param viewpoint Kugel von der aus die Kollision betrachtet wird.
     * @param normX Normalenvektor zwischen den Kugeln (x).
     * @param normY Normalenvektor zwischen den Kugeln (y).
     * @return Transferkomponente an die andere Kugel, eigene Komponente (tX, tY, eX, eY).
     */
    private double[] splitBallVelocity(final GameModel viewpoint, final double normX, final double normY) {
        // Winkel zwischen x-Achse und Geschwindigkeitsvektor
        double phi = Math.atan2(viewpoint.getvY(), viewpoint.getvX());

        // Drehen des Normalenvektors
        double rotNormX = Math.cos(-phi) * normX - Math.sin(-phi) * normY;
        double rotNormY = Math.sin(-phi) * normX + Math.cos(-phi) * normY;

        // Winkel zwischen Normalenvektor und Geschwindigkeit
        // In einem Dreieck gibt es keinen negativen Winkel -> muss positiv sein
        double alpha = Math.abs(Math.atan2(rotNormY, rotNormX));
        
        // dritter Winkel des Dreiecks
        double gamma = Math.PI - (Math.PI / 2) - alpha;

        // Betrag der ersten uebertragenen Geschwindigkeit
        double transferVelocity = (
                VectorCalculation.abs(viewpoint.getvX(), viewpoint.getvY()) * Math.sin(gamma))
                / Math.sin(Math.PI / 2);
        double factorTransferVeloAtoB = transferVelocity / VectorCalculation.abs(normX, normY);

        // Komponente fuer die andere Kugel
        double transferVeloAtoBX = normX * factorTransferVeloAtoB;
        double transferVeloAtoBY = normY * factorTransferVeloAtoB;

        // Komponente fuer diese Kugel
        double ownVeloAX = viewpoint.getvX() - transferVeloAtoBX;
        double ownVeloAY = viewpoint.getvY() - transferVeloAtoBY;
        
        double[] returnValues = {transferVeloAtoBX, transferVeloAtoBY, ownVeloAX, ownVeloAY};
        
        return returnValues;
    }
    
//    /** Berechnet den aus der Kollision zweier Kreise resultierenden Geschwindigkeitsvektor.
//     * @param first Kreis mit Bewegung.
//     * @param second Kreis mit dem kollidiert wird.
//     */
//    private void collideCircle(final GameModel first, final GameModel second) {
//        if (!collided.contains(second)) {
//            System.out.println("Ball coll");
//            // Vektor zwischen den Mittelpunkten der Kreise -> Distanzvektor
//            double distanceX = second.getPosX() - first.getPosX();
//            double distanceY = second.getPosY() - first.getPosY();
//            
//            // Winkel zwischen Distanzvektor und x-Achse
//            double phi = Math.atan2(distanceY, distanceX);
//
//            // ############################################################################################ minus phi?
//            // Rotation
//            double rotVeloX = Math.cos(phi) * first.getvX() - Math.sin(phi) * first.getvY();
//            double rotVeloY = Math.sin(phi) * first.getvX() + Math.cos(phi) * first.getvY();
//
//            // an der y-Achse spiegeln
//            rotVeloX = -rotVeloX;
//
//            // Rueckrotation des Geschwindigkeitsvektors und anpassen des y-Vektors
//            double newVeloX = Math.cos(-phi) * rotVeloX - Math.sin(-phi) * rotVeloY;
//            double newVeloY = Math.sin(-phi) * rotVeloX + Math.cos(-phi) * rotVeloY;
//            newVeloX = -newVeloX;
//            
//            // Setzen des neuen Richtungswinkels
//            double newPhi = Math.atan2(newVeloY, newVeloX);
//            first.setRotation(Math.toDegrees(newPhi));
//
//            // Setzen des neuen Geschwindkeikeitsvektors
//            setvX(VectorCalculation.abs(getvX(), getvY()) * Math.cos(newPhi));
//            setvY(VectorCalculation.abs(getvX(), getvY()) * Math.sin(newPhi));
//            
//            // Aktualisieren der Reibung
//            setaY((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
//            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
//            
//            // Als bearbeitet listen
////            collided.add(second);
//        }
//    }
    
    /** Prueft ob eine Kollision mit einem Rechteck vorliegt.
     * @param that Das Rechteck das ueberprueft wird.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    private void checkCollideBoxShapes(final GameModel that, double elapsedTime) {
        // Wenn das Rechteck ein Kugelfisch ist.
        if(that.toString().equals("Puffer")){
            collidePuffer(that);
        }else{
            // Die Eckpunkte des Rechtecks.
            double[] cornerPoints=that.getCornerPoints();
            // Fuer jede Seite des Rechtecks.
            for(int c=0;c<=cornerPoints.length-3;c=c+2){
                // Geraden.
                double a=cornerPoints[c]-cornerPoints[c+2];
                double b=cornerPoints[c+1]-cornerPoints[c+3];
                // Der Normalenvektor.
                double ngX=(-b);
                double ngY=a;
                // Abstandsberchnung.
                double e= VectorCalculation.times(ngX, ngY, getPosX()-cornerPoints[c], getPosY()-cornerPoints[c+1]);
                double d= Math.abs(e)/VectorCalculation.abs(ngX, ngY);
                // Wenn der Ball keine Ecke trifft.
                if(!checkCollisionCorner(cornerPoints, c, elapsedTime,that)){
                    // Wenn der Ball mit der Seite kollidiert.
                    if(d<=getSize()/2&&getPosX()>cornerPoints[c]&&getPosX()<cornerPoints[c+2]){
                        System.out.println(d+"distanceA");
                        collideBoxShapes(that,that.toString(),ngX,ngY,0+(that.getRotation()%90),elapsedTime,270+(that.getRotation()%90),d);
                    }else if(d<=getSize()/2&&getPosY()>cornerPoints[c+1]&&getPosY()<cornerPoints[c+3]){
                        System.out.println(d+"distanceB");
                        collideBoxShapes(that,that.toString(),ngX,ngY,180+(that.getRotation()%90),elapsedTime,0+(that.getRotation()%90),d);                 
                    }else if(d<=getSize()/2&&getPosX()<cornerPoints[c]&&getPosX()>cornerPoints[c+2]){
                        System.out.println(d+"distanceC");
                        collideBoxShapes(that,that.toString(),ngX,ngY,180+(that.getRotation()%90),elapsedTime,90+(that.getRotation()%90),d);
                    }else if(d<=getSize()/2&&getPosY()<cornerPoints[c+1]&&getPosY()>cornerPoints[c+3]){
                        System.out.println(d+"distanceD");
                        collideBoxShapes(that,that.toString(),ngX,ngY,0+(that.getRotation()%90),elapsedTime,180+(that.getRotation()%90),d);
                    }
                }
            }
        }
    }

    /** Ueberprueft ob der Ball eine Ecke des Rechtecks trifft und berechnet danach den Abprallwinkel.
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
            // Berechnung des Cosinus und Sinus des Abprallwinkels.
            cos= Math.cos(Math.toRadians(getRotation()));
            sin= Math.sin(Math.toRadians(getRotation()));
            // Berechnung des Geschwindigkeitsvektors.
            setvX(VectorCalculation.abs(getvX(),getvY())*cos);
            setvY(VectorCalculation.abs(getvX(),getvY())*sin);
            // Neuberechnung der Reibung.
            setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
            return true;
        }
        return false;
    }
    
    /** Berechnung des Abprallwinkels bei einer Kollision mit einer Feder.
     * @param that Die Feder.
     * @param elapsedTime Die vergangene Zeit nach dem letzten Aufruf.
     * @param rot Die Rotation des Normalenvektors der Feder.
     */
    private void collideSpring(GameModel that, double elapsedTime,double rot) {
        if(that.getActive()){
            // Cosinus und Sinus des Normalenvektors der Feder.
            double cosSpring= Math.cos(Math.toRadians(rot));
            double sinSpring= Math.sin(Math.toRadians(rot));
            // Berechnung des Geschwindigkeitsvektors.
            setvX((VectorCalculation.abs(getvX(), getvY())+springVel)*cosSpring);
            setvY((VectorCalculation.abs(getvX(), getvY())+springVel)*sinSpring);
            // Neuberechnung der Reibung.
            setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
            setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
            // Berechnung der Strecke im naechsten Schritt.
            double vX=getvX()+getaX()*elapsedTime;
            double vY=getvY()+getaY()*elapsedTime;
            double x = 0.5*getaX()*elapsedTime*elapsedTime+elapsedTime*vX;
            double y = 0.5*getaY()*elapsedTime*elapsedTime+elapsedTime*vY;
            // Entspannen der Feder.
            that.setPosX(that.getPosX()+that.getSize()/2);
            that.setPosY(that.getPosY()+that.getSize()/2);
            that.setSize(that.getSize()+VectorCalculation.abs(x,y)*2);
            that.setPosX(that.getPosX()-that.getSize()/2);
            that.setPosY(that.getPosY()-that.getSize()/2);
            // Wenn der entspannte Status erreicht ist.
            if(that.getSize()>=4*s){
                that.setActive(false);
            }
        }
    }

    /** Ueberprueft ob der Ball mit den Banden kollidiert.
     * @param config Die Bande.
     * @param elapsedTime Vergangene Zeit seit dem letzten Aufruf.
     */
    public void checkCollisionBoundary(Config config , double elapsedTime) {
        // Die obere Haelfte der Bande.
        double[][] upperBoundaries=config.getUpperBoundary();
        // Die untere Haelfte der Bande.
        double[][] lowerBoundaries=config.getLowerBoundary();
        
        // Fuer die untere und obere Haelfte.
        for(int k=0;k<2;k++){
            double[][] activeDouble;
            if(k==0){
                activeDouble=upperBoundaries;
            }else{
                activeDouble=lowerBoundaries;
            }
            
            // Fuer jede Gerade
            for(int c=0;c<activeDouble.length-1;c++){
                double a;
                double b;
                // Geraden aufstellen
                if(k==0){
                    a=activeDouble[c+1][0]-activeDouble[c][0];
                    b=activeDouble[c+1][1]-activeDouble[c][1];
                }else{
                    a=activeDouble[c][0]-activeDouble[c+1][0];
                    b=activeDouble[c][1]-activeDouble[c+1][1];
                }
//                System.out.println(a+" ab " + b);
                // Normale
                double nX=(-b);
                double nY=(a);
                nX=(nX/VectorCalculation.abs(nX, nY));
                nY=(nY/VectorCalculation.abs(nX, nY));
                //System.out.println(nX+" nx "+nY);
                // Abstandsberechnung.
                double e= VectorCalculation.times(nX, nY, getPosX()-activeDouble[c][0], getPosY()-activeDouble[c][1]);
                double d= Math.abs(e)/VectorCalculation.abs(nX, nY);
                //System.out.println(k+" "+d+" hier "+ c);
                if(isBoxCollision(nX,nY,d)){
                    //System.out.println(nX+" nx "+nY+" ny");
                    // Wenn die obere Haelfte ueberprueft wird.
                    if(k==0){
                        // Wenn der Ball mit der Bande kollidiert.
                        if(d<=getSize()/2 && getPosX()<upperBoundaries[c+1][0] && getPosX()>upperBoundaries[c][0]){
                            collisionBoundary(
                                    activeDouble[c][0],activeDouble[c][1],
                                    activeDouble[c+1][0],activeDouble[c+1][1]);
                        }
                    // Wenn die untere Haelfte ueberprueft wird.    
                    }else{
                        // Wenn der Ball mit der Bande kollidiert.
                        if(d<=getSize()/2 && getPosX()<lowerBoundaries[c+1][0] && getPosX()>lowerBoundaries[c][0]){
                            collisionBoundary(
                                    activeDouble[c+1][0],activeDouble[c+1][1],
                                    activeDouble[c][0],activeDouble[c][1]);
                        }
                    }
            }
        }

        // Fuer die linke und rechte Seite der Bande.
        for(int i = 0;i<7;i=i+6){
            // Geraden aufstellen.

            double a=lowerBoundaries[i][0]-upperBoundaries[i][0];
               double b=lowerBoundaries[i][1]-upperBoundaries[i][1];
            if(i==0){
                 a=upperBoundaries[i][0]-lowerBoundaries[i][0];
                 b=upperBoundaries[i][1]-lowerBoundaries[i][1];
            }
//            System.out.println(a+" ab " + b);
            // Normale.
            double nX=(-b);
            double nY=(a);
            nX=nX/VectorCalculation.abs(nX, nY);
            nY=nY/VectorCalculation.abs(nX, nY);
            // Abstandsberchnung
            double e= VectorCalculation.times(nX, nY, getPosX()-upperBoundaries[i][0], getPosY()-upperBoundaries[i][1]);
            double d= Math.abs(e)/VectorCalculation.abs(nX, nY);
            double rot;
            if(i==0){
                rot=180;
                if(isBoxCollision(nX,nY,d)){ 
                // Wenn der Ballmittelpunkt mit der Bande kollidiert.
                if(d<=getSize()/2 && getPosY()<lowerBoundaries[i][1] && getPosY()>upperBoundaries[i][1]){
                    collisionBoundary(lowerBoundaries[i][0],lowerBoundaries[i][1],upperBoundaries[i][0],upperBoundaries[i][1]); 
                }
            }
            }else{
                rot=0;
                if(isBoxCollision(nX,nY,d)){ 
                // Wenn der Ballmittelpunkt mit der Bande kollidiert.
                if(d<=getSize()/2 && getPosY()<lowerBoundaries[i][1] && getPosY()>upperBoundaries[i][1]){
                    collisionBoundary(upperBoundaries[i][0],upperBoundaries[i][1],lowerBoundaries[i][0],lowerBoundaries[i][1]); 
                }
            }
            }
            }
        }
    }

    /** Berechnet den Abprallwinkel des Balls nach Kollision mit der Bande.
     * @param d Der Abstand des Balls zum Eckpunkt.
     * @param nY Der Y_Wert der Normalen. 
     * @param nX Der X-Wert der Normalen.
     */
    private void collisionBoundary(final double firstX, final double firstY, final double secX, final double secY) {
        //System.out.println(d);
        // Berechnung des Aufprallwinkels.
        //double alpha= Math.toDegrees(Math.atan(getvY()/getvX()));
        System.err.println("#############################################################################################");
        System.out.println("Kollision bei x: " + this.getPosX() + ", y: " + this.getPosY());
        System.err.println("1x: " + firstX + ", 1y: " + firstY + ", 2x: " + secX + ", 2y: " + secY);
        //double beta= Math.toDegrees(Math.atan2(nY,nX));
        //double gamma = alpha-(2*beta);
            
        // Vektor zwischen zwei Eckpunkten
        double boundX=secX-firstX;
        double boundY=secY-firstY;
//        System.out.println(boundX+" bo " +boundY);

        // Winkel zwischen x-Achse und Wand
        double phi=Math.abs(Math.atan2(boundY, boundX));

         // Rotation des Geschwindigkeitsvektors um diesen Winkel
        double rotVeloX = Math.cos(-phi) * getvX() - Math.sin(-phi) * getvY();
        double rotVeloY = Math.sin(-phi) * getvX() + Math.cos(-phi) * getvY();

        // Spiegeln der Geschwindigkeit an der x-Achse
        rotVeloY=-rotVeloY;

        // Rueckrotation der Geschwindigkeit
        double newRotVelX = Math.cos(phi) * rotVeloX - Math.sin(phi) * rotVeloY;
        double newRotVelY = Math.sin(phi) * rotVeloX + Math.cos(phi) * rotVeloY;
            
//            double alpha=Math.toDegrees(Math.atan2(rotVeloY, rotVeloX));
//            // Berechnung des Abprallwinkels.
//            System.out.println(rot+"rot");
//            double delta;
//            if(alpha<0){
//                delta= (180+Math.abs(rot)-alpha) % 360;
//            }else{
//                delta= 360+Math.abs(rot)-alpha;
//            }
//        if(delta<0){
//                delta=360+delta;
//            }
//        System.out.println("alpha "+ alpha  + " delta: " + delta+ " !!!!!!!!!!!!!!!!!!!!!!#!!!!!!!!!!!!!!");
//        // Setzen des Rotationswinkels.
//        setRotation(delta);
//        // Berechnung des Cosinus und Sinus des Abprallwinkels.
//        cos= Math.cos(Math.toRadians(getRotation()));
//        sin= Math.sin(Math.toRadians(getRotation()));
        // Berechnung des Geschwindigkeitsvektors.
        
        // Setzen der neuen Geschwindigkeit
        setvX(newRotVelX);
        setvY(newRotVelY);
        
        // Neuberechnung der Reibung.
        setaX((-getvX()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
        setaY((-getvY()/VectorCalculation.abs(getvX(), getvY()))*getMaterial().getFrictionCoefficient());
    }
}
