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
    private double amount;
    private LocalDate date;
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
    public void setAmount(double amount){ this.amount = amount; }
    public LocalDate getDate(){return date;}
    public void setDate(LocalDate date){ this.date = date; }
    public Rate getRate(){return rate;}
    public void setRate(Rate rate){this.rate = rate;}
}
