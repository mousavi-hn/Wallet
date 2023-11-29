package main.wallet.wallet_graphics.main_card.side_panel;

import main.record.Record;
import main.record.SpentRecord;
import main.wallet.table_model.SpentTableModel;
import main.wallet.wallet_graphics.TextFieldUtils;
import main.wallet.wallet_graphics.main_card.HintTextField;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SpentAddDelete extends JPanel {
    public SpentAddDelete(JFrame mainFrame , SpentTableModel spentTableModel,
                          JTable spentTable , JLabel spentCumulativeAmount, NumberFormatter formatter){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //amount label and formatted text field
        JPanel amount = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel amountLabel = new JLabel("Amount:   ");

        JFormattedTextField amountTextField = new JFormattedTextField(formatter);
        amountTextField.setColumns(10);
        amount.add(amountLabel);
        amount.add(amountTextField);
        add(amount);

        //date label and text field
        JPanel date = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel dateLabel = new JLabel("Date:        ");
        HintTextField dateTextField = new HintTextField("YYYY-MM-DD");
        dateTextField.setColumns(10);
        date.add(dateLabel);
        date.add(dateTextField);
        add(date);

        //Category label and text field
        JPanel category = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel categoryLabel = new JLabel("Category: ");
        JTextField categoryTextField = new JTextField(10);
        category.add(categoryLabel);
        category.add(categoryTextField);
        add(category);

        //spent hours label and formatted text field
        JPanel seller = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sellerLabel = new JLabel("Seller:      ");
        JTextField sellerTextField = new JTextField(10);
        seller.add(sellerLabel);
        seller.add(sellerTextField);
        add(seller);

        //rate label and scroll bar
        JPanel rate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel rateLabel = new JLabel("Rate:        ");
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
                Object amount1 = amountTextField.getValue();
                LocalDate localDate = LocalDate.parse(dateTextField.getText());
                if(amount1 != null) {
                    SpentRecord spentRecord = new SpentRecord((double) amount1, localDate);
                    spentRecord.setCategory(categoryTextField.getText());
                    spentRecord.setSeller(sellerTextField.getText());
                    spentRecord.setRate((Record.Rate) rateComboBox.getSelectedItem());

                    spentTableModel.addRecord(spentRecord);
                    spentCumulativeAmount.setText(String.valueOf(spentTableModel.spentCumulativeAmount()));

                    TextFieldUtils.clearTextFields(dateTextField, categoryTextField, sellerTextField);
                    amountTextField.setValue(null);
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
        deleteButton.addActionListener(e -> {
            int selectedRow = spentTable.getSelectedRow();
            if(selectedRow != -1){
                spentTableModel.deleteRecord(selectedRow,
                        (String)spentTable.getValueAt(selectedRow, 0));
                spentCumulativeAmount.setText(String.valueOf(spentTableModel.spentCumulativeAmount()));
            }
        });
        buttons.add(addButton);
        buttons.add(deleteButton);
        add(buttons);
    }
}
