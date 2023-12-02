package main.wallet.table_model;

import main.record.IncomeRecord;
import main.record.Record;
import main.record.SpentRecord;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpentTableModel extends AbstractTableModel {
    private final List<SpentRecord> originalSpentRecords;
    public List<SpentRecord> displayedSpentRecords;
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
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0 || columnIndex == 2 ||
                columnIndex == 3 || columnIndex == 4){ return String.class; }
        else if(columnIndex == 1){ return Double.class; }
        else { return Record.Rate.class; }
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
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        SpentRecord spentRecord = originalSpentRecords.get(rowIndex);

        switch (columnIndex) {
            case 1 -> {
                if(value != null && !value.toString().isEmpty()){
                    spentRecord.setAmount(Double.parseDouble(value.toString()));
                }else{
                    JOptionPane.showMessageDialog(null, "Amount can not be empty!"
                            , "Invalid input", JOptionPane.WARNING_MESSAGE);
                }
            }
            case 2 -> {
                if(value != null && !value.toString().isEmpty()) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    try{
                        LocalDate localDate = LocalDate.parse(value.toString(), dateTimeFormatter);
                        spentRecord.setDate(localDate);
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null,
                                "Date must follow 'YYYY-MM-DD' format!"
                                , "Invalid input", JOptionPane.WARNING_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,
                            "Date can not be empty and must follow 'YYYY-MM-DD' format!"
                            , "Invalid input", JOptionPane.WARNING_MESSAGE);
                }
            }
            case 3 -> {
                spentRecord.setCategory((String) value);
            }
            case 4 -> {
                spentRecord.setSeller((String) value);
            }
            case 5 -> {
                spentRecord.setRate((Record.Rate) value);
            }
        }

        displayedSpentRecords = new ArrayList<>(originalSpentRecords);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        return columnIndex == 1 || columnIndex == 2 || columnIndex == 3
                || columnIndex == 4 || columnIndex == 5;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void addRecord(SpentRecord spentRecord){
        originalSpentRecords.add(0 , spentRecord);
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

    public void recordsBetweenTwoAmounts(double firstAmount, double secondAmount){
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

    public void recordsBetweenTwoDates(LocalDate init, LocalDate end){
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

    public void recordsQueryOnCategory(String category){
        displayedSpentRecords.removeIf(spentRecord -> !spentRecord.getCategory().equals(category));
        fireTableStructureChanged();
    }

    public void recordsQueryOnSeller(String seller){
        displayedSpentRecords.removeIf(spentRecord -> !spentRecord.getSeller().equals(seller));
    }

    public void recordsQueryOnRate(Record.Rate rate){
        displayedSpentRecords.removeIf(spentRecord -> spentRecord.getRate() == null ||
                !spentRecord.getRate().equals(rate));
    }

    public double cumulativeAmount(){
        double sum = 0;
        for(SpentRecord spentRecord : displayedSpentRecords){ sum += spentRecord.getAmount(); }
        return sum;
    }
}
