/**
 * tables to show all the records of a main.wallet with relevant functionalities
 * for example add/delete main.record or query
 */
package main.wallet.table_model;

import main.record.IncomeRecord;
import main.record.Record;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IncomeTableModel extends AbstractTableModel {
    private final List<IncomeRecord> originalIncomeRecords;
    public List<IncomeRecord> displayedIncomeRecords;
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
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0 || columnIndex == 2 || columnIndex == 3){ return String.class; }
        else if(columnIndex == 1 || columnIndex == 4){ return Double.class; }
        else {return Record.Rate.class; }
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
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        IncomeRecord incomeRecord = originalIncomeRecords.get(rowIndex);

        switch (columnIndex) {
            case 1 -> {
                if(value != null && !value.toString().isEmpty()){
                    incomeRecord.setAmount(Double.parseDouble(value.toString()));
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
                        incomeRecord.setDate(localDate);
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
                incomeRecord.setSource((String) value);
            }
            case 4 -> {
                incomeRecord.setSpentHours(Double.parseDouble(value.toString()));
            }
            case 5 -> {
                incomeRecord.setRate((Record.Rate) value);
            }
        }

        displayedIncomeRecords = new ArrayList<>(originalIncomeRecords);
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

    public void addRecord(IncomeRecord incomeRecord){
        originalIncomeRecords.add(0 , incomeRecord);
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

    public void recordsBetweenTwoAmounts(double firstAmount, double secondAmount){
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

    public void recordsBetweenTwoDates(LocalDate init, LocalDate end){
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

    public void recordsQueryOnSource(String source){
        displayedIncomeRecords.removeIf(incomeRecord -> !incomeRecord.getSource().equals(source));
        fireTableStructureChanged();
    }

    public void recordsQueryOnSpentHours(double spentHours){
        displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getSpentHours() == null ||
                incomeRecord.getSpentHours() > spentHours);
    }

    public void recordsQueryOnRate(Record.Rate rate){
        displayedIncomeRecords.removeIf(incomeRecord -> incomeRecord.getRate() == null ||
                !incomeRecord.getRate().equals(rate));
    }

    public double cumulativeAmount(){
        double sum = 0;
        for(IncomeRecord incomeRecord : displayedIncomeRecords){ sum += incomeRecord.getAmount(); }
        return sum;
    }
}
