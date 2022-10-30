package src.ConfigPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System;
import java.util.Scanner;


public class ConfigHandler {
    String appdataLocation;
    String autoBackupFolder;
    public String directoriesToBackup;

    public String placementDirectory;
    /** Used to prompt user for inputs into the two files to determine AutoBackup's configuration */
    Scanner scanner;

    public ConfigHandler(){
        this.appdataLocation = System.getenv("APPDATA");
        if (this.appdataLocation == null) // occurs when user OS is linux
            this.appdataLocation = System.getProperty("user.home") + File.separator + ".local" + File.separator + "share";
        this.autoBackupFolder = appdataLocation + File.separator + "AutoBackup";
        this.directoriesToBackup = appdataLocation + File.separator + "AutoBackup" + File.separator + "Directories.txt";
        this.placementDirectory = this.autoBackupFolder + File.separator + "PlacementLocations.txt";
        if (createFolder()){
            System.out.println("First time setup initialized. Thank you for using AutoBackup!");
            scanner = new Scanner(System.in);
            createPlacementDirectory();
            createBackupDirectories();
            scanner.close();
        }
    }

    /**
     * Creates PlacementLocations.txt and prompts user for the desired saved location
     */
    void createPlacementDirectory(){
        // if doesn't exist
            // ask user for desired backup location
            // show user what they entered and confirm
        File folder = new File(this.placementDirectory); // auto backup folder must be created first
        try{
            if (folder.createNewFile()){
                System.out.println("Please provide the directory where the backup's should be placed (note: does not need to be an existing directory): ");
                String location = this.scanner.nextLine();
                while (!verifyDriveLetter(location)) {
                    System.out.println("Invalid drive letter and/or format provided. Please make sure the drive is connected and that it was entered correctly.");
                    location = scanner.nextLine();
                }
                // scan.close();
                appendDirectory(location, this.placementDirectory);
            
            }
        } catch (IOException ex) {
            System.err.println("Error generating PlacementDirectory.txt");
            ex.printStackTrace();
            System.exit(-1);
            // throw new IOException(ex.getMessage());
        }
    }

    /**
     * Checks if the drive letter of the given directory is valid or not.
     * @param t Directory of which's drive letter to check.
     * @return True if the drive letter is valid. False is not.
     */
    public Boolean verifyDriveLetter(String t) {
        // String t = "G:\\Users\\avera\\OneDrive\\Documents\\My Games\\FINAL FANTASY XIV - A Realm Reborn";
        StringBuilder sb = new StringBuilder();
        sb.append(t.charAt(0));
        for (int i = 1; i < t.length(); i++) { // Loop will continue until the second instance of an 
            // System.out.println(t.charAt(i));
            if (Character.toLowerCase(t.charAt(i)) - 'a' >= 0 && Character.toLowerCase(t.charAt(i)) - 'z' <= 26){
                break;
            }
            sb.append(t.charAt(i));

        }
        File f = new File(sb.toString());
        if (f.exists())
            return true;
        return false;
    }

