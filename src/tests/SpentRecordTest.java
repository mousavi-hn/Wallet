package tests;

import main.record.*;

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
}

