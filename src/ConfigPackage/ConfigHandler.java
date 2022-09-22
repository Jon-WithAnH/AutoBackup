package src.ConfigPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System;

public class ConfigHandler {
    String appdataLocation;
    String autoBackupFolder;
    String fullFilePath;

    String placementDirectory;

    public ConfigHandler(){
        this.appdataLocation = System.getenv("APPDATA");
        this.autoBackupFolder = appdataLocation + File.separator + "AutoBackup";
        this.fullFilePath = appdataLocation + File.separator + "AutoBackup" + File.separator + "Directories.txt";
        this.placementDirectory = this.autoBackupFolder + File.separator + "PlacementLocations.txt";
    }

    public void createPlacementDirectory(){
        File folder = new File(this.placementDirectory);
        try{
            if (folder.createNewFile())
                System.out.println(folder + " created");
        } catch (IOException ex) {}
    }

    public StringBuilder readPlacementDirectory(){
        return readAllText(this.placementDirectory);
    }

    public void CreateFolderWithDefaults() {

        // TODO: Use a global varable to change the name of the file for easier maintinence. 
        File folder = new File(this.autoBackupFolder);
        if (folder.mkdir())
            System.out.println(folder + " created");
        
        // System.out.println(fileName);
        File f = new File(this.fullFilePath);
        try {
            if (f.createNewFile()){
                System.err.println("File \"" + this.fullFilePath + "\" created");
                writeDefault();
            } else {System.out.println("Already Exists");}   
        } catch (Exception e) {
            System.err.println("FATAL ERROR GENERATED IN ConfigHandler.CreateFolderWithDefaults()");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    void writeDefault(){
        try {
            FileWriter myWriter = new FileWriter(this.fullFilePath);
            myWriter.write("C:\\Users\\avera\\Videos\\Radeon ReLive");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    public boolean appendDirectory(String directoryToAdd){
        File f = new File(directoryToAdd);
        if (!f.exists()){
            System.err.println("Usage: Directory provided is not found: " + directoryToAdd);
            return false;
        }
        try {
            FileWriter fw = new FileWriter(this.fullFilePath, true);
            fw.write(directoryToAdd + "\n");
            fw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // System.err.println("Error ");
            // TODO: handle exception
        }
        return false;
    }
    public StringBuilder getDirectories(){
        return readAllText(this.fullFilePath);
    }
    public StringBuilder readAllText(String filepath) {
        StringBuilder sb = new StringBuilder();
        try{
            // Files.lines(Paths.get(filepath)).forEach(sb::append);
            // sb.append(Files.lines(Paths.get(filepath)));
            BufferedReader br1 = new BufferedReader(new FileReader(filepath));
            String readline = br1.readLine();
            while (readline != null){
                sb.append(readline + "\n");
                readline = br1.readLine();
            }
            sb.deleteCharAt(sb.length()-1); // remove the last \n
            br1.close();
            // sb.append
        }
        catch (IOException exception){
            exception.printStackTrace();

        }
        // System.out.print(sb.toString());
        return sb;
    }
    public boolean deleteDirectory(String directoryToRemove) throws IOException {
        boolean deleted = false;
        // StringBuilder sb = new StringBuilder();
        // PrintWriter object for output.txt
        PrintWriter pw = new PrintWriter(this.autoBackupFolder + File.separator + "tmp.txt");
        // BufferedReader object for input.txt
        BufferedReader br1 = new BufferedReader(new FileReader(this.fullFilePath));
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
        // TODO: Make tmp.txt the new Directories.txt
        br1.close();
        pw.close();
        if (!deleted) // no point in continues if nothing was deleted
            return deleted;
        // rename new new file to directories.txt
        File f1 = new File(this.fullFilePath);
        f1.delete();
        File f = new File(this.autoBackupFolder + File.separator + "tmp.txt");
        try{f.renameTo(f1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return deleted;
    }
}
