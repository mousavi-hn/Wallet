package wallet.wallet_graphics.main_card.side_panel;

import record.IncomeRecord;
import record.Record;
import wallet.table_model.IncomeTableModel;
import wallet.wallet_graphics.TextFieldUtils;
import wallet.wallet_graphics.main_card.HintTextField;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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
        JButton addButton = new JButton("Add record");
        addButton.addActionListener(e -> {
            try {
                Object amount1 = amountTextField.getValue();
                LocalDate localDate = LocalDate.parse(dateTextField.getText());
                if(amount1 != null) {

                    IncomeRecord incomeRecord = new IncomeRecord((double) amount1, localDate);
                    incomeRecord.setSource(sourceTextField.getText());
                    incomeRecord.setSpent_hours(
                            (Double) spentHoursTextField.getValue());
                    incomeRecord.setRate((Record.Rate) rateComboBox.getSelectedItem());
                    incomeTableModel.addRecord(incomeRecord);
                    incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));

                    TextFieldUtils.clearTextFields(dateTextField, sourceTextField);
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
        JButton deleteButton = new JButton("Delete record");
        deleteButton.addActionListener((ActionListener) e -> {
            int selectedRow = incomeTable.getSelectedRow();
            if(selectedRow != -1){
                incomeTableModel.deleteRecord(selectedRow,
                        (String)incomeTable.getValueAt(selectedRow, 0));
                incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
            }
        });
        buttons.add(addButton);
        buttons.add(deleteButton);
        add(buttons);
    }
}
