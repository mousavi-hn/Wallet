package wallet.tableModel;

import record.IncomeRecord;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class IncomeTableModel extends AbstractTableModel {
    private final List<IncomeRecord> incomeRecords;
    private final String[] columnNames = {"ID", "Amount", "Date", "Source", "Spent-hours", "Rate"};

    public IncomeTableModel(List<IncomeRecord> incomeRecords) {
        this.incomeRecords = incomeRecords;
    }

    @Override
    public int getRowCount() {
        return incomeRecords.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IncomeRecord incomeRecord = incomeRecords.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return incomeRecord.getRecordID();
            }
            case 1 -> {
                return incomeRecord.getAmount();
            }
            case 2 -> {
                return incomeRecord.getDate();
            }
            case 3 -> {
                return incomeRecord.getSource();
            }
            case 4 -> {
                return incomeRecord.getSpentHours();
            }
            case 5 -> {
                return incomeRecord.getRate();
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    public void addRecord(IncomeRecord incomeRecord){
        incomeRecords.add(incomeRecord);
        fireTableRowsInserted(0, 0);
    }

    public void deleteRecord(int rowIndex){
        incomeRecords.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
