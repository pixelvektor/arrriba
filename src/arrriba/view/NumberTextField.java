/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.view;

import javafx.scene.control.TextField;

/**
 *
 * @author fabian
 */
public class NumberTextField extends TextField {
    private double value;
    
    public double getValue() {
        return value;
    }
    
    public void setValue(final double value) {
//        double roundedValue = (double) Math.round(value * 10) / 10;
        this.value = value;
        String stringValue = "" + value;
        this.setText(stringValue);
    }
    
    @Override
    public void replaceText(final int start, final int end, final String text) {
        if (text.matches("[0-9.]") || text.isEmpty()) {
            super.replaceText(start, end, text);
            this.value = Double.parseDouble(super.getText());
        }
    }
}
