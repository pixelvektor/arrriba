/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arrriba.model;

import java.util.ArrayList;

/**
 *
 * @author Adrian
 */
public class Level {
    
    /** Alle Gegenstaende auf dem Spielfeld. */
    private final ArrayList<GameModel> obstacles = new ArrayList<>();
    private double startPosX;
    private double startPosY;
    private double holeX;
    private double holeY;
    
    public void loadOberdeck() {
        obstacles.clear();
        Box box = new Box(400, 670, 80);        
        obstacles.add(box);
        Barrel barrel = new Barrel(125, 165, 50);
        obstacles.add(barrel);
        startPosX=700;
        startPosY=800;
        holeX=300;
        holeY=400;
    }
    
    public void loadZwischendeck() {
        obstacles.clear();
        Box box = new Box(800, 370, 50);        
        obstacles.add(box);
        Barrel barrel = new Barrel(200, 400, 90);
        obstacles.add(barrel);
        startPosX=700;
        startPosY=800;
        holeX=500;
        holeY=800;
    }
    
    public void loadUnterdeck() {
        obstacles.clear();
        Box box = new Box(500, 730, 100);        
        obstacles.add(box);
        Barrel barrel = new Barrel(625, 500, 70);
        obstacles.add(barrel);
        startPosX=700;
        startPosY=800;
        holeX=100;
        holeY=400;
    }
    
    public ArrayList getObstacles(){
        return obstacles;
    }
    
    public double getStartPosX(){
        return startPosX;
    }
    
    public double getStartPosY(){
        return startPosY;
    }
    
    public double getHoleX(){
        return holeX;
    }
    
    public double getHoleY(){
        return holeY;
    }
    
}
