package wallet.tableModel;

import record.IncomeRecord;
import record.SpentRecord;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SpentTableModel extends AbstractTableModel {
    private final List<SpentRecord> spentRecords;
    private final String[] columnNames = {"ID", "Amount", "Date", "Category", "Seller", "Rate"};

    public SpentTableModel(List<SpentRecord> spentRecords) {
        this.spentRecords = spentRecords;
    }

    @Override
    public int getRowCount() {
        return spentRecords.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SpentRecord spentRecord = spentRecords.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return spentRecord.getRecordID();
            }
            case 1 -> {
                return spentRecord.getAmount();
            }
            case 2 -> {
                return spentRecord.getDate();
            }
            case 3 -> {
                return spentRecord.getCategory();
            }
            case 4 -> {
                return spentRecord.getSeller();
            }
            case 5 -> {
                return spentRecord.getRate();
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
    public void addRecord(SpentRecord spentRecord){
        spentRecords.add(spentRecord);
        fireTableRowsInserted(0, 0);
    }

    public void deleteRecord(int rowIndex){
        spentRecords.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
