package main.wallet.wallet_graphics.main_card.side_panel;

import main.record.Record;
import main.wallet.table_model.IncomeTableModel;
import main.wallet.wallet_graphics.TextFieldUtils;
import main.wallet.wallet_graphics.main_card.HintTextField;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class IncomeQuery extends JPanel {
    public IncomeQuery(JFrame mainFrame , IncomeTableModel incomeTableModel,
                       JLabel incomeCumulativeAmount, NumberFormatter formatter){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //toggle sorting changes the order of the records in the table, increasing or decreasing
        JPanel toggleButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JToggleButton amountToggleButton = new JToggleButton("Amount Toggle Sorting");

        amountToggleButton.addActionListener(e -> {
            if(amountToggleButton.isSelected()){
                incomeTableModel.recordsAmountIncreasingOrder();
            }else{
                incomeTableModel.recordsAmountDecreasingOrder();
            }
        });

        JToggleButton dateToggleButton = new JToggleButton("Date Toggle Sorting");
        dateToggleButton.addActionListener(e -> {
            if(dateToggleButton.isSelected()){
                incomeTableModel.recordsDateIncreasingOrder();
            }else{
                incomeTableModel.recordsDateDecreasingOrder();
            }
        });

        toggleButtons.add(amountToggleButton);
        toggleButtons.add(dateToggleButton);
        add(toggleButtons);

        //from amount label and text field
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel amountLabel = new JLabel("Amount:");
        amountPanel.add(amountLabel);
        add(amountPanel);

        JPanel amountFrom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel amountFromLabel = new JLabel("    From:        ");
        JFormattedTextField amountFromTextField = new JFormattedTextField(formatter);
        amountFromTextField.setColumns(10);
        amountFrom.add(amountFromLabel);
        amountFrom.add(amountFromTextField);
        add(amountFrom);

        //to amount label and text field
        JPanel amountTo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel amountToLabel = new JLabel("    To:            ");
        JFormattedTextField amountToTextField = new JFormattedTextField(formatter);
        amountToTextField.setColumns(10);
        amountTo.add(amountToLabel);
        amountTo.add(amountToTextField);
        add(amountTo);

        //from date label and text field
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dateLabel = new JLabel("Date:");
        datePanel.add(dateLabel);
        add(datePanel);

        JPanel dateFrom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dateFromLabel = new JLabel("    From:        ");
        HintTextField dateFromTextField = new HintTextField("YYYY-MM-DD");
        dateFromTextField.setColumns(10);
        dateFrom.add(dateFromLabel);
        dateFrom.add(dateFromTextField);
        add(dateFrom);

        //to date label and text field
        JPanel dateTo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dateToLabel = new JLabel("    To:            ");
        HintTextField dateToTextField = new HintTextField("YYYY-MM-DD");
        dateToTextField.setColumns(10);
        dateTo.add(dateToLabel);
        dateTo.add(dateToTextField);
        add(dateTo);

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

        //query button and its action listener
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton queryButton = new JButton("Query");
        queryButton.addActionListener(e -> {
            try{
                double fromAmount = 0;
                double toAmount = 0;
                LocalDate init = null;
                LocalDate end = null;
                if(!amountFromTextField.getText().isBlank()){
                    fromAmount = (double)amountFromTextField.getValue();
                }
                if(!amountToTextField.getText().isBlank()){
                    toAmount = (double)amountToTextField.getValue();
                }
                incomeTableModel.incomeRecordsBetweenTwoAmounts(fromAmount, toAmount);
                if(!dateFromTextField.getText().isBlank()){
                    init = LocalDate.parse(dateFromTextField.getText());
                }
                if(!dateToTextField.getText().isBlank()){
                    end = LocalDate.parse(dateToTextField.getText());
                }
                incomeTableModel.incomeRecordsBetweenTwoDates(init, end);
                if(!sourceTextField.getText().isBlank()){
                    incomeTableModel.incomeRecordsQueryOnSource(sourceTextField.getText());
                }
                if(spentHoursTextField.getValue() != null){
                    incomeTableModel.incomeRecordsQueryOnSpentHours((double)spentHoursTextField.getValue());
                }
                if(rateComboBox.getSelectedItem() != null){
                    incomeTableModel.incomeRecordsQueryOnRate((Record.Rate)rateComboBox.getSelectedItem());
                }
                incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
            }catch (DateTimeParseException dateTimeParseException){
                JOptionPane.showMessageDialog(mainFrame,
                        "Date must follow the specified format!\nFormat: YYYY-MM-DD",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            incomeTableModel.reset();
            incomeCumulativeAmount.setText(String.valueOf(incomeTableModel.incomeCumulativeAmount()));
            TextFieldUtils.clearTextFields(dateFromTextField, dateToTextField, sourceTextField);
            TextFieldUtils.clearFormattedFields(amountFromTextField, amountToTextField, spentHoursTextField);
            rateComboBox.setSelectedItem(null);
        });
        buttons.add(queryButton);
        buttons.add(resetButton);
        add(buttons);
    }
}
