/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arrriba.model;

import java.util.ArrayList;

public class Level {
    
    /** Alle Gegenstaende auf dem Spielfeld. */
    private final ArrayList<GameModel> obstacles = new ArrayList<>();
    /** x-Startposition der ersten Kugel. */
    private double startPosX;
    /** y-Startposition der ersten Kugel. */
    private double startPosY;
    /** x-Position des Lochs. */
    private double holeX;
    /** y-Position des Lochs. */
    private double holeY;
    
    /** Laden des ersten Levels (Oberdeck).
     */
    public void loadOberdeck() {
        obstacles.clear();
        Box box = new Box(300, 550, 100);        
        obstacles.add(box);
        Box box2 = new Box(500, 250, 100);        
        obstacles.add(box2);
        Box box3 = new Box(150, 700, 80);        
        obstacles.add(box3);
        Barrel barrel = new Barrel(900, 450, 100);
        obstacles.add(barrel);
        startPosX=100;
        startPosY=200;
        holeX=450;
        holeY=750;
    }
    
    /** Laden des zweiten Levels (Zwischendeck).
     */
    public void loadZwischendeck() {
        obstacles.clear();
        Box box = new Box(250, 720, 100);        
        obstacles.add(box);
        Barrel barrel = new Barrel(900, 500, 120);
        obstacles.add(barrel);
        Barrel barrel2 = new Barrel(300, 280, 60);
        obstacles.add(barrel2);
        Spring spring = new Spring(250, 470, 100);
        obstacles.add(spring);
        startPosX=450;
        startPosY=750;
        holeX=150;
        holeY=750;
    }
    
    /** Laden des dritten Levels (Unterdeck).
     */
    public void loadUnterdeck() {
        obstacles.clear();
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
        startPosX=150; // für die Kugel
        startPosY=750;
        holeX=150; // Loch
        holeY=200;
    }
    
    /** Gibt die Obstacles des Levels zurueck.
     * @return Obstacles des aktuellen Levels
     */
    public ArrayList getObstacles(){
        return obstacles;
    }
    
    /** Gibt die x-Startposition der ersten Kugel zurueck.
     * @return x-Startposition der ersten Kugel
     */
    public double getStartPosX(){
        return startPosX;
    }
    
    /** Gibt die y-Startposition der ersten Kugel zurueck.
     * @return y-Startposition der ersten Kugel
     */
    public double getStartPosY(){
        return startPosY;
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
