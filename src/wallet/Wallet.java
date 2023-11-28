package wallet;

import record.IncomeRecord;
import record.SpentRecord;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

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
            logger.severe("ERROR : file was not found!");
        }
        return userPassMap;
    }

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
            File file = new File(filePath);

            try(BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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

                writer.write(content.toString());

            } catch (IOException e) {
                logger.severe("Error on file modification while changing password!");
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
                BufferedReader reader = new BufferedReader(new FileReader(userPassFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(userPassFile))
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

            writer.write(content.toString());
            Files.delete(userRecordsFilePath);
        } catch (IOException e) {
            logger.severe("Error on file modification while changing password!");
        }
    }
}
