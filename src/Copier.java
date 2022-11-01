package src;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import src.ConfigPackage.ConfigHandler;
import src.FileBackupNum.FileHandler;


public class Copier {
    Boolean ENABLE_COPY;
    
    ConfigHandler cHandler;
    StringBuilder placementDirectories;

    /** Used to store the values of working directories that will be used to copy the new file. Holds the string for the .. name. */
    String currentParent;

    /** Holds the int value for it's location in the absolutePath()'s substring. It's used to adjust the currentParent's value recursively */
    int parentStart;

    /** Incremented every time a file is successfully copied. */
    int filesCopied; 
    /** Incremented every time a file is scanned, regardless of it's copy status. */
    int filesScanned;

    // TODO: This should be read from a configuration file or similar
    int max_files;

    /**  */
    public Copier(){
        this.cHandler = new ConfigHandler();
        this.placementDirectories = cHandler.readPlacementDirectory();
        this.filesCopied = 0;
        this.filesScanned = 0;
        this.max_files = 5;
        this.ENABLE_COPY = true;
    }

    public Copier(boolean copyStatus){
        this();
        this.ENABLE_COPY = copyStatus;

    }

    /**
     * Main function of the class. It goes.
     * @throws IOException Bad access
     */
    public void go() throws IOException {
        StringBuilder copyFromDirectories = cHandler.getDirectories();

        for (String s : placementDirectories.toString().split("\n")) {
            // System.out.println(s);'
            if (new File(s).listFiles().length < this.max_files) 
                // it needs to build up more folders
                // Changing max_files here is an easy solution to the problem.
                // TODO: Consider something better
                this.max_files = new File(s).listFiles().length+1;
            else
                if (ENABLE_COPY)
                    FileHandler.renameAllFiles(new File(s));
            s = s + File.separator + this.max_files;

            System.out.println("Placing into directory: " + s);
            File sFile = new File(s);
            if (ENABLE_COPY)
                if (!sFile.exists())
                    sFile.mkdirs();
            for (String cpDirectories : copyFromDirectories.toString().split("\n")) {
                File cpDir = new File(cpDirectories);
                int lastSlash = cpDirectories.lastIndexOf(File.separator);
                String gameFolderName = cpDirectories.substring(lastSlash+1, cpDirectories.length());
                System.out.println("Scanning folder \"" + cpDir.toString() + "\"");
                this.currentParent = gameFolderName;
                this.parentStart = lastSlash+1;
                getLinksInDir(cpDir);
            }
        }
    }    
    /**
     * If passed a directory, the method will recursively find all other directories. If passed a file, will preform a copy matching user specification.
     * @param f A file or a Directory
     */
    private void getLinksInDir(File f) {
            // Hidden folders are often used as caches for programs. We'll just skipped those.
            if (f.isHidden()){
                System.out.println(f + " is hidden & was skipped.");
                this.filesScanned++;
                return;
            }
            // System.out.println("Scanning folder" + f.toString());
            if(f.isFile()){
                while (!f.toString().contains(this.currentParent)) // walk back folders if we are in a folder we shouldn't be, until we find the common folder and use that instead.
                    this.currentParent = this.currentParent.substring(0, this.currentParent.lastIndexOf(File.separator));
                prepareFileForCopy(f);
                }// System.out.println(f.getAbsolutePath());
            else{
                File files[];
                files = f.listFiles();
                this.currentParent = f.toString().substring(this.parentStart); // update parent directory as we go further into recursion
                for (int i = 0; i < files.length; i++) {
                    getLinksInDir(files[i]);
                }
            }
        
        
    }

    
    /**
     * Method that will create directories as needed and creates the string that the file will be copied to. It then passes that new string to the copy function, where the copy is made.
     * @param f File to be copied
     */
    private void prepareFileForCopy(File f) {
        int helper = f.toString().lastIndexOf(File.separator);
        for (String s : placementDirectories.toString().split("\n")) {
            // s will be made to hold the parent directory to the new file being made.
            s = s + File.separator + this.max_files;

            s = String.join(File.separator, s, this.currentParent); // eg. E:\Backup Program\Radeon ReLive\Final Fantasy XIV
            // System.out.println(s);
            File newFile = new File(s);
            if (!newFile.exists())
                if (ENABLE_COPY)
                    newFile.mkdirs(); // create all directories leading up to where the new file will exist

            String dest = String.join(File.separator, s, f.toString().substring(helper+1)); // This is the new location of the file, with the file name at the end

            actualCopy(f, new File(dest));            
            // else
            // System.out.println(f.toString() + " GOES TO " + dest);
        }

    }

    private void actualCopy(File src, File dest) {
        this.filesScanned++;
        
        try {
            if (this.ENABLE_COPY){
              Files.copy(src.toPath(), dest.toPath());
            //   System.out.println("File successfully copied: " + src.toString() + " to\t" + dest.toString());
            }
          this.filesCopied++; // keep this outside of the if statement. It will still count files for dry-runs.
        } catch (FileAlreadyExistsException e){
            // Error that will occur if a file has already been copied of the same name.
            // System.err.println("Files already exists in backup location: \"" + dest.toString() + "\"");
        } catch (Exception e) {
            System.err.println("Unexpected exception when coping files: \"" + src.toString() + "\" to " + dest.toString());
            e.printStackTrace();
            System.exit(-1);
            // throw new RuntimeException(e.getMessage(), e);
        }
        
    }


}
