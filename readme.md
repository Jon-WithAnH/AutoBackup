# AutoBackup

## About

Autobackup's purpose is to allow for automated backing up of selected folders. The user can choose any amount of folders and the program will go through them in order of first added to last added, and place them in a directory that the user initally provides (the provided directory is persistant). The user can choose how many backups to keep and if they want it stored redundantly (on multiple devices). 

To run from cmd: 
```
java -jar AutoBackup.jar [args]
```

## Roadmap

- [x] Store list of directories to backup
- [x] Have user provide directory(s) to backup to
- [ ] Backup Types
  - [x] Files
  - [ ] Folders
- [ ] Mode changing
  - [ ] Mode to allow for an iterative backup that doesn't store multiple copies of backups. Just unique files
  - [ ] Mode to backup all given files n number of times


## Self Notes:

Might want to think of some sort of <b>initalizition</b>. This would include <b>Mode Selection</b> and <b>the location of the backup directory(s)</b>. Maybe using the arg /i

