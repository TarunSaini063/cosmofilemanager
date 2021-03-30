package win95.model;

import win95.constants.FileType;
import win95.utilities.filehandling.FileValidity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileDetail {
    /*
    *   Add system call to find details for particular file or directory
    *   not yet implemented...
    *   use utilities.fileHandling.os module to add logic
     */


    private final String filePath,fileName;
    private String sizeInByte,sizeInKiloByte,sizeInGigaByte,sizeInMegaByte,optimizedSize;
    private String lastAccessTime,lastModifiedTime,creationTime;
    private final File file;
    private FileType fileType;
    private String fileExtension = "unknown";


    private int numberOfFiles = 1;

    public FileDetail(File file) throws IOException {
        this.file = file;
        filePath = this.file.getPath();
        fileName = this.file.getName();
        if(FileValidity.isValid(file)){

            Path OsPath = this.file.toPath();
            BasicFileAttributes attributes = Files.readAttributes(OsPath,BasicFileAttributes.class);
            this.creationTime = attributes.creationTime().toString();
            this.lastAccessTime = attributes.lastAccessTime().toString();
            this.lastModifiedTime = attributes.lastModifiedTime().toString();

            creationTime = creationTime.substring(0,creationTime.length()-1);
            String []splitCreationTime = creationTime.split("T");
            creationTime = "";
            for(String var : splitCreationTime) creationTime += var + " ";

            lastAccessTime = lastAccessTime.substring(0,lastAccessTime.length()-1);
            String []splitLastAccessTime = lastAccessTime.split("T");
            lastAccessTime = "";
            for(String var : splitLastAccessTime) lastAccessTime += var + " ";

            lastModifiedTime = lastModifiedTime.substring(0,lastModifiedTime.length()-1);
            String []splitModifiedAccessTime = lastAccessTime.split("T");
            lastModifiedTime = "";
            for(String var : splitModifiedAccessTime) lastModifiedTime += var + " ";


            long bytes = attributes.size();
            long kilobytes = (bytes / 1024);
            long megabytes = (kilobytes / 1024);
            long gigabytes = (megabytes / 1024);

            this.sizeInByte = String.format("%,d bytes", bytes);
            this.sizeInKiloByte = String.format("%,d kb", kilobytes);
            this.sizeInMegaByte = String.format("%,d mb", megabytes);
            this.sizeInGigaByte = String.format("%,d gb", gigabytes);
            if(gigabytes != 0) optimizedSize = sizeInGigaByte;
            else if(megabytes != 0) optimizedSize = sizeInMegaByte;
            else if(kilobytes !=0 ) optimizedSize = sizeInKiloByte;
            else if(bytes !=0 ) optimizedSize = sizeInByte;

            int lastIndexOf = fileName.lastIndexOf(".");
            if (lastIndexOf == -1) {
                fileExtension = "unknown";
            }else{
                fileExtension = fileName.substring(lastIndexOf+1);
            }
            if(attributes.isDirectory()){
                fileType = FileType.DIRECTORY;
                fileExtension = "directory";
                /*
                * replace it with system call in future for fast working;
                * numberOfFiles = (this.file.list()).length;
                 */

            }else {
                fileType = FileType.FILE;
            }
        }

    }
    public String getLastAccessTime() {
        return lastAccessTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public FileType getFileType() {
        return fileType;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public String getSizeInByte() {
        return sizeInByte;
    }

    public String getSizeInKiloByte() {
        return sizeInKiloByte;
    }

    public String getSizeInGigaByte() {
        return sizeInGigaByte;
    }

    public File getFile() {
        return file;
    }


    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }
    public String getOptimizedSize(){

        return optimizedSize;
    }
    @Override
    public String toString() {
        return  "fileName       : " + fileName + "\n" +
                "filePath       : " + filePath + "\n" +
                "sizeInByte     : " + sizeInByte + "\n" +
                "sizeInKiloByte : " + sizeInKiloByte + "\n" +
                "sizeInGigaByte : " + sizeInGigaByte + "\n" +
                "optimizedSize  : " + optimizedSize + "\n" +
                "fileType       : " + fileType + "\n" +
                "fileExtension  : " + fileExtension + "\n" +
                "numberOfFiles  : " + numberOfFiles + "\n" ;

    }

}
