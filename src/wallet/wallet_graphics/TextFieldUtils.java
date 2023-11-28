package wallet.wallet_graphics;

import javax.swing.*;

public class TextFieldUtils {
    public static void clearTextFields(JTextField... textFields) {
        for (JTextField textField : textFields) {
            textField.setText("");
        }
    }

    public static void clearFormattedFields(JFormattedTextField... formattedFields){
        for (JFormattedTextField formattedField : formattedFields) {
            formattedField.setValue(null);
        }
    }
}
