package main.wallet.wallet_graphics.initial_cards;

import main.wallet.Wallet;
import main.wallet.wallet_graphics.TextFieldUtils;
import main.wallet.wallet_graphics.main_card.MainCard;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

import static main.wallet.Wallet.readFromFile;
import static main.wallet.Wallet.readFromUserPassFile;

/**
 * compares the provided username and password by the user with the users file entries
 * if valid, retrieve the main.record file of that user and passes it to be shown in the
 * main card which has the tables of records and operation panels
 */
public class SignInCard extends JPanel {
    static final String ERROR = "ERROR";
    //creating Card which is the card in which user enters his/her username pass
    public SignInCard(InitialCards initialFrame, JPanel cardPanel, Logger logger){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //username label and textField
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameTextField = new JTextField(20);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);

        //password label and textField
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:  ");
        JPasswordField passwordTextField = new JPasswordField(20);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordTextField);

        //back and enter button
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("BACK");
        backButton.addActionListener(e -> {
            TextFieldUtils.clearTextFields(usernameTextField, passwordTextField);
            CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
            cardLayout.show(cardPanel, "welcomeCard");
        });
        JButton enterAccountButton = new JButton("Enter");
        enterAccountButton.addActionListener(e -> {
            String username = usernameTextField.getText();

            char[] charPassword = passwordTextField.getPassword();
            String password = new String(charPassword);
            String hashedPassword = hashPassword(password, logger);

            Map<String, String> userPassMap = readFromUserPassFile(logger);

            if (username.isBlank() || password.isBlank()) {
                JOptionPane.showMessageDialog(initialFrame,
                        "Please fill all the fields!", ERROR,
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Map.Entry<String, String> userPass : userPassMap.entrySet()) {
                if (userPass.getKey().equals(username) &&
                        userPass.getValue().equals(hashedPassword)) {
                    Wallet wallet = readFromFile(username, logger);
                    MainCard mainCard = new MainCard();
                    initialFrame.setVisible(false);
                    mainCard.init(wallet, initialFrame, logger);
                    usernameTextField.setText("");
                    passwordTextField.setText("");
                    }
                }

            if (!userPassMap.containsKey(username)) {
                JOptionPane.showMessageDialog(initialFrame,
                        "Username not found!", ERROR,
                        JOptionPane.ERROR_MESSAGE);
            }else if(!userPassMap.get(username).equals(hashedPassword)) {
                JOptionPane.showMessageDialog(initialFrame,
                        "Incorrect password!", ERROR,
                        JOptionPane.ERROR_MESSAGE);
            }
            java.util.Arrays.fill(charPassword, '0');}
        );

        buttons.add(backButton);
        buttons.add(enterAccountButton);
        add(usernamePanel);
        add(passwordPanel);
        add(buttons);
    }
    private static String hashPassword(String password, Logger logger) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encodedHash);

        } catch (NoSuchAlgorithmException e) {
            logger.severe("ERROR : algorithm for hashing was not found!");
            return null; // Handle the exception appropriately in a real application
        }
    }
}
