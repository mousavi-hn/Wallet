package wallet;

import record.IncomeRecord;
import record.Record;
import record.SpentRecord;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Wallet implements Serializable {
    private final String ID;
    private final String owner;
    private final String username;
    private String password;
    private final List<IncomeRecord> incomeRecords;
    private final List<SpentRecord> spentRecords;

    public Wallet(String owner, String username, String password){
        this.ID = UUID.randomUUID().toString();
        this.owner = owner;
        this.username = username;
        this.password = password;
        incomeRecords = new ArrayList<>();
        spentRecords = new ArrayList<>();
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

    public static Wallet readFromFile(String username){
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
    public List<IncomeRecord> getIncomeRecords(){return incomeRecords;}
    public List<SpentRecord> getSpentRecords(){return spentRecords;}
    public void resetPassword(String previousPass, String newPass){
        if(previousPass.equals(password)){ password = newPass; }
        else { System.out.println("Wrong Password!"); }
    }

    public List<IncomeRecord> incomeRecordsBetweenTwoDates(LocalDate init, LocalDate end){
        List<IncomeRecord> subList = new ArrayList<>();
        if(init != null && end != null){
            for(IncomeRecord incomeRecord : incomeRecords) {
                if ((incomeRecord.getDate().isAfter(init) || incomeRecord.getDate().isEqual(init))
                        && (incomeRecord.getDate().isBefore(end) || incomeRecord.getDate().isEqual(end))) {
                    subList.add(incomeRecord);
                }
            }
        }else if(init == null) {
            for (IncomeRecord incomeRecord : incomeRecords) {
                if ((incomeRecord.getDate().isBefore(end) || incomeRecord.getDate().isEqual(end))) {
                    subList.add(incomeRecord);
                }
            }
        }else{
            for(IncomeRecord incomeRecord : incomeRecords) {
                if ((incomeRecord.getDate().isAfter(init) || incomeRecord.getDate().isEqual(init)) ) {
                    subList.add(incomeRecord);
                }
            }
        }
        return subList;
    }
    public List<SpentRecord> spentRecordsBetweenTwoDates(LocalDate init, LocalDate end){
        List<SpentRecord> subList = new ArrayList<>();
        if(init != null && end != null){
            for(SpentRecord spentRecord : spentRecords) {
                if ((spentRecord.getDate().isAfter(init) || spentRecord.getDate().isEqual(init))
                        && (spentRecord.getDate().isBefore(end) || spentRecord.getDate().isEqual(end))) {
                    subList.add(spentRecord);
                }
            }
        }else if(init == null) {
            for (SpentRecord spentRecord : spentRecords) {
                if ((spentRecord.getDate().isBefore(end) || spentRecord.getDate().isEqual(end))) {
                    subList.add(spentRecord);
                }
            }
        }else{
            for(SpentRecord spentRecord : spentRecords) {
                if ((spentRecord.getDate().isAfter(init) || spentRecord.getDate().isEqual(init)) ) {
                    subList.add(spentRecord);
                }
            }
        }
        return subList;
    }
    public List<IncomeRecord> incomeSortedBasedOnAmount(List<IncomeRecord> incomeList){
        incomeList.sort(Comparator.comparingDouble(IncomeRecord :: getAmount));
        return incomeList;
    }
    public List<SpentRecord> spentSortedBasedOnAmount(List<SpentRecord> spentList){
        spentList.sort(Comparator.comparingDouble(SpentRecord :: getAmount));
        return spentList;
    }
    public List<IncomeRecord> queryOnSource(String source){
        List<IncomeRecord> subList = new ArrayList<>();
        for(IncomeRecord incomeRecord : incomeRecords){
            if (incomeRecord.getSource().equals(source)){subList.add(incomeRecord);}
        }
        return subList;
    }
    public List<SpentRecord> queryOnSeller(String seller){
        List<SpentRecord> subList = new ArrayList<>();
        for(SpentRecord spentRecord : spentRecords){
            if (spentRecord.getSeller().equals(seller)){subList.add(spentRecord);}
        }
        return subList;
    }
    public List<SpentRecord> queryOnCategory(String category){
        List<SpentRecord> subList = new ArrayList<>();
        for(SpentRecord spentRecord : spentRecords){
            if (spentRecord.getCategory().equals(category)){subList.add(spentRecord);}
        }
        return subList;
    }
    public List<IncomeRecord> incomeRecordsQueryOnRate(Record.Rate rate){
        List<IncomeRecord> subList = new ArrayList<>();
        for(IncomeRecord incomeRecord : incomeRecords){
            if (incomeRecord.getRate().equals(rate)){subList.add(incomeRecord);}
        }
        return subList;
    }
    public List<SpentRecord> spentRecordsQueryOnRate(Record.Rate rate){
        List<SpentRecord> subList = new ArrayList<>();
        for(SpentRecord spentRecord : spentRecords){
            if (spentRecord.getRate().equals(rate)){subList.add(spentRecord);}
        }
        return subList;
    }
}
