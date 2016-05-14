/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.control;

import arrriba.model.Ball;
import arrriba.model.Obstacle;
import arrriba.Arrriba;
import java.util.ArrayList;

/**
 *
 * @author fabian
 */
public class Control {
    private Arrriba view;
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    
    public Control(final Arrriba view) {
        this.view = view;
        init();
    }
    
    private void init() {
        obstacles.add(new Ball());
        
        for (Obstacle o: obstacles) {
//            o.addObserver(view);
        }
    }
}
