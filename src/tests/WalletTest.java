package tests;

import wallet.Wallet;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

public class WalletTest {

    private static final Logger LOGGER = Logger.getLogger(WalletTest.class.getName());
    private Wallet wallet;

    @Before
    public void setUp() {
        wallet = new Wallet("John Doe", "john_doe", "password123");
    }

    @Test
    public void testStoreOnFile() {
        wallet.storeOnFile(LOGGER);

        File file = new File(System.getProperty("user.dir") + "/user-data/john_doe.txt");
        assertTrue(file.exists());
    }

    @Test
    public void testReadFromFile() {
        wallet.storeOnFile(LOGGER);

        Wallet readWallet = Wallet.readFromFile("john_doe", LOGGER);

        assertNotNull(readWallet);
        assertEquals(wallet.getOwner(), readWallet.getOwner());
        assertEquals(wallet.getUsername(), readWallet.getUsername());
        assertEquals(wallet.getPassword(), readWallet.getPassword());
    }

    @Test
    public void testAddToUserPassFile() {
        Wallet.addToUserPassFile("john_doe", "password123", LOGGER);

        Map<String, String> userPassMap = Wallet.readFromUserPassFile(LOGGER);

        assertTrue(userPassMap.containsKey("john_doe"));
        assertEquals("password123", userPassMap.get("john_doe"));
    }

    @Test
    public void testResetPassword() {
        assertTrue(wallet.resetPassword("password123", "newPassword", LOGGER));
        assertEquals("newPassword", wallet.getPassword());

        assertFalse(wallet.resetPassword("wrongPassword", "newPassword", LOGGER));
        assertEquals("newPassword", wallet.getPassword()); // Password should not be changed
    }
}
