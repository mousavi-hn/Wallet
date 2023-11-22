package record;

import java.io.Serializable;
import java.time.LocalDate;

public class IncomeRecord extends Record implements Comparable<IncomeRecord>, Serializable {
    private String source;
    private Double spent_hours;

    public int compareTo(IncomeRecord record){
        return this.getDate().compareTo(record.getDate());
    }
    public IncomeRecord(double amount, LocalDate date){
        super(amount, date);
        this.source = null;
        this.spent_hours = null;
    }
    public String getSource(){return source;}
    public void setSource(String source){this.source = source;}
    public Double getSpentHours(){return spent_hours;}
    public void setSpent_hours(Double spent_hours){this.spent_hours = spent_hours;}
}