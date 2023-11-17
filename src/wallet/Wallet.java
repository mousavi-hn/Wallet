package wallet;

import record.IncomeRecord;
import record.SpentRecord;

import java.io.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.UUID;

public class Wallet implements Serializable {
    private final String ID;
    private final String owner;
    private final String username;
    private String password;
    NavigableSet<IncomeRecord> incomeRecords;
    NavigableSet<SpentRecord> spentRecords;

    public Wallet(String owner, String username, String password){
        this.ID = UUID.randomUUID().toString();
        this.owner = owner;
        this.username = username;
        this.password = password;
        incomeRecords = new TreeSet<>();
        spentRecords = new TreeSet<>();
    }

    public void storeOnFile(){
        try {
            FileOutputStream file = new FileOutputStream(username + ".txt");
            ObjectOutputStream outFile = new ObjectOutputStream(file);
            outFile.writeObject(this);
            outFile.close();
        }catch (IOException exception){
            System.out.println("ERROR : could not write on file!");
        }
    }
    public Wallet readFromFile(){
        Wallet wallet = null;
        try {
            FileInputStream file = new FileInputStream(username + ".txt");
            ObjectInputStream inFile = new ObjectInputStream(file);
            wallet = (Wallet)inFile.readObject();
            inFile.close();
        }catch (IOException ioException){
            System.out.println("ERROR : file not found!");
        }catch (ClassNotFoundException classNotFoundException){
            System.out.println("ERROR : class not found!");
        }return wallet;
    }

    public String getID(){return ID;}
    public String getOwner(){return owner;}
    public String getUsername(){return username;}
    public void resetPassword(String previousPass, String newPass){
        if(previousPass.equals(password)){ password = newPass; }
        else { System.out.println("Wrong Password!"); }
    }

    public void addIncomeRecord(IncomeRecord record){incomeRecords.add(record);}
    public void addSpentRecord(SpentRecord record){spentRecords.add(record);}
    public void deleteIncomeRecord(IncomeRecord record){incomeRecords.remove(record);}
    public void deleteSpentRecord(SpentRecord record){spentRecords.remove(record);}

    public NavigableSet<IncomeRecord> incomeRecordsBetweenTwoDates(LocalDate init, LocalDate end){
        return incomeRecords.subSet(new IncomeRecord(0, init),
                true, new IncomeRecord(0, end), true);
    }
    public NavigableSet<SpentRecord> spentRecordsBetweenTwoDates(LocalDate init, LocalDate end){
        return spentRecords.subSet(new SpentRecord(0, init),
                true, new SpentRecord(0, end), true);
    }
    public NavigableSet<IncomeRecord> incomeSortedBasedOnAmount(NavigableSet<IncomeRecord> treeSet){
        NavigableSet<IncomeRecord> sortedTreeSet =
                new TreeSet<>(Comparator.comparing(IncomeRecord :: getAmount));
        sortedTreeSet.addAll(treeSet);
        return sortedTreeSet;
    }
    public NavigableSet<SpentRecord> spentSortedBasedOnAmount(NavigableSet<SpentRecord> treeSet){
        NavigableSet<SpentRecord> sortedTreeSet =
                new TreeSet<>(Comparator.comparing(SpentRecord :: getAmount));
        sortedTreeSet.addAll(treeSet);
        return sortedTreeSet;
    }
    public NavigableSet<IncomeRecord> queryOnSource(String source){
        NavigableSet<IncomeRecord> sameSource = new TreeSet<>();
        for(IncomeRecord incomeRecord : incomeRecords){
            if(incomeRecord.getSource().equals(source)){sameSource.add(incomeRecord);}
        }return sameSource;
    }
    public NavigableSet<SpentRecord> queryOnSeller(String seller){
        NavigableSet<SpentRecord> sameSeller = new TreeSet<>();
        for(SpentRecord spentRecord : spentRecords){
            if(spentRecord.getSeller().equals(seller)){sameSeller.add(spentRecord);}
        }return sameSeller;
    }
    public NavigableSet<SpentRecord> queryOnCategory(String category){
        NavigableSet<SpentRecord> sameCategory = new TreeSet<>();
        for(SpentRecord spentRecord : spentRecords){
            if(spentRecord.getCategory().equals(category)){sameCategory.add(spentRecord);}
        }return sameCategory;
    }
}
