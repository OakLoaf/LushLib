package org.lushplugins.lushlib.utils;

public class FilenameUtils {

    public static String removeExtension(String fileName) {
        return org.apache.commons.io.FilenameUtils.removeExtension(fileName);
    }
}
