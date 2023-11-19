package wallet;

import record.IncomeRecord;
import record.SpentRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

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
            String currentDirectory = System.getProperty("user.dir");
            String userFile = "/user-data/" + username + ".txt";
            String filePath = currentDirectory + userFile;
            FileOutputStream file = new FileOutputStream(filePath);
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
            String currentDirectory = System.getProperty("user.dir");
            String userFile = "/user-data/" + username + ".txt";
            String filePath = currentDirectory + userFile;
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream inFile = new ObjectInputStream(file);
            wallet = (Wallet)inFile.readObject();
            inFile.close();
        }catch (IOException ioException){
            System.out.println("ERROR : file not found!");
        }catch (ClassNotFoundException classNotFoundException){
            System.out.println("ERROR : class not found!");
        }return wallet;
    }

    public static void addToUserPassFile(String username, String password){
        String currentDirectory = System.getProperty("user.dir");
        String userPassFile = "/user-data/userPass.txt";
        String filePath = currentDirectory + userPassFile;
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Append the new data to the file
            bufferedWriter.write(username + ":" + password);
            bufferedWriter.newLine();

        } catch (IOException e) {
            System.out.println("ERROR : APPENDING TO USERS FILE : file was not found!");
        }
    }

    public static Map<String, String> readFromUserPassFile(){
        String currentDirectory = System.getProperty("user.dir");
        String userPassFile = "/user-data/userPass.txt";
        String filePath = currentDirectory + userPassFile;
        Map<String, String> userPassMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) { userPassMap.put(parts[0], parts[1]); }
            }
        } catch (IOException e) {
            System.out.println("ERROR : file was not found!");
        }return userPassMap;
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
