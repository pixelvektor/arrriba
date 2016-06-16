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

/** NumberTextField erweitert TextField um die Funktion, dass nur Nummern eingegeben werden koennen.
 */
public class NumberTextField extends TextField {
    /** Wert des Textfelds als Double. */
    private double value;
    
    /** NumberTextField.
     * Kann nur Zahlen als Double verarbeiten.
     * Der Konstruktor wurde, ausgenommen des Patterns,
     * http://stackoverflow.com/questions/31458198/how-to-restrict-textfield-so-that-it-can-contain-only-one-character-javafx
     * (Stand 22.05.2016) entnommen.
     */
    public NumberTextField() {
        // Checkpattern fuer Doublewerte, andere Eingaben werden ignoriert
        Pattern doublePattern = Pattern.compile(
                "((([0-9])\\d*\\.([0-9])\\d*)|(([0-9])\\d*\\.)|(([0-9])\\d*))");
        
        // Formatiert den Text im Feld basierend auf dem Pattern und konvertiert einen String in Doublewerte
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
    }
    
    /** Gibt den Wert des Feldes zurueck.
     * @return Wert des Feldes.
     */
    public double getValue() {
        return value;
    }
    
    /** Setzt den Wert des Feldes.
     * @param value Neuer Wert des Feldes.
     */
    public void setValue(final double value) {
        this.value = value;
        String stringValue = "" + value;
        this.setText(stringValue);
    }
}
