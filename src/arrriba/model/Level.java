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
        Box box = new Box(300, 550, 100);        
        obstacles.add(box);
        Box box2 = new Box(500, 250, 100);        
        obstacles.add(box2);
        Box box3 = new Box(150, 700, 80);        
        obstacles.add(box3);
        Barrel barrel = new Barrel(900, 450, 100);
        obstacles.add(barrel);
        Barrel barrel2 = new Barrel(800, 450, 50);
        obstacles.add(barrel2);
        Barrel barrel3 = new Barrel(1000, 450, 50);
        obstacles.add(barrel3);
        Barrel barrel4 = new Barrel(900, 350, 50);
        obstacles.add(barrel4);
        Barrel barrel5 = new Barrel(900, 550, 50);
        obstacles.add(barrel5);
        startPosX=100;
        startPosY=200;
        holeX=450;
        holeY=750;
    }
    
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
        holeY=150;
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
