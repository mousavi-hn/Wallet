package main.record;

import java.io.Serializable;
import java.time.LocalDate;

public class IncomeRecord extends Record implements Serializable {
    private String source;
    private Double spentHours;

    public IncomeRecord(double amount, LocalDate date){
        super(amount, date);
        this.source = null;
        this.spentHours = null;
    }
    public String getSource(){return source;}
    public void setSource(String source){this.source = source;}
    public Double getSpentHours(){return spentHours;}
    public void setSpentHours(Double spentHours){this.spentHours = spentHours;}
}