package wallet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import static wallet.Wallet.addToUserPassFile;
import static wallet.Wallet.readFromUserPassFile;

public class WalletGUI {
    private static String hashPassword(String password) {
        try {
            // Use SHA-256 for hashing (you can choose a different algorithm based on your requirements)
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a base64-encoded string for comparison
            return Base64.getEncoder().encodeToString(encodedHash);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR : algorithm for hashing was not found!");
            return null; // Handle the exception appropriately in a real application
        }
    }
    public void init(){
        //cardLayout is implemented, one frame with different cards within it is defined here
        //each card gets its layout also here
        JFrame frame = new JFrame("Wallet");
        JPanel cardPanel = new JPanel(new CardLayout());

        JPanel initialCard = new JPanel();
        initialCard.setLayout(new BoxLayout(initialCard, BoxLayout.Y_AXIS));

        JPanel signInCard = new JPanel();
        signInCard.setLayout(new BoxLayout(signInCard ,BoxLayout.Y_AXIS));

        JPanel signUpCard = new JPanel();
        signUpCard.setLayout(new BoxLayout(signUpCard, BoxLayout.Y_AXIS));

        JPanel mainMenuCard = new JPanel();
        mainMenuCard.setLayout(new BoxLayout(mainMenuCard, BoxLayout.Y_AXIS));

        //adding all cards to the main panel, which is called cardPanel
        cardPanel.add(initialCard, "initialCard");
        cardPanel.add(signInCard, "signInCard");
        cardPanel.add(signUpCard, "signUpCard");
        cardPanel.add(mainMenuCard, "mainMenuCard");

        //creating initialCard which has two buttons for sign-in or sign-up
        //sign-in button
        JButton signInButton = new JButton("SIGN-IN");
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "signInCard");
            }
        });

        //sign-up button
        JButton signUpButton = new JButton("SIGN-UP");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "signUpCard");
            }
        });

        //finalizing the initialCard
        initialCard.add(signInButton);
        initialCard.add(signUpButton);

        //creating signUpCard which is the card in which user creates a new account
        //name panel and text field
        JPanel signUpNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel signUpNameLabel = new JLabel("Name: ");
        JTextField signUpNameTextField = new JTextField(20);
        signUpNamePanel.add(signUpNameLabel);
        signUpNamePanel.add(signUpNameTextField);

        //username label and text field
        JPanel signUpUsernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel signUpUsernameLabel = new JLabel("Username: ");
        JTextField signUpUsernameTextField = new JTextField(20);
        signUpUsernamePanel.add(signUpUsernameLabel);
        signUpUsernamePanel.add(signUpUsernameTextField);

        //password label and text field
        JPanel signUpPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel signUpPasswordLabel = new JLabel("Password: ");
        JPasswordField signUpPasswordTextField = new JPasswordField(20);
        signUpPasswordPanel.add(signUpPasswordLabel);
        signUpPasswordPanel.add(signUpPasswordTextField);

        //confirm password label and text field
        JPanel signUpConfirmPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel signUpConfirmPasswordLabel = new JLabel("Password: ");
        JPasswordField signUpConfirmPasswordTextField = new JPasswordField(20);
        signUpConfirmPasswordPanel.add(signUpConfirmPasswordLabel);
        signUpConfirmPasswordPanel.add(signUpConfirmPasswordTextField);

        //back and create buttons
        JPanel buttonsInSignUpCardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButtonInSignUpCard = new JButton("BACK");
        backButtonInSignUpCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "initialCard");
            }
        });
        JButton signUpCreateButton = new JButton("CREATE ACCOUNT");
        signUpCreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = signUpNameTextField.getText();
                String username = signUpUsernameTextField.getText();
                char[] charPassword = signUpPasswordTextField.getPassword();
                String password = new String(charPassword);
                char[] charConfirmPassword = signUpConfirmPasswordTextField.getPassword();
                String confirmPassword = new String(charConfirmPassword);
                Map<String, String> userPassMap = readFromUserPassFile();
                if (name.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please fill all the fields!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    if (!userPassMap.containsKey(username)) {
                        if (password.equals(confirmPassword)) {
                            String hashedPassword = hashPassword(password);
                            Wallet newWallet = new Wallet(name, username, hashedPassword);
                            addToUserPassFile(username, hashedPassword);
                            newWallet.storeOnFile();
                            CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                            cardLayout.show(cardPanel, "initialCard");
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Passwords does not match!", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Username already taken!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }java.util.Arrays.fill(charPassword, '0');
                java.util.Arrays.fill(charConfirmPassword, '0');
            }
        });
        buttonsInSignUpCardPanel.add(backButtonInSignUpCard);
        buttonsInSignUpCardPanel.add(signUpCreateButton);

        //finalizing the signUpCard
        signUpCard.add(signUpNamePanel);
        signUpCard.add(signUpUsernamePanel);
        signUpCard.add(signUpPasswordPanel);
        signUpCard.add(signUpConfirmPasswordPanel);
        signUpCard.add(buttonsInSignUpCardPanel);

        //creating signInCard which is the card in which user enters his/her username pass
        //username label and textField
        JPanel signInUsernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel signInUsernameLabel = new JLabel("Username: ");
        JTextField signInUsernameTextField = new JTextField(20);
        signInUsernamePanel.add(signInUsernameLabel);
        signInUsernamePanel.add(signInUsernameTextField);

        //password label and textField
        JPanel signInPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel signInPasswordLabel = new JLabel("Password: ");
        JPasswordField signInPasswordTextField = new JPasswordField(20);
        signInPasswordPanel.add(signInPasswordLabel);
        signInPasswordPanel.add(signInPasswordTextField);

        //back and enter button
        JPanel buttonsInSignInCardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButtonInSignInCard = new JButton("BACK");
        backButtonInSignInCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                cardLayout.show(cardPanel, "initialCard");
            }
        });
        JButton enterAccountButton = new JButton("Enter");
        enterAccountButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                String username = signInUsernameTextField.getText();
                char[] charPassword = signInPasswordTextField.getPassword();
                String password = new String(charPassword);
                String hashedPassword = hashPassword(password);
                Map<String, String> userPassMap = readFromUserPassFile();
                if (username.isBlank() || password.isBlank()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please fill all the fields!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean found = false;
                    for (String _username : userPassMap.keySet()) {
                        if (_username.equals(username)) {
                            found = true;
                            if (userPassMap.get(_username).equals(hashedPassword)) {
                                CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
                                cardLayout.show(cardPanel, "mainMenuCard");
                            } else {
                                JOptionPane.showMessageDialog(frame,
                                        "Incorrect password!", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }if(!found) {
                        JOptionPane.showMessageDialog(frame,
                                "Username not found!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }java.util.Arrays.fill(charPassword, '0');
            }
        });
        buttonsInSignInCardPanel.add(backButtonInSignInCard);
        buttonsInSignInCardPanel.add(enterAccountButton);

        //finalizing the signIn card
        signInCard.add(signInUsernamePanel);
        signInCard.add(signInPasswordPanel);
        signInCard.add(buttonsInSignInCardPanel);

        //finalizing the whole frame
        frame.add(cardPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
