package record;

import java.io.Serializable;
import java.time.LocalDate;

public class SpentRecord extends Record implements Comparable<SpentRecord>, Serializable {
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
    public String getSeller(){return seller;}
    public void setSeller(String seller){this.seller = seller;}
    public String getCategory(){return category;}
    public void setCategory(String category){this.category = category;}
}
