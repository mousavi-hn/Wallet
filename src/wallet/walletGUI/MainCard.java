package wallet.walletGUI;

import record.IncomeRecord;
import record.Record;
import record.SpentRecord;
import wallet.Wallet;
import wallet.tableModel.IncomeTableModel;
import wallet.tableModel.SpentTableModel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MainCard {
    public void init(Wallet wallet){
        JFrame frame = new JFrame(wallet.getOwner().toUpperCase() + "'s Account");

        //creating mainCard, it has two parts, one shows the records in a table
        //the other enables the user to add/delete/analyse the records
        JPanel mainCard = new JPanel();
        mainCard.setLayout(new BorderLayout());
        JPanel tablePanel = new JPanel(new CardLayout());
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel ,BoxLayout.Y_AXIS));

        //tablePanel has two cards, first is for the spent records, the other for income records
        //table for income records
        IncomeTableModel incomeTableModel = new IncomeTableModel(wallet.getIncomeRecords());
        JTable incomeTable = new JTable(incomeTableModel);
        JScrollPane incomeScrollPane = new JScrollPane(incomeTable);

        //table for spent records
        SpentTableModel spentTableModel = new SpentTableModel(wallet.getSpentRecords());
        JTable spentTable = new JTable(spentTableModel);
        JScrollPane spentScrollPane = new JScrollPane(spentTable);

        //adding tables to the table panel with their specified cards
        tablePanel.add(incomeScrollPane, "incomeScrollPane");
        tablePanel.add(spentScrollPane, "spentScrollPane");
        mainCard.add(tablePanel, BorderLayout.CENTER);
        
        
        //side panel has two buttons at the top which specifies which records we are working with
        //it can be income or spent records
        //below the record type choosing buttons, we have another panel
        //which has two cards, one is for add/delete row from the table, the other is
        //for query the data
        JPanel operatorPanel = new JPanel(new CardLayout());
        JPanel incomeOperatorPanel = new JPanel(new CardLayout());
        JPanel spentOperatorPanel = new JPanel(new CardLayout());
        operatorPanel.add(incomeOperatorPanel, "incomeOperatorPanel");
        operatorPanel.add(spentOperatorPanel, "spentOperatorPanel");
        
        JPanel incomeSpentButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton incomeButton = new JButton("Income");
        incomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) tablePanel.getLayout();
                cardLayout.show(tablePanel, "incomeScrollPane");
                CardLayout _cardLayout = (CardLayout) operatorPanel.getLayout();
                _cardLayout.show(operatorPanel, "incomeOperatorPanel");
            }
        });
        JButton spentButton = new JButton("Spent");
        spentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cardLayout = (CardLayout) tablePanel.getLayout();
                cardLayout.show(tablePanel, "spentScrollPane");
                CardLayout _cardLayout = (CardLayout) operatorPanel.getLayout();
                _cardLayout.show(operatorPanel, "spentOperatorPanel");
            }
        });
        incomeSpentButtons.add(incomeButton);
        incomeSpentButtons.add(spentButton);
        sidePanel.add(incomeSpentButtons);

        //income operator panel
        JPanel incomeAddDeletePanel = new JPanel();
        incomeAddDeletePanel.setLayout(new BoxLayout(incomeAddDeletePanel, BoxLayout.Y_AXIS));
        JPanel incomeQueryPanel = new JPanel();
        incomeQueryPanel.setLayout(new BoxLayout(incomeQueryPanel, BoxLayout.Y_AXIS));
        incomeOperatorPanel.add(incomeAddDeletePanel, "incomeAddDeletePanel");
        incomeOperatorPanel.add(incomeQueryPanel, "incomeQueryPanel");

        //spent operator panel
        JPanel spentAddDeletePanel = new JPanel();
        spentAddDeletePanel.setLayout(new BoxLayout(spentAddDeletePanel, BoxLayout.Y_AXIS));
        JPanel spentQueryPanel = new JPanel();
        spentQueryPanel.setLayout(new BoxLayout(spentQueryPanel, BoxLayout.Y_AXIS));
        spentOperatorPanel.add(spentAddDeletePanel, "spentAddDeletePanel");
        spentOperatorPanel.add(spentQueryPanel, "spentQueryPanel");

        //buttons for changing between add/delete mode and query mode
        JPanel operatorModeButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addDeleteButton = new JButton("Add/Delete");
        addDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tablePanel.getName().equals("incomeScrollPane")) {
                    CardLayout cardLayout = (CardLayout) incomeOperatorPanel.getLayout();
                    cardLayout.show(incomeOperatorPanel, "incomeAddDeletePanel");
                }else {
                    CardLayout cardLayout = (CardLayout) spentOperatorPanel.getLayout();
                    cardLayout.show(spentOperatorPanel, "spentAddDeletePanel");
                }
            }
        });
        JButton queryButton = new JButton("Query");
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tablePanel.getName().equals("incomeScrollPane")) {
                    CardLayout cardLayout = (CardLayout) incomeOperatorPanel.getLayout();
                    cardLayout.show(incomeOperatorPanel, "incomeQueryPanel");
                }else {
                    CardLayout cardLayout = (CardLayout) spentOperatorPanel.getLayout();
                    cardLayout.show(spentOperatorPanel, "spentQueryPanel");
                }
            }
        });
        operatorModeButtons.add(addDeleteButton);
        operatorModeButtons.add(queryButton);

        //income records add/delete panel
        //amount label and formatted text field
        JPanel incomeAddDeletePanelAmount = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeAddDeletePanelAmountLabel = new JLabel("Amount: ");

        NumberFormatter formatter = new NumberFormatter(new DecimalFormat("#0.00"));
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);

        JFormattedTextField incomeAddDeletePanelAmountTextField = new JFormattedTextField(formatter);
        incomeAddDeletePanelAmountTextField.setColumns(10);
        incomeAddDeletePanelAmount.add(incomeAddDeletePanelAmountLabel);
        incomeAddDeletePanelAmount.add(incomeAddDeletePanelAmountTextField);
        incomeAddDeletePanel.add(incomeAddDeletePanelAmount);

        //date label and text field
        JPanel incomeAddDeletePanelDate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeAddDeletePanelDateLabel = new JLabel("Date: ");
        HintTextField incomeAddDeletePanelDateTextField = new HintTextField("YYYY-MM-DD");
        incomeAddDeletePanelDateTextField.setColumns(10);
        incomeAddDeletePanelDate.add(incomeAddDeletePanelDateLabel);
        incomeAddDeletePanelDate.add(incomeAddDeletePanelDateTextField);
        incomeAddDeletePanel.add(incomeAddDeletePanelDate);

        //source label and text field
        JPanel incomeAddDeletePanelSource = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeAddDeletePanelSourceLabel = new JLabel("Source: ");
        JTextField incomeAddDeletePanelSourceTextField = new JTextField(10);
        incomeAddDeletePanelSource.add(incomeAddDeletePanelSourceLabel);
        incomeAddDeletePanelSource.add(incomeAddDeletePanelSourceTextField);
        incomeAddDeletePanel.add(incomeAddDeletePanelSource);

        //spent hours label and formatted text field
        JPanel incomeAddDeletePanelSpentHours = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeAddDeletePanelSpentHoursLabel = new JLabel("Spent-hours: ");
        JFormattedTextField incomeAddDeletePanelSpentHoursTextField = new JFormattedTextField(formatter);
        incomeAddDeletePanelSpentHoursTextField.setColumns(10);
        incomeAddDeletePanelSpentHours.add(incomeAddDeletePanelSpentHoursLabel);
        incomeAddDeletePanelSpentHours.add(incomeAddDeletePanelSpentHoursTextField);
        incomeAddDeletePanel.add(incomeAddDeletePanelSpentHours);

        //rate label and scroll bar
        JPanel incomeAddDeletePanelRate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeAddDeletePanelRateLabel = new JLabel("Rate: ");
        JComboBox<Record.Rate> incomeAddDeletePanelRateComboBox =
                new JComboBox<>(Record.Rate.values());
        incomeAddDeletePanelRate.add(incomeAddDeletePanelRateLabel);
        incomeAddDeletePanelRate.add(incomeAddDeletePanelRateComboBox);
        incomeAddDeletePanel.add(incomeAddDeletePanelRate);

        //add button and its action listener
        JPanel incomeAddDeletePanelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton incomeAddDeletePanelAddButton = new JButton("Add record");
        incomeAddDeletePanelAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object amount = incomeAddDeletePanelAmountTextField.getValue();
                    LocalDate localDate = LocalDate.parse(incomeAddDeletePanelDateTextField.getText());
                    if(amount != null) {
                        IncomeRecord incomeRecord = new IncomeRecord((double)amount, localDate);
                        incomeRecord.setSource(incomeAddDeletePanelSourceTextField.getText());
                        incomeRecord.setSpent_hours(
                                (Double) incomeAddDeletePanelSpentHoursTextField.getValue());
                        incomeRecord.setRate((Record.Rate) incomeAddDeletePanelRateComboBox.getSelectedItem());
                        incomeTableModel.addRecord(incomeRecord);
                        incomeAddDeletePanelAmountTextField.setValue(null);
                        incomeAddDeletePanelDateTextField.setText("");
                        incomeAddDeletePanelSourceTextField.setText("");
                        incomeAddDeletePanelSpentHoursTextField.setValue(null);
                    }else {
                        JOptionPane.showMessageDialog(frame,
                                "Amount is necessary and must be a positive value!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch (DateTimeParseException exception){
                    JOptionPane.showMessageDialog(frame,
                            "Date is necessary and must follow the" +
                                    "specified format!\nFormat: YYYY-MM-DD", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //delete button and its action listener
        JButton incomeAddDeletePanelDeleteButton = new JButton("Delete record");
        incomeAddDeletePanelDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = incomeTable.getSelectedRow();
                if(selectedRow != -1){
                    incomeTableModel.deleteRecord(selectedRow);
                }
            }
        });
        incomeAddDeletePanelButtons.add(incomeAddDeletePanelAddButton);
        incomeAddDeletePanelButtons.add(incomeAddDeletePanelDeleteButton);
        incomeAddDeletePanel.add(incomeAddDeletePanelButtons);

        //spent records add/delete panel
        //amount label and formatted text field
        JPanel spentAddDeletePanelAmount = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelAmountLabel = new JLabel("Amount: ");

        JFormattedTextField spentAddDeletePanelAmountTextField = new JFormattedTextField(formatter);
        spentAddDeletePanelAmountTextField.setColumns(10);
        spentAddDeletePanelAmount.add(spentAddDeletePanelAmountLabel);
        spentAddDeletePanelAmount.add(spentAddDeletePanelAmountTextField);
        spentAddDeletePanel.add(spentAddDeletePanelAmount);

        //date label and text field
        JPanel spentAddDeletePanelDate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelDateLabel = new JLabel("Date: ");
        HintTextField spentAddDeletePanelDateTextField = new HintTextField("YYYY-MM-DD");
        spentAddDeletePanelDateTextField.setColumns(10);
        spentAddDeletePanelDate.add(spentAddDeletePanelDateLabel);
        spentAddDeletePanelDate.add(spentAddDeletePanelDateTextField);
        spentAddDeletePanel.add(spentAddDeletePanelDate);

        //Category label and text field
        JPanel spentAddDeletePanelCategory = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelCategoryLabel = new JLabel("Category: ");
        JTextField spentAddDeletePanelCategoryTextField = new JTextField(10);
        spentAddDeletePanelCategory.add(spentAddDeletePanelCategoryLabel);
        spentAddDeletePanelCategory.add(spentAddDeletePanelCategoryTextField);
        spentAddDeletePanel.add(spentAddDeletePanelCategory);

        //spent hours label and formatted text field
        JPanel spentAddDeletePanelSeller = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelSellerLabel = new JLabel("Seller: ");
        JTextField spentAddDeletePanelSellerTextField = new JTextField(10);
        spentAddDeletePanelSeller.add(spentAddDeletePanelSellerLabel);
        spentAddDeletePanelSeller.add(spentAddDeletePanelSellerTextField);
        spentAddDeletePanel.add(spentAddDeletePanelSeller);

        //rate label and scroll bar
        JPanel spentAddDeletePanelRate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelRateLabel = new JLabel("Rate: ");
        JComboBox<Record.Rate> spentAddDeletePanelRateComboBox =
                new JComboBox<>(Record.Rate.values());
        spentAddDeletePanelRate.add(spentAddDeletePanelRateLabel);
        spentAddDeletePanelRate.add(spentAddDeletePanelRateComboBox);
        spentAddDeletePanel.add(spentAddDeletePanelRate);

        //add button and its action listener
        JPanel spentAddDeletePanelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton spentAddDeletePanelAddButton = new JButton("Add record");
        spentAddDeletePanelAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Object amount = spentAddDeletePanelAmountTextField.getValue();
                    LocalDate localDate = LocalDate.parse(spentAddDeletePanelDateTextField.getText());
                    if(amount != null) {
                        SpentRecord spentRecord = new SpentRecord((double)amount, localDate);
                        spentRecord.setCategory(spentAddDeletePanelCategoryTextField.getText());
                        spentRecord.setSeller(spentAddDeletePanelSellerTextField.getText());
                        spentRecord.setRate((Record.Rate) spentAddDeletePanelRateComboBox.getSelectedItem());
                        spentTableModel.addRecord(spentRecord);
                        spentAddDeletePanelAmountTextField.setValue(null);
                        spentAddDeletePanelDateTextField.setText("");
                        spentAddDeletePanelCategoryTextField.setText("");
                        spentAddDeletePanelSellerTextField.setText("");
                    }else {
                        JOptionPane.showMessageDialog(frame,
                                "Amount is necessary and must be a positive value!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch (DateTimeParseException exception){
                    JOptionPane.showMessageDialog(frame,
                            "Date is necessary and must follow the" +
                                    "specified format!\nFormat: YYYY-MM-DD", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //delete button and its action listener
        JButton spentAddDeletePanelDeleteButton = new JButton("Delete record");
        spentAddDeletePanelDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = spentTable.getSelectedRow();
                if(selectedRow != -1){
                    spentTableModel.deleteRecord(selectedRow);
                }
            }
        });
        spentAddDeletePanelButtons.add(spentAddDeletePanelAddButton);
        spentAddDeletePanelButtons.add(spentAddDeletePanelDeleteButton);
        spentAddDeletePanel.add(spentAddDeletePanelButtons);

        sidePanel.add(operatorPanel);
        mainCard.add(sidePanel, BorderLayout.EAST);
        frame.add(mainCard);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                wallet.storeOnFile();
            }
        });
    }
}
