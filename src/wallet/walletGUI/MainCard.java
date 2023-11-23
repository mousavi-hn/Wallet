package wallet.walletGUI;

import record.IncomeRecord;
import record.Record;
import record.SpentRecord;
import wallet.Wallet;
import wallet.tableModel.IncomeTableModel;
import wallet.tableModel.SpentTableModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
    public void init(Wallet wallet, Frame previousFrame){
        JFrame frame = new JFrame(wallet.getOwner().toUpperCase() + "'s Account");

        //creating option menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Options");

        JMenuItem changePassword = new JMenuItem("Change Password");
        JMenuItem deleteAccount = new JMenuItem("Delete Account");
        JMenuItem exitAccount = new JMenuItem("Exit Account");

        fileMenu.add(changePassword);
        fileMenu.add(deleteAccount);
        fileMenu.add(exitAccount);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        //change password in option menu
        JDialog dialog = new JDialog(frame, "Change Password", true);

        JLabel previousPasswordLabel = new JLabel("Previous Password:");
        JPasswordField previousPasswordField = new JPasswordField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        JButton changeButton = new JButton("Change Password");

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
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] previousPassword = previousPasswordField.getPassword();
                String _previousPassword = new String(previousPassword);
                String hashedPreviousPassword = InitialCards.hashPassword(_previousPassword);

                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if(newPassword.equals(confirmPassword)){
                    String hashedNewPassword = InitialCards.hashPassword(newPassword);
                    if(wallet.resetPassword(hashedPreviousPassword, hashedNewPassword)){
                        JOptionPane.showMessageDialog(frame,
                                "Password changed!", "Successful",
                                JOptionPane.INFORMATION_MESSAGE);
                        dialog.setVisible(false);
                    }else {
                        JOptionPane.showMessageDialog(frame,
                                "Wrong previous password!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(frame,
                            "Provided new password does not match!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //finalizing the change password dialog
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(frame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        changePassword.addActionListener(e -> dialog.setVisible(true));

        //delete account menu item
        deleteAccount.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete the wallet?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION) {
                if (wallet.deleteWallet()) {
                    JOptionPane.showMessageDialog(frame,
                            "Wallet was deleted!", "Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    previousFrame.setVisible(true);
                    frame.dispose();
                }
            }
        });

        //exit account menu item
        exitAccount.addActionListener(e -> {
            wallet.storeOnFile();
            previousFrame.setVisible(true);
            frame.dispose();});

        //creating mainCard, it has two parts, one shows the records in a table
        //the other enables the user to add/delete/analyse the records
        JPanel mainCard = new JPanel();
        mainCard.setLayout(new BorderLayout());
        JTabbedPane tablePanel = new JTabbedPane();
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel ,BoxLayout.Y_AXIS));

        //tablePanel has two cards, first is for the spent records, the other for income records
        //table for income records
        IncomeTableModel incomeTableModel = new IncomeTableModel(wallet.getIncomeRecords());
        JTable incomeTable = new JTable(incomeTableModel);
        JScrollPane incomeScrollPane = new JScrollPane(incomeTable);

        JLabel incomeCumulativeAmountLabel = new JLabel("Cumulative Amount: ");
        JLabel incomeCumulativeAmount =
                new JLabel(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
        JPanel incomeCumulativeAmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        incomeCumulativeAmountPanel.add(incomeCumulativeAmountLabel);
        incomeCumulativeAmountPanel.add(incomeCumulativeAmount);

        JPanel incomeTablePanel = new JPanel(new BorderLayout());
        incomeTablePanel.add(incomeScrollPane, BorderLayout.CENTER);
        incomeTablePanel.add(incomeCumulativeAmountPanel, BorderLayout.SOUTH);

        //table for spent records
        SpentTableModel spentTableModel = new SpentTableModel(wallet.getSpentRecords());
        JTable spentTable = new JTable(spentTableModel);
        JScrollPane spentScrollPane = new JScrollPane(spentTable);

        JLabel spentCumulativeAmountLabel = new JLabel("Cumulative Amount: ");
        JLabel spentCumulativeAmount =
                new JLabel(String.valueOf(spentTableModel.spentCumulativeAmount()));
        JPanel spentCumulativeAmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        spentCumulativeAmountPanel.add(spentCumulativeAmountLabel);
        spentCumulativeAmountPanel.add(spentCumulativeAmount);

        JPanel spentTablePanel = new JPanel(new BorderLayout());
        spentTablePanel.add(spentScrollPane, BorderLayout.CENTER);
        spentTablePanel.add(spentCumulativeAmountPanel, BorderLayout.SOUTH);

        //adding tables to the table panel with their specified cards
        tablePanel.add("Income Records", incomeTablePanel);
        tablePanel.add("Spent Records", spentTablePanel);
        mainCard.add(tablePanel, BorderLayout.CENTER);
        
        
        //side panel depends on the table, if the table is income records then it shows
        //operations relating to incomes, otherwise if table is spent records then it shows
        //operations for spent records
        JPanel operatorPanel = new JPanel(new CardLayout());
        JPanel incomeOperatorPanel = new JPanel(new CardLayout());
        JPanel spentOperatorPanel = new JPanel(new CardLayout());
        operatorPanel.add(incomeOperatorPanel, "incomeOperatorPanel");
        operatorPanel.add(spentOperatorPanel, "spentOperatorPanel");

        //by changing the table using the tabs above it operator panel changes accordingly
        tablePanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                CardLayout cardLayout = (CardLayout) operatorPanel.getLayout();
                if(tablePanel.getSelectedIndex() == 0){
                    cardLayout.show(operatorPanel, "incomeOperatorPanel");
                }else{
                    cardLayout.show(operatorPanel, "spentOperatorPanel");
                }
            }
        });

        //income operator panel
        JPanel incomeAddDeletePanel = new JPanel();
        incomeAddDeletePanel.setLayout(new BoxLayout(incomeAddDeletePanel, BoxLayout.Y_AXIS));
        JPanel incomeQueryPanel = new JPanel();
        incomeQueryPanel.setLayout(new BoxLayout(incomeQueryPanel, BoxLayout.Y_AXIS));
        incomeOperatorPanel.add(incomeAddDeletePanel, "incomeAddDeletePanel");
        incomeOperatorPanel.add(incomeQueryPanel, "incomeQueryPanel");

        JTabbedPane incomeOperatorModeTabs = new JTabbedPane();
        incomeOperatorModeTabs.add("Add-Delete Mode", incomeAddDeletePanel);
        incomeOperatorModeTabs.add("Query Mode", incomeQueryPanel);
        incomeOperatorPanel.add(incomeOperatorModeTabs);

        //spent operator panel
        JPanel spentAddDeletePanel = new JPanel();
        spentAddDeletePanel.setLayout(new BoxLayout(spentAddDeletePanel, BoxLayout.Y_AXIS));
        JPanel spentQueryPanel = new JPanel();
        spentQueryPanel.setLayout(new BoxLayout(spentQueryPanel, BoxLayout.Y_AXIS));
        spentOperatorPanel.add(spentAddDeletePanel, "spentAddDeletePanel");
        spentOperatorPanel.add(spentQueryPanel, "spentQueryPanel");

        JTabbedPane spentOperatorModeTabs = new JTabbedPane();
        spentOperatorModeTabs.add("Add-Delete Mode", spentAddDeletePanel);
        spentOperatorModeTabs.add("Query Mode", spentQueryPanel);
        spentOperatorPanel.add(spentOperatorModeTabs);

        //income records add/delete panel
        //amount label and formatted text field
        JPanel incomeAddDeletePanelAmount = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeAddDeletePanelAmountLabel = new JLabel("Amount:        ");

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
        JLabel incomeAddDeletePanelDateLabel = new JLabel("Date:             ");
        HintTextField incomeAddDeletePanelDateTextField = new HintTextField("YYYY-MM-DD");
        incomeAddDeletePanelDateTextField.setColumns(10);
        incomeAddDeletePanelDate.add(incomeAddDeletePanelDateLabel);
        incomeAddDeletePanelDate.add(incomeAddDeletePanelDateTextField);
        incomeAddDeletePanel.add(incomeAddDeletePanelDate);

        //source label and text field
        JPanel incomeAddDeletePanelSource = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeAddDeletePanelSourceLabel = new JLabel("Source:          ");
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
        JLabel incomeAddDeletePanelRateLabel = new JLabel("Rate:              ");
        JComboBox<Record.Rate> incomeAddDeletePanelRateComboBox = new JComboBox<>();
        incomeAddDeletePanelRateComboBox.addItem(null);
        for(Record.Rate rate : Record.Rate.values()){
            incomeAddDeletePanelRateComboBox.addItem(rate);
        }
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
                        incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
                        incomeAddDeletePanelAmountTextField.setValue(null);
                        incomeAddDeletePanelDateTextField.setText("");
                        incomeAddDeletePanelSourceTextField.setText("");
                        incomeAddDeletePanelSpentHoursTextField.setValue(null);
                        incomeAddDeletePanelRateComboBox.setSelectedItem(null);
                    }else {
                        JOptionPane.showMessageDialog(frame,
                                "Amount is necessary and must be a positive value!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch (DateTimeParseException exception){
                    JOptionPane.showMessageDialog(frame,
                            "Date is necessary and must follow the " +
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
                    incomeTableModel.deleteRecord(selectedRow,
                            (String)incomeTable.getValueAt(selectedRow, 0));
                    incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
                }
            }
        });
        incomeAddDeletePanelButtons.add(incomeAddDeletePanelAddButton);
        incomeAddDeletePanelButtons.add(incomeAddDeletePanelDeleteButton);
        incomeAddDeletePanel.add(incomeAddDeletePanelButtons);

        //spent records add/delete panel
        //amount label and formatted text field
        JPanel spentAddDeletePanelAmount = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelAmountLabel = new JLabel("Amount:   ");

        JFormattedTextField spentAddDeletePanelAmountTextField = new JFormattedTextField(formatter);
        spentAddDeletePanelAmountTextField.setColumns(10);
        spentAddDeletePanelAmount.add(spentAddDeletePanelAmountLabel);
        spentAddDeletePanelAmount.add(spentAddDeletePanelAmountTextField);
        spentAddDeletePanel.add(spentAddDeletePanelAmount);

        //date label and text field
        JPanel spentAddDeletePanelDate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelDateLabel = new JLabel("Date:        ");
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
        JLabel spentAddDeletePanelSellerLabel = new JLabel("Seller:      ");
        JTextField spentAddDeletePanelSellerTextField = new JTextField(10);
        spentAddDeletePanelSeller.add(spentAddDeletePanelSellerLabel);
        spentAddDeletePanelSeller.add(spentAddDeletePanelSellerTextField);
        spentAddDeletePanel.add(spentAddDeletePanelSeller);

        //rate label and scroll bar
        JPanel spentAddDeletePanelRate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentAddDeletePanelRateLabel = new JLabel("Rate:        ");
        JComboBox<Record.Rate> spentAddDeletePanelRateComboBox = new JComboBox<>();
        spentAddDeletePanelRateComboBox.addItem(null);
        for(Record.Rate rate : Record.Rate.values()){
            spentAddDeletePanelRateComboBox.addItem(rate);
        }
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
                        spentCumulativeAmount.setText(String.valueOf(spentTableModel.spentCumulativeAmount()));
                        spentAddDeletePanelAmountTextField.setValue(null);
                        spentAddDeletePanelDateTextField.setText("");
                        spentAddDeletePanelCategoryTextField.setText("");
                        spentAddDeletePanelSellerTextField.setText("");
                        spentAddDeletePanelRateComboBox.setSelectedItem(null);
                    }else {
                        JOptionPane.showMessageDialog(frame,
                                "Amount is necessary and must be a positive value!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch (DateTimeParseException exception){
                    JOptionPane.showMessageDialog(frame,
                            "Date is necessary and must follow the " +
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
                    spentTableModel.deleteRecord(selectedRow,
                            (String)spentTable.getValueAt(selectedRow, 0));
                    spentCumulativeAmount.setText(String.valueOf(spentTableModel.spentCumulativeAmount()));
                }
            }
        });
        spentAddDeletePanelButtons.add(spentAddDeletePanelAddButton);
        spentAddDeletePanelButtons.add(spentAddDeletePanelDeleteButton);
        spentAddDeletePanel.add(spentAddDeletePanelButtons);

        //creating query panels
        //income query panel
        //toggle sorting changes the order of the records in the table, increasing or decreasing
        JPanel incomeQueryPanelToggleButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton incomeAmountToggleButton = new JToggleButton("Amount Toggle Sorting");
        incomeAmountToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(incomeAmountToggleButton.isSelected()){
                    incomeTableModel.recordsAmountIncreasingOrder();
                }else{
                    incomeTableModel.recordsAmountDecreasingOrder();
                }
            }
        });
        JToggleButton incomeDateToggleButton = new JToggleButton("Date Toggle Sorting");
        incomeDateToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(incomeDateToggleButton.isSelected()){
                    incomeTableModel.recordsDateIncreasingOrder();
                }else{
                    incomeTableModel.recordsDateDecreasingOrder();
                }
            }
        });
        incomeQueryPanelToggleButtons.add(incomeAmountToggleButton);
        incomeQueryPanelToggleButtons.add(incomeDateToggleButton);
        incomeQueryPanel.add(incomeQueryPanelToggleButtons);

        //from date label and text field
        JPanel incomeQueryPanelDateFrom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeQueryPanelDateFromLabel = new JLabel("From:            ");
        HintTextField incomeQueryPanelDateFromTextField = new HintTextField("YYYY-MM-DD");
        incomeQueryPanelDateFromTextField.setColumns(10);
        incomeQueryPanelDateFrom.add(incomeQueryPanelDateFromLabel);
        incomeQueryPanelDateFrom.add(incomeQueryPanelDateFromTextField);
        incomeQueryPanel.add(incomeQueryPanelDateFrom);

        //to date label and text field
        JPanel incomeQueryPanelDateTo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeQueryPanelDateToLabel = new JLabel("To:                ");
        HintTextField incomeQueryPanelDateToTextField = new HintTextField("YYYY-MM-DD");
        incomeQueryPanelDateToTextField.setColumns(10);
        incomeQueryPanelDateTo.add(incomeQueryPanelDateToLabel);
        incomeQueryPanelDateTo.add(incomeQueryPanelDateToTextField);
        incomeQueryPanel.add(incomeQueryPanelDateTo);

        //source label and text field
        JPanel incomeQueryPanelSource = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeQueryPanelSourceLabel = new JLabel("Source:          ");
        JTextField incomeQueryPanelSourceTextField = new JTextField(10);
        incomeQueryPanelSource.add(incomeQueryPanelSourceLabel);
        incomeQueryPanelSource.add(incomeQueryPanelSourceTextField);
        incomeQueryPanel.add(incomeQueryPanelSource);

        //spent hours label and formatted text field
        JPanel incomeQueryPanelSpentHours = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeQueryPanelSpentHoursLabel = new JLabel("Spent-hours: ");
        JFormattedTextField incomeQueryPanelSpentHoursTextField = new JFormattedTextField(formatter);
        incomeQueryPanelSpentHoursTextField.setColumns(10);
        incomeQueryPanelSpentHours.add(incomeQueryPanelSpentHoursLabel);
        incomeQueryPanelSpentHours.add(incomeQueryPanelSpentHoursTextField);
        incomeQueryPanel.add(incomeQueryPanelSpentHours);

        //rate label and scroll bar
        JPanel incomeQueryPanelRate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel incomeQueryPanelRateLabel = new JLabel("Rate:              ");
        JComboBox<Record.Rate> incomeQueryPanelRateComboBox = new JComboBox<>();
        incomeQueryPanelRateComboBox.addItem(null);
        for(Record.Rate rate : Record.Rate.values()){
            incomeQueryPanelRateComboBox.addItem(rate);
        }
        incomeQueryPanelRate.add(incomeQueryPanelRateLabel);
        incomeQueryPanelRate.add(incomeQueryPanelRateComboBox);
        incomeQueryPanel.add(incomeQueryPanelRate);

        //query button and its action listener
        JPanel incomeQueryPanelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton incomeQueryButton = new JButton("Query");
        incomeQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    LocalDate init = null;
                    LocalDate end = null;
                    if(!incomeQueryPanelDateFromTextField.getText().isBlank()){
                        init = LocalDate.parse(incomeQueryPanelDateFromTextField.getText());
                    }
                    if(!incomeQueryPanelDateToTextField.getText().isBlank()){
                        end = LocalDate.parse(incomeQueryPanelDateToTextField.getText());
                    }
                    incomeTableModel.incomeRecordsBetweenTwoDates(init, end);
                    if(!incomeQueryPanelSourceTextField.getText().isBlank()){
                        incomeTableModel.incomeRecordsQueryOnSource(incomeQueryPanelSourceTextField.getText());
                    }
                    if(incomeQueryPanelSpentHoursTextField.getValue() != null){
                        incomeTableModel.incomeRecordsQueryOnSpentHours((double)incomeQueryPanelSpentHoursTextField.getValue());
                    }
                    if(incomeQueryPanelRateComboBox.getSelectedItem() != null){
                        incomeTableModel.incomeRecordsQueryOnRate((Record.Rate)incomeQueryPanelRateComboBox.getSelectedItem());
                    }
                    incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
                }catch (DateTimeParseException dateTimeParseException){
                    JOptionPane.showMessageDialog(frame,
                            "Date must follow the specified format!\nFormat: YYYY-MM-DD",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton incomeResetButton = new JButton("Reset");
        incomeResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                incomeTableModel.reset();
                incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
                incomeQueryPanelDateFromTextField.setText("");
                incomeQueryPanelDateToTextField.setText("");
                incomeQueryPanelSourceTextField.setText("");
                incomeQueryPanelSpentHoursTextField.setValue(null);
                incomeQueryPanelRateComboBox.setSelectedItem(null);
            }
        });
        incomeQueryPanelButtons.add(incomeQueryButton);
        incomeQueryPanelButtons.add(incomeResetButton);
        incomeQueryPanel.add(incomeQueryPanelButtons);

        //spent query panel
        //toggle sorting changes the order of the records in the table, increasing or decreasing
        JPanel spentQueryPanelToggleButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton spentAmountToggleButton = new JToggleButton("Amount Toggle Sorting");
        spentAmountToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(spentAmountToggleButton.isSelected()){
                    spentTableModel.recordsAmountIncreasingOrder();
                }else{
                    spentTableModel.recordsAmountDecreasingOrder();
                }
            }
        });
        JToggleButton spentDateToggleButton = new JToggleButton("Date Toggle Sorting");
        spentDateToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(spentDateToggleButton.isSelected()){
                    spentTableModel.recordsDateIncreasingOrder();
                }else{
                    spentTableModel.recordsDateDecreasingOrder();
                }
            }
        });
        spentQueryPanelToggleButtons.add(spentAmountToggleButton);
        spentQueryPanelToggleButtons.add(spentDateToggleButton);
        spentQueryPanel.add(spentQueryPanelToggleButtons);

        //from date label and text field
        JPanel spentQueryPanelDateFrom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentQueryPanelDateFromLabel = new JLabel("From:       ");
        HintTextField spentQueryPanelDateFromTextField = new HintTextField("YYYY-MM-DD");
        spentQueryPanelDateFromTextField.setColumns(10);
        spentQueryPanelDateFrom.add(spentQueryPanelDateFromLabel);
        spentQueryPanelDateFrom.add(spentQueryPanelDateFromTextField);
        spentQueryPanel.add(spentQueryPanelDateFrom);

        //to date label and text field
        JPanel spentQueryPanelDateTo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentQueryPanelDateToLabel = new JLabel("To:           ");
        HintTextField spentQueryPanelDateToTextField = new HintTextField("YYYY-MM-DD");
        spentQueryPanelDateToTextField.setColumns(10);
        spentQueryPanelDateTo.add(spentQueryPanelDateToLabel);
        spentQueryPanelDateTo.add(spentQueryPanelDateToTextField);
        spentQueryPanel.add(spentQueryPanelDateTo);

        //category label and text field
        JPanel spentQueryPanelCategory = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentQueryPanelCategoryLabel = new JLabel("Category: ");
        JTextField spentQueryPanelCategoryTextField = new JTextField(10);
        spentQueryPanelCategory.add(spentQueryPanelCategoryLabel);
        spentQueryPanelCategory.add(spentQueryPanelCategoryTextField);
        spentQueryPanel.add(spentQueryPanelCategory);

        //seller label and formatted text field
        JPanel spentQueryPanelSeller = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentQueryPanelSellerLabel = new JLabel("Seller:       ");
        JTextField spentQueryPanelSellerTextField = new JTextField(10);
        spentQueryPanelSeller.add(spentQueryPanelSellerLabel);
        spentQueryPanelSeller.add(spentQueryPanelSellerTextField);
        spentQueryPanel.add(spentQueryPanelSeller);

        //rate label and scroll bar
        JPanel spentQueryPanelRate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentQueryPanelRateLabel = new JLabel("Rate:         ");
        JComboBox<Record.Rate> spentQueryPanelRateComboBox = new JComboBox<>();
        spentQueryPanelRateComboBox.addItem(null);
        for(Record.Rate rate : Record.Rate.values()){
            spentQueryPanelRateComboBox.addItem(rate);
        }
        spentQueryPanelRate.add(spentQueryPanelRateLabel);
        spentQueryPanelRate.add(spentQueryPanelRateComboBox);
        spentQueryPanel.add(spentQueryPanelRate);

        //query button and its action listener
        JPanel spentQueryPanelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton spentQueryButton = new JButton("Query");
        spentQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    LocalDate init = null;
                    LocalDate end = null;
                    if(!spentQueryPanelDateFromTextField.getText().isBlank()){
                        init = LocalDate.parse(spentQueryPanelDateFromTextField.getText());
                    }
                    if(!spentQueryPanelDateToTextField.getText().isBlank()){
                        end = LocalDate.parse(spentQueryPanelDateToTextField.getText());
                    }
                    spentTableModel.spentRecordsBetweenTwoDates(init, end);
                    if(!spentQueryPanelCategoryTextField.getText().isBlank()){
                        spentTableModel.spentRecordsQueryOnCategory(spentQueryPanelCategoryTextField.getText());
                    }
                    if(!spentQueryPanelSellerTextField.getText().isBlank()){
                        spentTableModel.spentRecordsQueryOnSeller(spentQueryPanelSellerTextField.getText());
                    }
                    if(spentQueryPanelRateComboBox.getSelectedItem() != null){
                        spentTableModel.spentRecordsQueryOnRate((Record.Rate)spentQueryPanelRateComboBox.getSelectedItem());
                    }
                    spentCumulativeAmount.setText(String.valueOf(spentTableModel.spentCumulativeAmount()));
                }catch (DateTimeParseException dateTimeParseException){
                    JOptionPane.showMessageDialog(frame,
                            "Date must follow the specified format!\nFormat: YYYY-MM-DD",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton spentResetButton = new JButton("Reset");
        spentResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spentTableModel.reset();
                spentCumulativeAmount.setText(String.valueOf(spentTableModel.spentCumulativeAmount()));
                spentQueryPanelDateFromTextField.setText("");
                spentQueryPanelDateToTextField.setText("");
                spentQueryPanelCategoryTextField.setText("");
                spentQueryPanelSellerTextField.setText("");
                spentQueryPanelRateComboBox.setSelectedItem(null);
            }
        });
        spentQueryPanelButtons.add(spentQueryButton);
        spentQueryPanelButtons.add(spentResetButton);
        spentQueryPanel.add(spentQueryPanelButtons);

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
