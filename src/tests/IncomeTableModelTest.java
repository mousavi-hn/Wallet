package tests;

import record.IncomeRecord;
import wallet.table_model.IncomeTableModel;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncomeTableModelTest {

    @Test
    public void testInitialization() {
        List<IncomeRecord> incomeRecords = new ArrayList<>();
        IncomeTableModel incomeTableModel = new IncomeTableModel(incomeRecords);

        assertEquals(0, incomeTableModel.getRowCount());
        assertEquals(6, incomeTableModel.getColumnCount());
        assertEquals("ID", incomeTableModel.getColumnName(0));
        assertEquals("Amount", incomeTableModel.getColumnName(1));
        assertEquals("Date", incomeTableModel.getColumnName(2));
        assertEquals("Source", incomeTableModel.getColumnName(3));
        assertEquals("Spent-hours", incomeTableModel.getColumnName(4));
        assertEquals("Rate", incomeTableModel.getColumnName(5));
    }

    @Test
    public void testAddRecord() {
        List<IncomeRecord> incomeRecords = new ArrayList<>();
        IncomeTableModel incomeTableModel = new IncomeTableModel(incomeRecords);

        assertEquals(0, incomeTableModel.getRowCount());

        IncomeRecord incomeRecord = new IncomeRecord(100.0, LocalDate.now());
        incomeTableModel.addRecord(incomeRecord);

        assertEquals(1, incomeTableModel.getRowCount());
    }

    @Test
    public void testDeleteRecord() {
        List<IncomeRecord> incomeRecords = new ArrayList<>();
        IncomeTableModel incomeTableModel = new IncomeTableModel(incomeRecords);

        IncomeRecord incomeRecord = new IncomeRecord(100.0, LocalDate.now());
        incomeTableModel.addRecord(incomeRecord);

        assertEquals(1, incomeTableModel.getRowCount());

        incomeTableModel.deleteRecord(0, incomeRecord.getRecordID());

        assertEquals(0, incomeTableModel.getRowCount());
    }

    @Test
    public void testRecordsAmountIncreasingOrder() {
        List<IncomeRecord> incomeRecords = new ArrayList<>();
        IncomeTableModel incomeTableModel = new IncomeTableModel(incomeRecords);

        IncomeRecord record1 = new IncomeRecord(100.0, LocalDate.of(2023, 1, 1));
        IncomeRecord record2 = new IncomeRecord(150.0, LocalDate.of(2023, 1, 2));
        incomeTableModel.addRecord(record2);
        incomeTableModel.addRecord(record1);

        incomeTableModel.recordsAmountIncreasingOrder();

        assertEquals(record1, incomeTableModel.displayedIncomeRecords.get(0));
        assertEquals(record2, incomeTableModel.displayedIncomeRecords.get(1));
    }

    @Test
    public void testRecordsAmountDecreasingOrder() {
        List<IncomeRecord> incomeRecords = new ArrayList<>();
        IncomeTableModel incomeTableModel = new IncomeTableModel(incomeRecords);

        IncomeRecord record1 = new IncomeRecord(100.0, LocalDate.of(2023, 1, 1));
        IncomeRecord record2 = new IncomeRecord(150.0, LocalDate.of(2023, 1, 2));
        incomeTableModel.addRecord(record2);
        incomeTableModel.addRecord(record1);

        incomeTableModel.recordsAmountDecreasingOrder();

        assertEquals(record2, incomeTableModel.displayedIncomeRecords.get(0));
        assertEquals(record1, incomeTableModel.displayedIncomeRecords.get(1));
    }
}

