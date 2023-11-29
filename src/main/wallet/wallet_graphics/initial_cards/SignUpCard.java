package main.wallet.wallet_graphics.initial_cards;

import main.wallet.Wallet;
import main.wallet.wallet_graphics.TextFieldUtils;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

import static main.wallet.Wallet.addToUserPassFile;
import static main.wallet.Wallet.readFromUserPassFile;

/**
 * a new user is created and added to the users file for future authentication and access
 */
public class SignUpCard extends JPanel {
    static final String ERROR = "ERROR";
    public SignUpCard(InitialCards frame, JPanel cardPanel, Logger logger){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //name panel and text field
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Name:       ");
        JTextField nameTextField = new JTextField(20);
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);

        //username label and text field
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameTextField = new JTextField(20);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameTextField);

        //password label and text field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:  ");
        JPasswordField passwordTextField = new JPasswordField(20);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordTextField);

        //confirm password label and text field
        JPanel confirmPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel confirmPasswordLabel = new JLabel("Password:  ");
        JPasswordField confirmPasswordTextField = new JPasswordField(20);
        confirmPasswordPanel.add(confirmPasswordLabel);
        confirmPasswordPanel.add(confirmPasswordTextField);

        //back and create buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("BACK");
        backButton.addActionListener(e -> {
            TextFieldUtils.clearTextFields(nameTextField,
                    usernameTextField,passwordTextField,confirmPasswordTextField);
            CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
            cardLayout.show(cardPanel, "welcomeCard");
        });
        JButton createButton = new JButton("CREATE ACCOUNT");
        createButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String username = usernameTextField.getText();

            char[] charPassword = passwordTextField.getPassword();
            String password = new String(charPassword);
            char[] charConfirmPassword = confirmPasswordTextField.getPassword();
            String confirmPassword = new String(charConfirmPassword);

            Map<String, String> userPassMap = readFromUserPassFile(logger);

            if (name.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                JOptionPane.showMessageDialog(frame,
                        "Please fill all the fields!", ERROR,
                        JOptionPane.ERROR_MESSAGE);
            } else {
                if (!userPassMap.containsKey(username)) {
                    if (password.equals(confirmPassword)) {
                        String hashedPassword = hashPassword(password, logger);
                        Wallet newWallet = new Wallet(name, username, hashedPassword);
                        addToUserPassFile(username, hashedPassword, logger);
                        newWallet.storeOnFile(logger);
                        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                        cardLayout.show(cardPanel, "welcomeCard");
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Passwords does not match!", ERROR,
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Username already taken!", ERROR,
                            JOptionPane.ERROR_MESSAGE);
                }
                TextFieldUtils.clearTextFields(nameTextField,
                        usernameTextField,passwordTextField,confirmPasswordTextField);
            }
            java.util.Arrays.fill(charPassword, '0');
            java.util.Arrays.fill(charConfirmPassword, '0');
        });
        buttons.add(backButton);
        buttons.add(createButton);

        //finalizing the Card
        add(namePanel);
        add(usernamePanel);
        add(passwordPanel);
        add(confirmPasswordPanel);
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
