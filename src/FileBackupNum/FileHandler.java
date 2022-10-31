package src.FileBackupNumTest;

import java.io.File;

public class FileHandler {
    /* Class needs the following functionalities:
     *  get most recent folder
     *  delete oldest dir
     *  rename all the folders so that 1 is always the youngest, and n is always the oldest (?)
     */

    public static void main(String[] args) {
        // getOldestFileInDir(new File("src/FileBackupNumTest/dirs"));
        renameAllFiles(new File("src/FileBackupNumTest/dirs"));
    }
    public FileHandler(){
        // Needs file PlacementLocations.txt
    }


    /**
     * Method will search all directories in given directory and return the most recent file using 
     * the lastModified() timestamp of the directory.
     * @param f A Directory
     * @throws NullPointerException If passed an empty directory, or a directory that doesn't contain
     * directories, a null exception will be thrown.
     */
    private static File getOldestFileInDir(File f) throws NullPointerException {
        File[] files = f.listFiles();

        long min = Long.MAX_VALUE;
        File oldestFile = null;

        for (File iFile : files){
            if (iFile.isDirectory() && iFile.lastModified() < min){
                // System.out.println(iFile.toString() + "\t" + iFile.lastModified());
                min = iFile.lastModified();
                oldestFile = iFile;
            }
        }
        if (oldestFile == null)
            throw new NullPointerException("ERROR: No files were found in location \"" + f.toString() + "\"");
        System.out.println("Oldest folder: " + oldestFile.toPath());
        return oldestFile;
    }

    /**
     * Scans folder given by arg. It will scan for the amount of subfolders that exist, deleteing the oldest folder (ie, File("./1"))
     * and shift all existing folder names up by one digit. <br><br>
     * ie:
     * 1 - deleted,
     * 2 -> 1,
     * 3 -> 2,
     * 4 -> 3,
     * and so on
     * @param f Parent folder to be scanned
     * @throws FileNotFoundException if there are no subfolders within f
     */
    public static void renameAllFiles(File f) {
        int filesLength = f.listFiles().length;
        String s = null;
        // System.out.println("Heelo from: " + f.toString());
        if (filesLength < 2){
            // throw new FileNotFoundException("Not enough files to delete and rename");
            System.out.println("Not enough files to rename");
            return;
            
        } 
        // Thie first file is the only one we need to delete, so we'll handle that outside the loop
        File hm = new File((f.toString() + File.separator + 1));
        s = hm.toString();
        deleteAllFiles(hm);
        if (!hm.delete()){
            System.out.println("ERROR: Folder must be empty \"" + hm.toString() + "\"");
            return;
        }
        // then iterate through all the remaining folders renaming them as we go
        for (int i = 2; i <= filesLength; i++) {
            hm = new File((f.toString() + File.separator + i));
            String tmp = hm.toString();
            File sFile = new File(s);
            s = tmp;
            hm.renameTo(sFile);
        }
    }

    private static void deleteAllFiles(File f){
        if (f.isFile()){
            f.delete();
            return;
        }
        File[] files = f.listFiles();
        for (File eachFile : files){
            deleteAllFiles(eachFile);
            eachFile.delete();
        }
        
    }
}
