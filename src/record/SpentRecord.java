package record;

import java.time.LocalDate;

public class SpentRecord extends Record implements Comparable<SpentRecord> {
    private String category;
    private String seller;

    public int compareTo(SpentRecord record){
        return this.getDate().compareTo(record.getDate());
    }
    public SpentRecord(double amount, LocalDate date){
        super(amount, date);
        this.category = null;
        this.seller = null;
    }
    public SpentRecord(double amount, LocalDate date, String category){
        super(amount, date);
        this.category = category;
        this.seller = null;
    }
    public SpentRecord(double amount, LocalDate date, String category,
                       String seller){
        super(amount, date);
        this.category = category;
        this.seller = seller;
    }
    public SpentRecord(double amount, LocalDate date, String category, Rate rate){
        super(amount, date, rate);
        this.category = category;
        this.seller = null;
    }
    public SpentRecord(double amount, LocalDate date, Rate rate, String seller){
        super(amount, date, rate);
        this.category = null;
        this.seller = seller;
    }
    public SpentRecord(double amount, LocalDate date, String category,
                       Rate rate, String seller){
        super(amount, date, rate);
        this.category = category;
        this.seller = seller;
    }
    public String getSeller(){return seller;}
    public void setSeller(String seller){this.seller = seller;}
    public String getCategory(){return category;}
    public void setCategory(String category){this.category = category;}
}
