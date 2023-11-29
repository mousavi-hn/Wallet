package tests;

import main.record.SpentRecord;
import main.wallet.table_model.SpentTableModel;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SpentTableModelTest {

    @Test
    public void testInitialization() {
        List<SpentRecord> spentRecords = new ArrayList<>();
        SpentTableModel spentTableModel = new SpentTableModel(spentRecords);

        assertEquals(0, spentTableModel.getRowCount());
        assertEquals(6, spentTableModel.getColumnCount());
        assertEquals("ID", spentTableModel.getColumnName(0));
        assertEquals("Amount", spentTableModel.getColumnName(1));
        assertEquals("Date", spentTableModel.getColumnName(2));
        assertEquals("Category", spentTableModel.getColumnName(3));
        assertEquals("Seller", spentTableModel.getColumnName(4));
        assertEquals("Rate", spentTableModel.getColumnName(5));
    }

    @Test
    public void testAddRecord() {
        List<SpentRecord> spentRecords = new ArrayList<>();
        SpentTableModel spentTableModel = new SpentTableModel(spentRecords);

        assertEquals(0, spentTableModel.getRowCount());

        SpentRecord spentRecord = new SpentRecord(100.0, LocalDate.now());
        spentTableModel.addRecord(spentRecord);

        assertEquals(1, spentTableModel.getRowCount());
    }

    @Test
    public void testDeleteRecord() {
        List<SpentRecord> spentRecords = new ArrayList<>();
        SpentTableModel spentTableModel = new SpentTableModel(spentRecords);

        SpentRecord spentRecord = new SpentRecord(100.0, LocalDate.now());
        spentTableModel.addRecord(spentRecord);

        assertEquals(1, spentTableModel.getRowCount());

        spentTableModel.deleteRecord(0, spentRecord.getRecordID());

        assertEquals(0, spentTableModel.getRowCount());
    }

    @Test
    public void testRecordsAmountIncreasingOrder() {
        List<SpentRecord> spentRecords = new ArrayList<>();
        SpentTableModel spentTableModel = new SpentTableModel(spentRecords);

        SpentRecord record1 = new SpentRecord(100.0, LocalDate.of(2023, 1, 1));
        SpentRecord record2 = new SpentRecord(150.0, LocalDate.of(2023, 1, 2));
        spentTableModel.addRecord(record2);
        spentTableModel.addRecord(record1);

        spentTableModel.recordsAmountIncreasingOrder();

        assertEquals(record1, spentTableModel.displayedSpentRecords.get(0));
        assertEquals(record2, spentTableModel.displayedSpentRecords.get(1));
    }

    @Test
    public void testRecordsAmountDecreasingOrder() {
        List<SpentRecord> spentRecords = new ArrayList<>();
        SpentTableModel spentTableModel = new SpentTableModel(spentRecords);

        SpentRecord record1 = new SpentRecord(100.0, LocalDate.of(2023, 1, 1));
        SpentRecord record2 = new SpentRecord(150.0, LocalDate.of(2023, 1, 2));
        spentTableModel.addRecord(record2);
        spentTableModel.addRecord(record1);

        spentTableModel.recordsAmountDecreasingOrder();

        assertEquals(record2, spentTableModel.displayedSpentRecords.get(0));
        assertEquals(record1, spentTableModel.displayedSpentRecords.get(1));
    }
}

