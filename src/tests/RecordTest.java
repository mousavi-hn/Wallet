package tests;
import org.junit.Test;
import main.record.*;
import main.record.Record;

import static org.junit.Assert.*;

import java.time.LocalDate;

public class RecordTest {

    @Test
    public void testRecordInitialization() {
        double amount = 100.0;
        LocalDate date = LocalDate.now();

        Record record = new IncomeRecord(amount, date);

        assertNotNull(record.getRecordID());
        assertEquals(amount, record.getAmount(), 0.01);
        assertEquals(date, record.getDate());
        assertNull(record.getRate());
    }

    @Test
    public void testSetAndGetRate() {
        double amount = 100.0;
        LocalDate date = LocalDate.now();

        Record record = new IncomeRecord(amount, date);
        assertNull(record.getRate());

        record.setRate(Record.Rate.Good);
        assertEquals(Record.Rate.Good, record.getRate());
    }
}

