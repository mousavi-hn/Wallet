package record;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;
public abstract class Record {
    private final String transactionID;
    private final double amount;
    private final LocalDate date;
    public enum Rate{
        Excellent, Good, NotBad, Bad, Terrible
    }
    private Rate rate;
    public Record(double amount, LocalDate date){
        this.transactionID = UUID.randomUUID().toString();
        this.amount = amount;
        this.date = date;
        this.rate = null;
    }
    public Record(double amount, LocalDate date, Rate rate){
        this.transactionID = UUID.randomUUID().toString();
        this.amount = amount;
        this.date = date;
        this.rate = rate;
    }
    public String getTransactionID(){return transactionID;}
    public double getAmount(){return amount;}
    public LocalDate getDate(){return date;}
    public Rate getRate(){return rate;}
    public void setRate(Rate rate){this.rate = rate;}
}
