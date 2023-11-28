package wallet.wallet_graphics.main_card;

import javax.swing.*;
import java.awt.*;

public class TablePanel extends JTabbedPane {
    public TablePanel(JPanel operatorPanel, JTable incomeTable, JLabel incomeCumulativeAmount,
                      JTable spentTable, JLabel spentCumulativeAmount){

        //table for income records
        JScrollPane incomeScrollPane = new JScrollPane(incomeTable);

        JLabel incomeCumulativeAmountLabel = new JLabel("Cumulative Amount: ");
        JPanel incomeCumulativeAmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        incomeCumulativeAmountPanel.add(incomeCumulativeAmountLabel);
        incomeCumulativeAmountPanel.add(incomeCumulativeAmount);

        JPanel incomeTablePanel = new JPanel(new BorderLayout());
        incomeTablePanel.add(incomeScrollPane, BorderLayout.CENTER);
        incomeTablePanel.add(incomeCumulativeAmountPanel, BorderLayout.SOUTH);

        //table for spent records
        JScrollPane spentScrollPane = new JScrollPane(spentTable);

        JLabel spentCumulativeAmountLabel = new JLabel("Cumulative Amount: ");
        JPanel spentCumulativeAmountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        spentCumulativeAmountPanel.add(spentCumulativeAmountLabel);
        spentCumulativeAmountPanel.add(spentCumulativeAmount);

        JPanel spentTablePanel = new JPanel(new BorderLayout());
        spentTablePanel.add(spentScrollPane, BorderLayout.CENTER);
        spentTablePanel.add(spentCumulativeAmountPanel, BorderLayout.SOUTH);

        //adding tables to the table panel with their specified cards
        add("Income Records", incomeTablePanel);
        add("Spent Records", spentTablePanel);

        //by changing the table using the tabs above it operator panel changes accordingly
        addChangeListener(e -> {
            CardLayout cardLayout = (CardLayout) operatorPanel.getLayout();
            if(getSelectedIndex() == 0){
                cardLayout.show(operatorPanel, "incomeOperatorPanel");
            }else{
                cardLayout.show(operatorPanel, "spentOperatorPanel");
            }
        });
    }
}
