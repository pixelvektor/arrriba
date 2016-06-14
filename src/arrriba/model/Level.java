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
        Box box = new Box(400, 670, 80);        
        obstacles.add(box);
        Barrel barrel = new Barrel(125, 165, 50);
        obstacles.add(barrel);
        startPosX=600;
        startPosY=700;
        holeX=300;
        holeY=400;
    }
    
    /** Laden des zweiten Levels (Zwischendeck).
     */
    public void loadZwischendeck() {
        obstacles.clear();
        Box box = new Box(800, 370, 50);        
        obstacles.add(box);
        Barrel barrel = new Barrel(200, 400, 90);
        obstacles.add(barrel);
        startPosX=600;
        startPosY=700;
        holeX=500;
        holeY=800;
    }
    
    /** Laden des dritten Levels (Unterdeck).
     */
    public void loadUnterdeck() {
        obstacles.clear();
        Box box = new Box(500, 730, 100);        
        obstacles.add(box);
        Barrel barrel = new Barrel(625, 500, 70);
        obstacles.add(barrel);
        startPosX=600;
        startPosY=700;
        holeX=100;
        holeY=400;
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
