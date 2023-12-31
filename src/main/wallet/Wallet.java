/**
 * this package has all the aspects of a main.wallet including its graphics
 */
package main.wallet;

import main.record.IncomeRecord;
import main.record.SpentRecord;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * each user has a main.wallet in which all the records are stored
 * beside credentials like username and password, it provides methods to store data on file
 * or retrieve from it
 * also accessing the users file in which all the users are listed and
 * modifying it like deleting a user or changing password are possible here
 */
public class Wallet implements Serializable {
    private final String owner;
    private final String username;
    private String password;
    private final List<IncomeRecord> incomeRecords;
    private final List<SpentRecord> spentRecords;
    private static final String PATH = System.getProperty("user.dir") + "/user-data/";
    private static final String USERS_FILE = "userPass.txt";

    public Wallet(String owner, String username, String password) {
        this.owner = owner;
        this.username = username;
        this.password = password;
        incomeRecords = new ArrayList<>();
        spentRecords = new ArrayList<>();
    }

    public void storeOnFile(Logger logger) {
        try {
            Path path = Paths.get(PATH);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        }catch (Exception exception){
            logger.severe("ERROR : could not create user-data directory");
        }

        String filePath = PATH + username + ".txt";

        try(FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream outFile = new ObjectOutputStream(file)
                ) {

            outFile.writeObject(this);

        } catch (IOException exception) {
            logger.severe("ERROR : could not write on file!");
        }
    }

    public static Wallet readFromFile(String username, Logger logger) {
        Wallet wallet = null;
        String filePath = PATH + username + ".txt";

        try(FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream inFile = new ObjectInputStream(file);
                ) {

            wallet = (Wallet) inFile.readObject();

        } catch (IOException ioException) {
            logger.severe("ERROR : file not found!");
        } catch (ClassNotFoundException classNotFoundException) {
            logger.severe("ERROR : class not found!");
        }
        return wallet;
    }

    public static void addToUserPassFile(String username, String password, Logger logger) {
        try {
            Path path = Paths.get(PATH);
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        }catch (Exception exception){
            logger.severe("ERROR : could not create user-data directory");
        }

        String filePath = PATH + USERS_FILE;

        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            bufferedWriter.write(username + ":" + password);
            bufferedWriter.newLine();

        } catch (IOException e) {
            logger.severe("ERROR : APPENDING TO USERS FILE : file was not found!");
        }
    }

    public static Map<String, String> readFromUserPassFile(Logger logger) {

        String filePath = PATH + USERS_FILE;
        Map<String, String> userPassMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    userPassMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            logger.warning("WARNING : userPass file was not found! new file created");
        }
        return userPassMap;
    }

    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getOwner() {
        return owner;
    }

    public List<IncomeRecord> getIncomeRecords() {
        return incomeRecords;
    }

    public List<SpentRecord> getSpentRecords() {
        return spentRecords;
    }

    public boolean resetPassword(String previousPass, String newPass, Logger logger) {
        if (previousPass.equals(password)) {
            password = newPass;
            String filePath = PATH + USERS_FILE;

            try(
                BufferedReader reader = new BufferedReader(new FileReader(filePath))
                    ) {

                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    String varUsername = parts[0].trim();
                    if (varUsername.equals(username)) {
                        line = username + ":" + newPass;
                    }
                    content.append(line).append("\n");
                }

                try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
                    writer.write(content.toString());
                }

            } catch (IOException e) {
                logger.severe("ERROR happened while modifying the content of the userPass file!");
            }
            return true;
        } else {
            return false;
        }
    }

    public void deleteWallet(Logger logger) {

        String userPassFilePath = PATH + USERS_FILE;
        Path userRecordsFilePath = Paths.get(PATH + username + ".txt");

        File userPassFile = new File(userPassFilePath);
        try(
                BufferedReader reader = new BufferedReader(new FileReader(userPassFile))
        ) {

            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String varUsername = parts[0].trim();
                if (!varUsername.equals(this.username)) {
                    content.append(line).append("\n");
                }
            }

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(userPassFile))){
                writer.write(content.toString());
                Files.delete(userRecordsFilePath);
            }

        } catch (IOException e) {
            logger.severe("Error on file modification while changing password!");
        }
    }
}
