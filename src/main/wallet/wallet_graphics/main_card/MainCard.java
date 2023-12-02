package main.wallet.wallet_graphics.main_card;

import main.record.Record;
import main.wallet.Wallet;
import main.wallet.table_model.IncomeTableModel;
import main.wallet.table_model.SpentTableModel;
import main.wallet.wallet_graphics.main_card.side_panel.IncomeAddDelete;
import main.wallet.wallet_graphics.main_card.side_panel.IncomeQuery;
import main.wallet.wallet_graphics.main_card.side_panel.SpentAddDelete;
import main.wallet.wallet_graphics.main_card.side_panel.SpentQuery;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.*;
import java.util.logging.Logger;

/**
 * the main interface of the software, user can see all the records in two separate tables of
 * income and spent records, can add to or delete from it using the provided side panels and
 * also performing queries on data using regular constraints like records between two specified
 * amounts and dates and within a category
 */
public class MainCard extends JFrame{
    public void init(Wallet wallet, Frame initialFrame, Logger logger){
        setTitle(wallet.getOwner().toUpperCase() + "'s Wallet");

        //creating option menu
        MenuBar menuBar = new MenuBar(wallet, initialFrame, this, logger);
        setJMenuBar(menuBar);

        //creating mainCard, it has two parts, one shows the records in a table
        //the other enables the user to add/delete/analyse the records
        JPanel mainCard = new JPanel();
        mainCard.setLayout(new BorderLayout());

        //side panel depends on the table, if the table is income records then it shows
        //operations relating to incomes, otherwise if table is spent records then it shows
        //operations for spent records
        JPanel operatorPanel = new JPanel(new CardLayout());
        JPanel incomeOperatorPanel = new JPanel(new CardLayout());
        JPanel spentOperatorPanel = new JPanel(new CardLayout());
        operatorPanel.add(incomeOperatorPanel, "incomeOperatorPanel");
        operatorPanel.add(spentOperatorPanel, "spentOperatorPanel");

        IncomeTableModel incomeTableModel = new IncomeTableModel(wallet.getIncomeRecords());
        JTable incomeTable = new JTable(incomeTableModel);
        TableColumn incomeRateColumn = incomeTable.getColumnModel().getColumn(5);
        incomeRateColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(Record.Rate.values())));
        JLabel incomeCumulativeAmount =
                new JLabel(String.valueOf(incomeTableModel.cumulativeAmount()));

        //action listener for the table so that when it is updated cumulative amount updates as well
        incomeTableModel.addTableModelListener(e -> {
            if(e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1){
                incomeCumulativeAmount.setText(
                        String.valueOf(incomeTableModel.cumulativeAmount()));
            }
        });

        SpentTableModel spentTableModel = new SpentTableModel(wallet.getSpentRecords());
        JTable spentTable = new JTable(spentTableModel);
        TableColumn spentRateColumn = spentTable.getColumnModel().getColumn(5);
        spentRateColumn.setCellEditor(new DefaultCellEditor(new JComboBox<>(Record.Rate.values())));
        JLabel spentCumulativeAmount =
                new JLabel(String.valueOf(spentTableModel.cumulativeAmount()));

        //action listener for the table so that when it is updated cumulative amount updates as well
        spentTableModel.addTableModelListener(e -> {
            if(e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1){
                spentCumulativeAmount.setText(
                        String.valueOf(spentTableModel.cumulativeAmount()));
            }
        });

        TablePanel tablePanel = new TablePanel(operatorPanel,incomeTable, incomeCumulativeAmount,
                spentTable, spentCumulativeAmount);
        mainCard.add(tablePanel, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel ,BoxLayout.Y_AXIS));

        NumberFormatter formatter = new NumberFormatter(new DecimalFormat("#0.00"));
        formatter.setValueClass(Double.class);
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);
        formatter.setAllowsInvalid(false);

        //income operator panel
        IncomeAddDelete incomeAddDeletePanel = new IncomeAddDelete(this, incomeTableModel,
                incomeTable, incomeCumulativeAmount, formatter);

        IncomeQuery incomeQueryPanel = new IncomeQuery(this, incomeTableModel,
                incomeCumulativeAmount,formatter);

        incomeOperatorPanel.add(incomeAddDeletePanel, "incomeAddDeletePanel");
        incomeOperatorPanel.add(incomeQueryPanel, "incomeQueryPanel");

        JTabbedPane incomeOperatorModeTabs = new JTabbedPane();
        incomeOperatorModeTabs.add("Add-Delete Mode", incomeAddDeletePanel);
        incomeOperatorModeTabs.add("Query Mode", incomeQueryPanel);
        incomeOperatorPanel.add(incomeOperatorModeTabs);

        //spent operator panel
        SpentAddDelete spentAddDeletePanel = new SpentAddDelete(this, spentTableModel,
                spentTable, spentCumulativeAmount, formatter);

        SpentQuery spentQueryPanel = new SpentQuery(this, spentTableModel,
                spentCumulativeAmount, formatter);

        spentOperatorPanel.add(spentAddDeletePanel, "spentAddDeletePanel");
        spentOperatorPanel.add(spentQueryPanel, "spentQueryPanel");

        JTabbedPane spentOperatorModeTabs = new JTabbedPane();
        spentOperatorModeTabs.add("Add-Delete Mode", spentAddDeletePanel);
        spentOperatorModeTabs.add("Query Mode", spentQueryPanel);
        spentOperatorPanel.add(spentOperatorModeTabs);

        sidePanel.add(operatorPanel);
        mainCard.add(sidePanel, BorderLayout.EAST);

        add(mainCard);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                wallet.storeOnFile(logger);
            }
        });
    }
}
