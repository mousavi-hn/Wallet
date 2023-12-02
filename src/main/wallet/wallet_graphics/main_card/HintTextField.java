package main.wallet.wallet_graphics.main_card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HintTextField extends JTextField {
    public final String hintText;

    public HintTextField(String hintText) {
        this.hintText = hintText;
        setForeground(Color.GRAY);

        // Add a focus listener to handle the hint text
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().isEmpty() || getText().equals(hintText)) {
                    setForeground(Color.BLACK);
                    setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty() || getText().equals(hintText)) {
                    setForeground(Color.GRAY);
                    setText(hintText);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Paint the hint text only if the field is empty and doesn't have focus
        if (getText().isEmpty() && !hasFocus()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(getForeground());
            g2.drawString(hintText, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
        }
    }
}