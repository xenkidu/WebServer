package org.example.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utils {


    public static String getFilePathFromUrlPath(String filename) {
        if (filename.isBlank()) {
            return filename;
        }
        String currentDirectory = System.getProperty("user.dir");
        return currentDirectory + "\\" + filename;
    }

    public static String fileToString(File file) {
        try {
            return Files.readString(Path.of(file.toString()));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "";
        }
    }
}
