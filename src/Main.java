package src;

import java.io.IOException;

import src.ConfigPackage.ConfigHandler;

public class Main {
    static boolean DRY_RUN_FLAG = false;
    public static void main(String[] args) throws IOException {
        if (argsHandler(args))
            return; // Handle args request and exits if needed
        System.out.println("Beginning execution...");
        Copier c;
        String copiedStatus; // Handles grammar
        if (DRY_RUN_FLAG){
            c = new Copier(false);
            copiedStatus = "would have been";
            }
        else{
            c = new Copier();
            copiedStatus = "were";
            }

        c.go();
        System.out.println("AutoBackup completed successfully: " + c.filesScanned + " file(s) scanned & " + c.filesCopied + " file(s) " + copiedStatus + " backed up.");
        
    }

    static boolean argsHandler(String[] args)throws IOException{
        if (args.length > 0){
            ConfigHandler cHandler = new ConfigHandler();
            switch (args[0]) {
                case "-a":
                    cHandler.appendDirectory(args[1], cHandler.directoriesToBackup);
                case "-p": // stands for placement
                    cHandler.appendDirectory(args[1], cHandler.placementDirectory);
                case "-d":
                    cHandler.deleteDirectory(args[1]);
                case "-ap":
                    cHandler.appendDirectory(args[1], cHandler.directoriesToBackup);
                    cHandler.appendDirectory(args[2], cHandler.placementDirectory);
                case "-ad":
                    cHandler.appendDirectory(args[1], cHandler.directoriesToBackup);
                    cHandler.deleteDirectory(args[2]);
                case "-adp":
                    cHandler.appendDirectory(args[1], cHandler.directoriesToBackup);
                    cHandler.deleteDirectory(args[2]);
                    cHandler.appendDirectory(args[3], cHandler.placementDirectory);
                case "-dp":
                    cHandler.deleteDirectory(args[1]);
                    cHandler.appendDirectory(args[2], cHandler.placementDirectory);
                case "--dry-run":
                    System.out.println("============ Dry-run initalized ============");
                    DRY_RUN_FLAG = true;
                    return false;
                default:
                    printHelp();
            }
            return true;
        }
        return false;
    }

    static void printHelp(){
        System.out.println("Help Options for program: AutoBackup\n\n" + 
                            "Program used to backup files/folders automatically. Optional to set it up to autostart on boot. Compatable on linux and windows machines\n" +
                            "Available commands: -a, -d, -p, -h, --dry-run\n" + 
                            "AutoBackup -h: This menu\n" +
                            "Autobackup -a [path]: Adds the specified path to the automated backup. Saved directories are located in %APPDATA%\\AutoBackup\\Directories.txt\n" +
                            "AutoBackup -d [path]: Removes the specified path from the automated backup. Existing files already backed up will not be affected.\n" +  
                            "AutoBackup -p [path]: Adds the provided path to the placement directory. This is in addition to any other backup location already added.\n" +
                            "AutoBackup --dry-run: AutoBackup will preform normally, but won't back up any files.\n" +
                            "This program also accepts any alphabetical combination of single character, non -h commands.\n" +
                            "AutoBackup -adp [a-path] [d-path] [p-path]: [a-path] is equivilent to -a's path and so on");

    }
}
