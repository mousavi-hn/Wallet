/**
 * initial frames which allows the user to create a new account
 * or enter an existing account
 */
package main.wallet.wallet_graphics.initial_cards;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class InitialCards extends JFrame {
    private static final Logger logger = Logger.getLogger(InitialCards.class.getName());

    public void init(){

        setTitle("Wallet");
        JPanel cardPanel = new JPanel(new CardLayout());

        WelcomeCard welcomeCard = new WelcomeCard(cardPanel);

        SignInCard signInCard = new SignInCard(this, cardPanel, logger);

        SignUpCard signUpCard = new SignUpCard(this, cardPanel, logger);

        cardPanel.add(welcomeCard, "welcomeCard");
        cardPanel.add(signInCard, "signInCard");
        cardPanel.add(signUpCard, "signUpCard");

        add(cardPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
