package wallet.wallet_graphics.initial_cards;

import javax.swing.*;
import java.awt.*;

public class WelcomeCard extends JPanel {
    public WelcomeCard(JPanel nextCard){
        setLayout(new GridBagLayout());

        JButton signInButton = new JButton("SIGN-IN");
        setActionListener(signInButton, nextCard, "signInCard");

        JButton signUpButton = new JButton("SIGN-UP");
        setActionListener(signUpButton, nextCard, "signUpCard");

        add(signInButton);
        add(signUpButton);
    }

    private void setActionListener(JButton button,JPanel nextCard, String constraint){
        button.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) nextCard.getLayout();
            cardLayout.show(nextCard, constraint);
        });
    }
}
