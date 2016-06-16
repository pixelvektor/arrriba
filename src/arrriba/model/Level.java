/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arrriba.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    
    /** Alle Gegenstaende auf dem Spielfeld. */
    private final ArrayList<GameModel> obstacles = new ArrayList<>();
    /** x-Position des Lochs. */
    private double holeX;
    /** y-Position des Lochs. */
    private double holeY;
    /* ArrayList mit x- und y-Positionen der Kugeln beim Start. */
    ArrayList<Double> startPos = new ArrayList<>();
    
    /** Laden des ersten Levels (Oberdeck).
     */
    public void loadOberdeck() {
        obstacles.clear();
        startPos.clear();
        Box box = new Box(300, 550, 100);        
        obstacles.add(box);
        Box box2 = new Box(500, 250, 100);        
        obstacles.add(box2);
        Box box3 = new Box(150, 700, 80);        
        obstacles.add(box3);
        Barrel barrel = new Barrel(900, 450, 100);
        obstacles.add(barrel);
        startPos.add(200.0);
        startPos.add(200.0);
        startPos.add(100.0);
        startPos.add(580.0);
        startPos.add(800.0);
        startPos.add(300.0);
        holeX=450;
        holeY=750;
    }
    
    /** Laden des zweiten Levels (Zwischendeck).
     */
    public void loadZwischendeck() {
        obstacles.clear();
        startPos.clear();
        Box box = new Box(250, 720, 100);        
        obstacles.add(box);
        Barrel barrel = new Barrel(900, 500, 120);
        obstacles.add(barrel);
        Barrel barrel2 = new Barrel(300, 280, 60);
        obstacles.add(barrel2);
        Spring spring = new Spring(250, 470, 100);
        obstacles.add(spring);
        startPos.add(450.0);
        startPos.add(750.0);
        startPos.add(1000.0);
        startPos.add(700.0);
        startPos.add(600.0);
        startPos.add(200.0);
        holeX=150;
        holeY=750;
    }
    
    /** Laden des dritten Levels (Unterdeck).
     */
    public void loadUnterdeck() {
        obstacles.clear();
        startPos.clear();
        Box box = new Box(100,350, 100);        
        obstacles.add(box); // jedes neue objekt da adden
        Box box2 = new Box(250, 350, 100);        
        obstacles.add(box2);
        Box box3 = new Box(400, 350, 100);        
        obstacles.add(box3); 
        Box box4 = new Box(550, 350, 100);        
        obstacles.add(box4); 
        Barrel barrel = new Barrel(900, 600, 70);
        obstacles.add(barrel);
        Puffer puffer = new Puffer(100, 500, 100);
        obstacles.add(puffer);
        startPos.add(150.0);
        startPos.add(750.0);
        startPos.add(750.0);
        startPos.add(400.0);
        startPos.add(1100.0);
        startPos.add(550.0);
        holeX=150; // Loch
        holeY=200;
    }
    
    /** Gibt die Obstacles des Levels zurueck.
     * @return Obstacles des aktuellen Levels
     */
    public ArrayList getObstacles(){
        return obstacles;
    }
    
    /** Gibt die Startpositionen der Kugeln zurueck.
     * @return x-Startposition der ersten Kugel
     */
    public ArrayList<Double> getStartPos(){
        return startPos;
    }
    
    /** Gibt die x-Position des Lochs zurueck.
     * @return x-Position des Lochs
     */
    public double getHoleX(){
        return holeX;
    }
    
    /** Gibt die y-Position des Lochs zurueck.
     * @return y-Position des Lochs
     */
    public double getHoleY(){
        return holeY;
    }
}
