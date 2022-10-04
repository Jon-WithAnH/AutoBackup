package src;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import src.ConfigPackage.ConfigHandler;


public class Copier {
    Boolean ENABLE_COPY = true;
    
    ConfigHandler cHandler = new ConfigHandler();
    StringBuilder placementDirectories = cHandler.readPlacementDirectory();

    /** These variables are used the store the values of working directories that will be used to copy the new file. currentParents holds the string for the .. name.
     * Parent start holds the int value for it's location in the absolutePath()'s substring. It's used to adjust the currentParent's value as recursion is used.
     */
    
    String currentParent;
    int parentStart;

    /** Incremented every time a file is successfully copied. Keeps track for reporting to main. */
    int filesCopied = 0; 

    /**
     * Main function of the class. It goes.
     * @throws IOException Bad access
     */
    public void go() throws IOException {
        StringBuilder copyFromDirectories = cHandler.getDirectories();

        for (String s : placementDirectories.toString().split("\n")) {
            System.out.println("Placing into directory: " + s);
            File sFile = new File(s);
            if (!sFile.exists())
                sFile.mkdir();
            for (String cpDirectories : copyFromDirectories.toString().split("\n")) {
                File cpDir = new File(cpDirectories);
                int lastSlash = cpDirectories.lastIndexOf(File.separator);
                String gameFolderName = cpDirectories.substring(lastSlash+1, cpDirectories.length());

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
            File files[];
            if(f.isFile()){
                while (!f.toString().contains(this.currentParent)) // walk back folders if we are in a folder we shouldn't be, until we find the common folder and use that instead.
                    this.currentParent = this.currentParent.substring(0, this.currentParent.lastIndexOf(File.separator));
                prepareFileForCopy(f);
                }// System.out.println(f.getAbsolutePath());
            else{
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
        // int lastSlash = f.toString().lastIndexOf("\\");
        for (String s : placementDirectories.toString().split("\n")) {
            // s will be made to hold the parent directory to the new file being made.
            s = String.join(File.separator, s, this.currentParent); // eg. E:\Backup Program\Radeon ReLive\Final Fantasy XIV
            // System.out.println(s);
            File newFile = new File(s);
            if (!newFile.exists())
                newFile.mkdirs(); // create all directories leading up to where the new file will exist

            String dest = String.join(File.separator, s, f.toString().substring(helper+1)); // This is the new location of the file, with the file name at the end

            if (this.ENABLE_COPY)
                actualCopy(f, new File(dest));            
            else
                System.out.println(f.toString() + " GOES TO " + dest);
        }

    }

    private void actualCopy(File src, File dest) {
        try {
          Files.copy(src.toPath(), dest.toPath());
          this.filesCopied++;
          System.out.println("File successfully copied: " + src.toString());
        } catch (FileAlreadyExistsException e){
            // Common error that will occur if a file has already been copied of the same name.
            // System.err.println("Files already exists in backup location: \"" + dest.toString() + "\"");
        } catch (Exception e) {
            System.err.println("Unexpected exception when coping files: \"" + src.toString() + "\" and " + dest.toString());
            throw new RuntimeException(e.getMessage(), e);
        }
        
    }


}
