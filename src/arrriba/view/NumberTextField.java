/*
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.view;

import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

/**
 *
 * @author fabian
 */
public class NumberTextField extends TextField {
    private double value;
    
    public NumberTextField() {
        Pattern doublePattern = Pattern.compile(
                "((([0-9])\\d*\\.([0-9])\\d*)|(([0-9])\\d*\\.)|(([0-9])\\d*))");
        
        TextFormatter<Double> textFormatter = new TextFormatter<Double>(
                new DoubleStringConverter(), 0.0,
                change -> {
                    String newText = change.getControlNewText();
                    if (doublePattern.matcher(newText).matches()) {
                        value = Double.parseDouble(newText);
                        return change;
                    } else
                        return null;
                });
        
        this.setTextFormatter(textFormatter);
        
        textFormatter.valueProperty().addListener((obs, oldValue, newValue) -> {
            System.out.println("new double vlaue: " + newValue);
        });
    }
    
    public double getValue() {
        return value;
    }
    
    public void setValue(final double value) {
        this.value = value;
        String stringValue = "" + value;
        this.setText(stringValue);
    }
}
