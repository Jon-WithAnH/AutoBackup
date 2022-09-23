package src.ConfigPackage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ConfigHandlerTest {
    ConfigHandler cHandler;
    @Before
    public void setUp(){
        this.cHandler = new ConfigHandler();
    }

    @Test
    public void testCreateFolderWithDefaults() {

    }

    @Test
    public void testAppendDirectory() throws IOException {
        assertTrue(cHandler.appendDirectory("C:\\Users\\avera\\OneDrive\\Documents\\Rockstar Games\\Red Dead Redemption 2", cHandler.directoriesToBackup));
        assertTrue(cHandler.deleteDirectory("C:\\Users\\avera\\OneDrive\\Documents\\Rockstar Games\\Red Dead Redemption 2"));
    }

    @Test
    public void testBetterReadAllText() {

    }

    @Test
    public void testDeleteDirectory() {

    }

    @Test
    public void testReadAllText() {
        cHandler.readPlacementDirectory();
        cHandler.getDirectories();
        // assertFalse("message", true);
    }

    @Test
    public void testWriteDefault() {
        assertFalse("Test failed using an invalid directory", this.cHandler.appendDirectory("C:\\Invalid44681618545", cHandler.directoriesToBackup));
    }
}
