/**
 * this package represents a single main.record
 * there are two types of records, income and spent main.record
 */
package main.record;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
public abstract class Record implements Serializable {
    private final String recordID;
    private final double amount;
    private final LocalDate date;
    public enum Rate{
        Excellent, Good, NotBad, Bad, Terrible
    }
    private Rate rate;
    public Record(double amount, LocalDate date){
        this.recordID = UUID.randomUUID().toString();
        this.amount = amount;
        this.date = date;
        this.rate = null;
    }
    public String getRecordID(){return recordID;}
    public double getAmount(){return amount;}
    public LocalDate getDate(){return date;}
    public Rate getRate(){return rate;}
    public void setRate(Rate rate){this.rate = rate;}
}
