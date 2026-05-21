package it.aulab.progetto_finale_michele_macis.utils;

public class StringManipulation {
    
    public static String getFileExstension(String nameFile){
        int dotIndex = nameFile.indexOf('.');
        String extension = nameFile.substring(dotIndex + 1);
        return extension;
    }
}
