/**
 * side panel provides interface to add/delete and query on records
 * it has these functionalities separately for income and spent records
 * due to their difference in the main.record structure
 */
package main.wallet.wallet_graphics.main_card.side_panel;

import main.record.IncomeRecord;
import main.record.Record;
import main.wallet.table_model.IncomeTableModel;
import main.wallet.wallet_graphics.TextFieldUtils;
import main.wallet.wallet_graphics.main_card.HintTextField;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class IncomeAddDelete extends JPanel {
    public IncomeAddDelete(JFrame mainFrame ,IncomeTableModel incomeTableModel,
                           JTable incomeTable ,JLabel incomeCumulativeAmount, NumberFormatter formatter){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //amount label and formatted text field
        JPanel amount = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel amountLabel = new JLabel("Amount:        ");

        JFormattedTextField amountTextField = new JFormattedTextField(formatter);
        amountTextField.setColumns(10);
        amount.add(amountLabel);
        amount.add(amountTextField);
        add(amount);

        //date label and text field
        JPanel date = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dateLabel = new JLabel("Date:             ");
        HintTextField dateTextField = new HintTextField("YYYY-MM-DD");
        dateTextField.setColumns(10);
        date.add(dateLabel);
        date.add(dateTextField);

        JButton incomeTodayButton = new JButton("Today");
        incomeTodayButton.addActionListener(e -> {
            dateTextField.setText(LocalDate.now().toString());
            dateTextField.setForeground(Color.BLACK);
        });

        date.add(incomeTodayButton);
        add(date);

        //source label and text field
        JPanel source = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sourceLabel = new JLabel("Source:          ");
        JTextField sourceTextField = new JTextField(10);
        source.add(sourceLabel);
        source.add(sourceTextField);
        add(source);

        //spent hours label and formatted text field
        JPanel spentHours = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel spentHoursLabel = new JLabel("Spent-hours: ");
        JFormattedTextField spentHoursTextField = new JFormattedTextField(formatter);
        spentHoursTextField.setColumns(10);
        spentHours.add(spentHoursLabel);
        spentHours.add(spentHoursTextField);
        add(spentHours);

        //rate label and scroll bar
        JPanel rate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel rateLabel = new JLabel("Rate:              ");
        JComboBox<Record.Rate> rateComboBox = new JComboBox<>();
        rateComboBox.addItem(null);
        for(Record.Rate varRate : Record.Rate.values()){
            rateComboBox.addItem(varRate);
        }
        rate.add(rateLabel);
        rate.add(rateComboBox);
        add(rate);

        //add button and its action listener
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            try {
                Object tmpAmount = amountTextField.getValue();
                LocalDate localDate = LocalDate.parse(dateTextField.getText());
                if(tmpAmount != null) {

                    IncomeRecord incomeRecord = new IncomeRecord((double) tmpAmount, localDate);
                    incomeRecord.setSource(sourceTextField.getText());
                    incomeRecord.setSpentHours(
                            (Double) spentHoursTextField.getValue());
                    incomeRecord.setRate((Record.Rate) rateComboBox.getSelectedItem());
                    incomeTableModel.addRecord(incomeRecord);
                    incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.cumulativeAmount()));

                    TextFieldUtils.clearTextFields(dateTextField, sourceTextField);
                    dateTextField.setForeground(Color.GRAY);
                    TextFieldUtils.clearFormattedFields(amountTextField, spentHoursTextField);
                    rateComboBox.setSelectedItem(null);

                }else {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Amount is necessary and must be a positive value!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }catch (DateTimeParseException exception){
                JOptionPane.showMessageDialog(mainFrame,
                        "Date is necessary and must follow the " +
                                "specified format!\nFormat: YYYY-MM-DD", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        //delete button and its action listener
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener((ActionListener) e -> {
            int selectedRow = incomeTable.getSelectedRow();
            if(selectedRow != -1){
                incomeTableModel.deleteRecord(selectedRow,
                        (String)incomeTable.getValueAt(selectedRow, 0));
                incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.cumulativeAmount()));
            }
        });
        buttons.add(addButton);
        buttons.add(deleteButton);
        add(buttons);
    }
}
