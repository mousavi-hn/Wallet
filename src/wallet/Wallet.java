package wallet;

import record.IncomeRecord;
import record.SpentRecord;

import java.io.*;
import java.util.*;

public class Wallet implements Serializable {
    private final String owner;
    private final String username;
    private String password;
    private final List<IncomeRecord> incomeRecords;
    private final List<SpentRecord> spentRecords;

    public Wallet(String owner, String username, String password){
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

    public String getOwner(){return owner;}
    public List<IncomeRecord> getIncomeRecords(){return incomeRecords;}
    public List<SpentRecord> getSpentRecords(){return spentRecords;}
    public boolean resetPassword(String previousPass, String newPass){
        if(previousPass.equals(password)){
            password = newPass;
            try {
                String currentDirectory = System.getProperty("user.dir");
                String userPassFile = "/user-data/userPass.txt";
                String filePath = currentDirectory + userPassFile;
                File file = new File(filePath);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    String username = parts[0].trim();
                    if (username.equals(this.username)) { line = username + ":" + newPass; }
                    content.append(line).append("\n");
                }reader.close();

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(content.toString());
                writer.close();

            } catch (IOException e) {
                System.out.println("Error on file modification while changing password!");
            } return true;
        }
        else { return false; }
    }

    public boolean deleteWallet(){
        try {
            String currentDirectory = System.getProperty("user.dir");
            String userPassFile = "/user-data/userPass.txt";
            String filePath = currentDirectory + userPassFile;
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String username = parts[0].trim();
                if (!username.equals(this.username)) { content.append(line).append("\n"); }
            }reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content.toString());
            writer.close();
            return file.delete();
        } catch (IOException e) {
            System.out.println("Error on file modification while changing password!");
            return false;
        }
    }
}
