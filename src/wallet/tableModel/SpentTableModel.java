package wallet.tableModel;

import record.Record;
import record.SpentRecord;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpentTableModel extends AbstractTableModel {
    private final List<SpentRecord> originalSpentRecords;
    private List<SpentRecord> displayedSpentRecords;
    private final String[] columnNames = {"ID", "Amount", "Date", "Category", "Seller", "Rate"};

    public SpentTableModel(List<SpentRecord> spentRecords) {

        displayedSpentRecords = new ArrayList<>(spentRecords);
        originalSpentRecords = spentRecords;
    }

    @Override
    public int getRowCount() {
        return displayedSpentRecords.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SpentRecord spentRecord = displayedSpentRecords.get(rowIndex);
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
        originalSpentRecords.add(spentRecord);
        displayedSpentRecords = new ArrayList<>(originalSpentRecords);
        fireTableRowsInserted(0, 0);
    }

    public void deleteRecord(int rowIndex, String recordID){
        originalSpentRecords.removeIf(spentRecord -> spentRecord.getRecordID().equals(recordID));
        displayedSpentRecords = new ArrayList<>(originalSpentRecords);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void recordsAmountIncreasingOrder(){
        displayedSpentRecords.sort(Comparator.comparingDouble(SpentRecord :: getAmount));
        fireTableStructureChanged();
    }

    public void recordsAmountDecreasingOrder(){
        displayedSpentRecords.sort(Comparator.comparingDouble(SpentRecord :: getAmount).reversed());
        fireTableStructureChanged();
    }

    public void recordsDateIncreasingOrder(){
        displayedSpentRecords.sort(Comparator.comparing(SpentRecord :: getDate));
        fireTableStructureChanged();
    }

    public void recordsDateDecreasingOrder(){
        displayedSpentRecords.sort(Comparator.comparing(SpentRecord :: getDate).reversed());
        fireTableStructureChanged();
    }

    public void reset(){
        displayedSpentRecords = new ArrayList<>(originalSpentRecords);
        fireTableStructureChanged();
    }

    public void spentRecordsBetweenTwoAmounts(double firstAmount, double secondAmount){
        if(firstAmount != 0 && secondAmount != 0){
            displayedSpentRecords.removeIf(spentRecord -> spentRecord.getAmount() < firstAmount
                    || spentRecord.getAmount() > secondAmount);
        }else if(firstAmount == 0 && secondAmount != 0) {
            displayedSpentRecords.removeIf(spentRecord -> spentRecord.getAmount() > secondAmount);
        }else if(firstAmount != 0){
            displayedSpentRecords.removeIf(spentRecord -> spentRecord.getAmount() < firstAmount);
        }
        fireTableStructureChanged();
    }

    public void spentRecordsBetweenTwoDates(LocalDate init, LocalDate end){
        if(init != null && end != null){
            displayedSpentRecords.removeIf(spentRecord -> spentRecord.getDate().isBefore(init)
                    || spentRecord.getDate().isAfter(end));
        }else if(init == null && end != null) {
            displayedSpentRecords.removeIf(spentRecord -> spentRecord.getDate().isAfter(end));
        }else if(init != null){
            displayedSpentRecords.removeIf(spentRecord -> spentRecord.getDate().isBefore(init));
        }
        fireTableStructureChanged();
    }

    public void spentRecordsQueryOnCategory(String category){
        displayedSpentRecords.removeIf(spentRecord -> !spentRecord.getCategory().equals(category));
        fireTableStructureChanged();
    }

    public void spentRecordsQueryOnSeller(String seller){
        displayedSpentRecords.removeIf(spentRecord -> !spentRecord.getSeller().equals(seller));
    }

    public void spentRecordsQueryOnRate(Record.Rate rate){
        displayedSpentRecords.removeIf(spentRecord -> spentRecord.getRate() == null ||
                !spentRecord.getRate().equals(rate));
    }

    public double spentCumulativeAmount(){
        double sum = 0;
        for(SpentRecord spentRecord : displayedSpentRecords){ sum += spentRecord.getAmount(); }
        return sum;
    }
}
