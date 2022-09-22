package src;

import java.io.IOException;

import src.ConfigPackage.ConfigHandler;
// import src.Copier;
public class Main {
    public final Boolean TESTING = false;
    public static void main(String[] args) throws IOException {
        System.out.println("Beginning execution...");
        if (args.length > 0){
            for (int i = 0; i < args.length-1; i++)
                // System.out.println(args[i]);
                if (args[0].equals("-h")){
                    printHelp();
                    return;
                }
                if (args[0].equals("-a")){
                        ConfigHandler cHandler = new ConfigHandler();
                        cHandler.appendDirectory(args[1]);
                        return;
                }
                if (args[0].equals("-d")){
                    ConfigHandler cHandler = new ConfigHandler();
                    cHandler.deleteDirectory(args[1]);
                    return;
            }
        }
        // ConfigHandler cHandler = new ConfigHandler();
        // cHandler.createPlacementDirectory();
        // System.out.println(cHandler.readPlacementDirectory());
        // cHandler.readPlacementDirectory();
        // cHandler.getDirectories();
        // cHandler.readAllText(filepath)
        // cHandler.appendDirectory("C:\\Invalid");
        Copier c = new Copier();
        // c.betterCopy();
        // c.copy();
        c.go();
        System.out.println("AutoBackup completed successfully.");
        // c.runCopyFile();
        
    }

    static void printHelp(){
        System.out.println("Help Options for program: AutoBackup\n\n" + 
                            "Program used to backup files/folders automatically. Make sure to set it up to autostart on boot. Only tested on windows.\n" +
                            "Available commands: -a, -h\n" + 
                            "AutoBackup -h: This menu\n" +
                            "Autoback -a [path]: Adds the specified path to the automated backup. Saved directories are located in %APPDATA%\\AutoBackup\\Directories.txt\n" + 
                            "AutoBackup -d [path]: Removes the specified path from the automated backup. Existing files already backed up will not be affected.\n");

    }
}
