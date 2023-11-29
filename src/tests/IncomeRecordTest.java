package tests;

import main.record.*;

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

        incomeRecord.setSpentHours(8.0);
        assertEquals(8.0, incomeRecord.getSpentHours(), 0.01);
    }
}

