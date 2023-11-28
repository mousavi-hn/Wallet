package tests;

import record.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class SpentRecordTest {

    @Test
    public void testSpentRecordInitialization() {
        double amount = 50.0;
        LocalDate date = LocalDate.now();

        SpentRecord spentRecord = new SpentRecord(amount, date);

        assertNull(spentRecord.getCategory());
        assertNull(spentRecord.getSeller());
    }

    @Test
    public void testSetAndGetCategory() {
        double amount = 50.0;
        LocalDate date = LocalDate.now();

        SpentRecord spentRecord = new SpentRecord(amount, date);
        assertNull(spentRecord.getCategory());

        spentRecord.setCategory("Groceries");
        assertEquals("Groceries", spentRecord.getCategory());
    }

    @Test
    public void testSetAndGetSeller() {
        double amount = 50.0;
        LocalDate date = LocalDate.now();

        SpentRecord spentRecord = new SpentRecord(amount, date);
        assertNull(spentRecord.getSeller());

        spentRecord.setSeller("LocalMart");
        assertEquals("LocalMart", spentRecord.getSeller());
    }

    @Test
    public void testCompareTo() {
        SpentRecord record1 = new SpentRecord(50.0, LocalDate.of(2023, 1, 1));
        SpentRecord record2 = new SpentRecord(30.0, LocalDate.of(2023, 1, 2));
        assertTrue(record1.compareTo(record2) < 0);
    }
}

