package record;

import java.time.LocalDate;

public class IncomeRecord extends Record implements Comparable<IncomeRecord> {
    private final String source;
    private final Integer spent_hours;

    public int compareTo(IncomeRecord record){
        return this.getDate().compareTo(record.getDate());
    }
    public IncomeRecord(double amount, LocalDate date){
        super(amount, date);
        this.source = null;
        this.spent_hours = null;
    }

    public IncomeRecord(double amount, LocalDate date, String source){
        super(amount, date);
        this.source = source;
        this.spent_hours = null;
    }
    public IncomeRecord(double amount, LocalDate date, Integer spent_hours){
        super(amount, date);
        this.source = null;
        this.spent_hours = spent_hours;
    }
    public IncomeRecord(double amount, LocalDate date, String source,
                        Integer spent_hours){
        super(amount, date);
        this.source = source;
        this.spent_hours = spent_hours;
    }
    public IncomeRecord(double amount, LocalDate date, String source, Rate rate){
        super(amount, date, rate);
        this.source = source;
        this.spent_hours = null;
    }
    public IncomeRecord(double amount, LocalDate date, Integer spent_hours,
                        Rate rate){
        super(amount, date, rate);
        this.source = null;
        this.spent_hours = spent_hours;
    }
    public IncomeRecord(double amount, LocalDate date, String source,
                        Integer spent_hours, Rate rate){
        super(amount, date, rate);
        this.source = source;
        this.spent_hours = spent_hours;
    }
    public String getSource(){return source;}
    public Integer getSpentHours(){return spent_hours;}
}