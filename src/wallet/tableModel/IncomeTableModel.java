package wallet.tableModel;

import record.IncomeRecord;
import record.Record;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IncomeTableModel extends AbstractTableModel {
    private final List<IncomeRecord> originalIncomeRecords;
    private List<IncomeRecord> displayedIncomeRecords;
    private final String[] columnNames = {"ID", "Amount", "Date", "Source", "Spent-hours", "Rate"};

    public IncomeTableModel(List<IncomeRecord> incomeRecords) {
        displayedIncomeRecords = new ArrayList<>(incomeRecords);
        originalIncomeRecords = incomeRecords;
    }

    @Override
    public int getRowCount() {
        return displayedIncomeRecords.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        IncomeRecord incomeRecord = displayedIncomeRecords.get(rowIndex);
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
        originalIncomeRecords.add(incomeRecord);
        displayedIncomeRecords = new ArrayList<>(originalIncomeRecords);
        fireTableRowsInserted(0, 0);
    }

    public void deleteRecord(int rowIndex, String recordID){
        originalIncomeRecords.removeIf(incomeRecord -> incomeRecord.getRecordID().equals(recordID));
        displayedIncomeRecords = new ArrayList<>(originalIncomeRecords);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void recordsAmountIncreasingOrder(){
        displayedIncomeRecords.sort(Comparator.comparingDouble(IncomeRecord :: getAmount));
        fireTableStructureChanged();
    }

    public void recordsAmountDecreasingOrder(){
        displayedIncomeRecords.sort(Comparator.comparingDouble(IncomeRecord :: getAmount).reversed());
        fireTableStructureChanged();
    }

    public void recordsDateIncreasingOrder(){
        displayedIncomeRecords.sort(Comparator.comparing(IncomeRecord :: getDate));
        fireTableStructureChanged();
    }

    public void recordsDateDecreasingOrder(){
        displayedIncomeRecords.sort(Comparator.comparing(IncomeRecord :: getDate).reversed());
        fireTableStructureChanged();
    }

    public void reset(){
        displayedIncomeRecords = new ArrayList<>(originalIncomeRecords);
        fireTableStructureChanged();
    }

    public void incomeRecordsBetweenTwoAmounts(double firstAmount, double secondAmount){
        if(firstAmount != 0 && secondAmount != 0){
            displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getAmount() < firstAmount
                    || incomeRecord.getAmount() > secondAmount);
        }else if(firstAmount == 0 && secondAmount != 0) {
            displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getAmount() > secondAmount);
        }else if(firstAmount != 0){
            displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getAmount() < firstAmount);
        }
        fireTableStructureChanged();
    }

    public void incomeRecordsBetweenTwoDates(LocalDate init, LocalDate end){
        if(init != null && end != null){
            displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getDate().isBefore(init)
                                            || incomeRecord.getDate().isAfter(end));
        }else if(init == null && end != null) {
            displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getDate().isAfter(end));
        }else if(init != null){
            displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getDate().isBefore(init));
        }
        fireTableStructureChanged();
    }

    public void incomeRecordsQueryOnSource(String source){
        displayedIncomeRecords.removeIf(incomeRecord -> !incomeRecord.getSource().equals(source));
        fireTableStructureChanged();
    }

    public void incomeRecordsQueryOnSpentHours(double spentHours){
        displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getSpentHours() == null ||
                incomeRecord.getSpentHours() > spentHours);
    }

    public void incomeRecordsQueryOnRate(Record.Rate rate){
        displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getRate() == null ||
                !incomeRecord.getRate().equals(rate));
    }

    public double incomeCumulativeAmount(){
        double sum = 0;
        for(IncomeRecord incomeRecord : displayedIncomeRecords){ sum += incomeRecord.getAmount(); }
        return sum;
    }
}
