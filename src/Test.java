import java.time.LocalDate;
import wallet.*;

import record.*;
import record.Record;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {
    public static void main(String[] args){
        Map<String, String> user = Wallet.readFromUserPassFile();
        System.out.println(user);
    }
}
