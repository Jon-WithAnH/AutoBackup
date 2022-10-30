package src;

import java.io.IOException;

import src.ConfigPackage.ConfigHandler;
// import src.Copier;
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Beginning execution...");
        if (args.length > 0){
            ConfigHandler cHandler = new ConfigHandler();
            for (int i = 0; i < args.length-1; i++){
                if (args[0].equals("-h")){
                    printHelp();
                    return;
                }
                if (args[0].equals("-a")){
                        cHandler.appendDirectory(args[1], cHandler.directoriesToBackup);
                        return;
                }
                if (args[0].equals("-p")){ // stands for place
                    cHandler.appendDirectory(args[1], cHandler.placementDirectory);
                    return;
                }
                if (args[0].equals("-d")){
                    cHandler.deleteDirectory(args[1]);
                    return;
                }
            }
        }

        Copier c = new Copier();
        c.go();
        String copiedStatus = "were ";
        if (!c.ENABLE_COPY)
            copiedStatus = "would have been ";

        System.out.println("AutoBackup completed successfully: " + c.filesScanned + " file(s) scanned & " + c.filesCopied + " file(s) " + copiedStatus + "backed up.");
        
    }

    static void printHelp(){
        System.out.println("Help Options for program: AutoBackup\n\n" + 
                            "Program used to backup files/folders automatically. Make sure to set it up to autostart on boot. Only tested on windows.\n" +
                            "Available commands: -a, -h\n" + 
                            "AutoBackup -h: This menu\n" +
                            "Autobackup -a [path]: Adds the specified path to the automated backup. Saved directories are located in %APPDATA%\\AutoBackup\\Directories.txt\n" +
                            "Autobackup -p [path]: Adds the provided path to the placement directory. This is in addition to any other backup location already added." +  
                            "AutoBackup -d [path]: Removes the specified path from the automated backup. Existing files already backed up will not be affected.\n");

    }
}
