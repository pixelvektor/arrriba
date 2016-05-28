/**
 * Hochschule Hamm-Lippstadt
 * Praktikum Visual Computing II (ARRRiba!)
 * (C) 2016 Kevin Otte, Lara Sievers, Adrian Schmidt, Fabian Schneider
 */
package arrriba.view;

/**
 *
 * @author fabian
 */
public class DegreeTextField extends NumberTextField {

    @Override
    public void replaceText(final int start, final int end, final String text) {
        if (text.matches("[0-9]") || text.isEmpty()) {
            super.replaceText(start, end, text);
            
            String newText = this.getText();
            if (!newText.isEmpty()) {
                int degree = Integer.parseInt(newText) % 360;
                super.replaceText(0, this.getText().length(),
                        Integer.toString(degree));
            }
            
        }
    }
    
}
