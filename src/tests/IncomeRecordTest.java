package tests;

import record.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class IncomeRecordTest {

    @Test
    public void testIncomeRecordInitialization() {
        double amount = 100.0;
        LocalDate date = LocalDate.now();

        IncomeRecord incomeRecord = new IncomeRecord(amount, date);

        assertNull(incomeRecord.getSource());
        assertNull(incomeRecord.getSpentHours());
    }

    @Test
    public void testSetAndGetSource() {
        double amount = 100.0;
        LocalDate date = LocalDate.now();

        IncomeRecord incomeRecord = new IncomeRecord(amount, date);
        assertNull(incomeRecord.getSource());

        incomeRecord.setSource("Salary");
        assertEquals("Salary", incomeRecord.getSource());
    }

    @Test
    public void testSetAndGetSpentHours() {
        double amount = 100.0;
        LocalDate date = LocalDate.now();

        IncomeRecord incomeRecord = new IncomeRecord(amount, date);
        assertNull(incomeRecord.getSpentHours());

        incomeRecord.setSpent_hours(8.0);
        assertEquals(8.0, incomeRecord.getSpentHours(), 0.01);
    }

    @Test
    public void testCompareTo() {
        IncomeRecord record1 = new IncomeRecord(100.0, LocalDate.of(2023, 1, 1));
        IncomeRecord record2 = new IncomeRecord(150.0, LocalDate.of(2022, 12, 31));
        assertTrue(record1.compareTo(record2) > 0);
    }
}

