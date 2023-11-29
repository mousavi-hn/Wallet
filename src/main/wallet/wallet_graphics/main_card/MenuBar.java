package main.wallet.wallet_graphics.main_card;

import main.wallet.Wallet;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * two important functionalities, changing password of an account or deleting an account
 * are implemented using this class
 */
public class MenuBar extends JMenuBar {
    static final String CHANGE_PASSWORD = "Change Password";
    static final String ERROR = "ERROR";
    public MenuBar(Wallet wallet, Frame initialFrame, Frame mainFrame, Logger logger){
        JMenu fileMenu = new JMenu("Options");

        JMenuItem changePassword = new JMenuItem(CHANGE_PASSWORD);
        JMenuItem deleteAccount = new JMenuItem("Delete Account");
        JMenuItem exitAccount = new JMenuItem("Exit Account");

        fileMenu.add(changePassword);
        fileMenu.add(deleteAccount);
        fileMenu.add(exitAccount);
        add(fileMenu);

        //change password in option menu
        JDialog dialog = new JDialog(mainFrame, CHANGE_PASSWORD, true);

        JLabel previousPasswordLabel = new JLabel("Previous Password:");
        JPasswordField previousPasswordField = new JPasswordField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton changeButton = new JButton(CHANGE_PASSWORD);

        dialog.setLayout(new GridLayout(5, 2));
        dialog.add(previousPasswordLabel);
        dialog.add(previousPasswordField);
        dialog.add(newPasswordLabel);
        dialog.add(newPasswordField);
        dialog.add(confirmPasswordLabel);
        dialog.add(confirmPasswordField);
        dialog.add(new JLabel()); // Empty label for spacing
        dialog.add(new JLabel());
        dialog.add(changeButton);

        // Add action listener to the change button
        changeButton.addActionListener(e -> {
            char[] charPreviousPassword = previousPasswordField.getPassword();
            String previousPassword = new String(charPreviousPassword);
            String hashedPreviousPassword = hashPassword(previousPassword, logger);

            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if(newPassword.equals(confirmPassword)){
                String hashedNewPassword = hashPassword(newPassword, logger);
                assert hashedPreviousPassword != null;
                if(wallet.resetPassword(hashedPreviousPassword, hashedNewPassword, logger)){
                    JOptionPane.showMessageDialog(mainFrame,
                            "Password changed!", "Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    dialog.setVisible(false);
                }else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Wrong previous password!", ERROR,
                            JOptionPane.ERROR_MESSAGE);
                }
            }else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Provided new password does not match!", ERROR,
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        //finalizing the change password dialog
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        changePassword.addActionListener(e -> dialog.setVisible(true));

        //delete account menu item
        deleteAccount.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete the wallet?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if(choice == JOptionPane.YES_OPTION ) {

                wallet.deleteWallet(logger);
                    JOptionPane.showMessageDialog(mainFrame,
                            "Wallet was deleted!", "Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    initialFrame.setVisible(true);
                    mainFrame.dispose();
            }
        });

        //exit account menu item
        exitAccount.addActionListener(e -> {
            wallet.storeOnFile(logger);
            initialFrame.setVisible(true);
            mainFrame.dispose();});
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