    public StringBuilder readPlacementDirectory(){
        return readAllText(this.placementDirectory);
    }
    /**
     * Creates the install directory the program will run in and the program's config files will be places in.
     * @return True if the folder is created. This is synonmous with it being the user's first time running the programs. False if otherwise.
     */
    private boolean createFolder(){
        File folder = new File(this.autoBackupFolder);
        return folder.mkdir();
    }
    /**
     * Creates Directories.txt
     */
    public void createBackupDirectories() {
        File f = new File(this.directoriesToBackup);
        // TODO: Func should prompt user for first entry. This is to keep it consistant with #createPlacementDirectory(). 
        // Right now, it's handled somewhere else in the file.
        try {
            if (f.createNewFile()){
                System.err.println("File \"" + this.directoriesToBackup + "\" created");
                writeDefault();
            }   
        } catch (Exception e) {
            System.err.println("FATAL ERROR GENERATED IN ConfigHandler.CreateFolderWithDefaults()");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Handles validation of user input. 
     * Makes sure that the file/folder exists before it gets added. If it doesn't exist, prompts user infinitely until a valid entry is given.
     * @return Path to the folder/file
     */
    String validateFileFolder(){
        File verification = new File(scanner.nextLine());

        while (!verification.exists()){
            System.out.println("Folder/File not found: " + verification);
            System.out.println("Please enter a valid folder/file location: ");
            scanner = new Scanner(System.in);
            verification = new File(scanner.next());

        }
        return verification.toString();
    }

    void writeDefault(){
        try {
            FileWriter myWriter = new FileWriter(this.directoriesToBackup);
            System.out.println("Please enter a directory to backup:");
            String fileToWrite = validateFileFolder();

            myWriter.write(fileToWrite);
            myWriter.close();
            System.out.println("Successfully wrote to the file: " + fileToWrite);
          } catch (Exception e) {
            System.err.println("Error in ConfigHandler.writeDefault()");
            e.printStackTrace();
            System.exit(-1);
          }
    }

    public boolean appendDirectory(String directoryToAdd, String whichDirectory){
        assert whichDirectory == this.directoriesToBackup || whichDirectory == this.placementDirectory: "whichDirectory must match either directories.txt or placementlocations.txt";

        File f = new File(directoryToAdd);
        if (whichDirectory.equals(this.directoriesToBackup)) // we don't care if the directory doesn't exist where the backup is going to be made. That will be created later.
            if (!f.exists()){
                System.err.println("Usage: Directory provided is not found: " + f.toString());
                return false;
            }
        try {
            FileWriter fw = new FileWriter(whichDirectory, true);
            fw.write(directoryToAdd + "\n");
            fw.close();
            System.out.println("Successfully wrote to file: " + directoryToAdd);
            return true;
        } catch (Exception e) {
            System.err.println("Error appending directory: " + directoryToAdd);
            e.printStackTrace();
        }
        return false;
    }
    public StringBuilder getDirectories(){
        return readAllText(this.directoriesToBackup);
    }
    public StringBuilder readAllText(String filepath) {
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader br1 = new BufferedReader(new FileReader(filepath));
            String readline = br1.readLine();
            while (readline != null){
                sb.append(readline + "\n");
                readline = br1.readLine();
            }
            sb.deleteCharAt(sb.length()-1); // remove the last \n
            br1.close();
        } catch (IOException exception){
            System.err.println("Failed to read data from file: " + filepath);
            exception.printStackTrace();

        }
        return sb;
    }

    /**
     * Method that will remove a given directory from Directories.txt. 
     * If it makes a deletion, it will create a new file, copy all other data (aside from deletion), delete the old file, and renew the new file to the same name as the old one.
     * @param directoryToRemove The directory to remove that exists in the file that holds the folders/files to backup
     * @return True if there was a deletion (IE., it was found within the file). False is no deletion occured.
     * @throws IOException Bad access
     */
    public boolean deleteDirectory(String directoryToRemove) throws IOException {
        boolean deleted = false;
        // StringBuilder sb = new StringBuilder();
        // PrintWriter object for output.txt
        PrintWriter pw = new PrintWriter(this.autoBackupFolder + File.separator + "tmp.txt");
        // BufferedReader object for input.txt
        BufferedReader br1 = new BufferedReader(new FileReader(this.directoriesToBackup));
        String readline = br1.readLine();
        while (readline != null){ // read through the file line by line and look for the input to remove
            System.out.println(readline);
            if (readline.equals(directoryToRemove)){
                deleted = true; // set the flag and don't write to the new file
                readline = br1.readLine();
                continue;
            }
            pw.write(readline + "\n");
            readline = br1.readLine();
        }
        pw.flush();
        br1.close();
        pw.close();
        if (!deleted) // no point in continues if nothing was deleted
            return deleted;
        // rename new new file to directories.txt
        File f1 = new File(this.directoriesToBackup);
        f1.delete();
        File f = new File(this.autoBackupFolder + File.separator + "tmp.txt");
        try{f.renameTo(f1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return deleted;
    }
}
