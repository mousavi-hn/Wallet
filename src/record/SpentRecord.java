package record;

import java.time.LocalDate;

public class SpentRecord extends Record {
    private final String item;
    private final String seller;

    public SpentRecord(double amount, LocalDate date){
        super(amount, date);
        this.item = null;
        this.seller = null;
    }
    public SpentRecord(double amount, LocalDate date, String item){
        super(amount, date);
        this.item = item;
        this.seller = null;
    }
    public SpentRecord(double amount, LocalDate date, String item,
                       String seller){
        super(amount, date);
        this.item = item;
        this.seller = seller;
    }
    public SpentRecord(double amount, LocalDate date, String item, Rate rate){
        super(amount, date, rate);
        this.item = item;
        this.seller = null;
    }
    public SpentRecord(double amount, LocalDate date, Rate rate, String seller){
        super(amount, date, rate);
        this.item = null;
        this.seller = seller;
    }
    public SpentRecord(double amount, LocalDate date, String item,
                       Rate rate, String seller){
        super(amount, date, rate);
        this.item = item;
        this.seller = seller;
    }
    public String getSeller(){return seller;}
    public String getItem(){return item;}
}
