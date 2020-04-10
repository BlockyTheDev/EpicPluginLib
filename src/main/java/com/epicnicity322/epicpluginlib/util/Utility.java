package com.epicnicity322.epicpluginlib.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class Utility
{
    public static boolean isInteger(Object value)
    {
        try {
            Integer.parseInt(value.toString());
        } catch (NumberFormatException ignored) {
            return false;
        }

        return true;
    }

    public static void stringToFile(String data, Path destination) throws IOException
    {
        Files.write(destination, data.replaceAll("\n", System.getProperty("line.separator")).getBytes(),
                StandardOpenOption.CREATE_NEW);
    }

    public static int matchZeros(int maxLength, String toMatch)
    {
        StringBuilder toMatchBuilder = new StringBuilder(toMatch);

        while (toMatchBuilder.length() < maxLength) {
            toMatchBuilder.append("0");
        }

        return Integer.parseInt(toMatchBuilder.toString());
    }

    /**
     * Checks if the file in the end of the path already exists. If so, then this will rename the path by adding
     * "(1)" (Or a greater number depending on how many duplicates are in the parent folder) to the end of the file name.
     *
     * @param path The path to the desired file.
     * @return The same path or a renamed one depending if the file in the end already exists.
     */
    public static Path getUniquePath(Path path)
    {
        String fileName = path.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        Path parentPath = path.getParent();

        long l = 2;

        while (Files.exists(path)) {
            fileName = path.getFileName().toString();

            String fileNameOnly = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")).trim() :
                    fileName.trim();
            String parenthesisPrefix = "";

            // If a duplicate does not have a space before the parenthesis, then this space will be added by this.
            if (fileNameOnly.contains(" ") && fileNameOnly.charAt(fileNameOnly.lastIndexOf(" ") + 1) != '(') {
                parenthesisPrefix = " ";
            }

            if (isADuplicate(fileNameOnly)) {
                l = Long.parseLong(fileNameOnly.substring(fileNameOnly.lastIndexOf("(") + 1).replace(")",
                        ""));
                path = parentPath.resolve(fileNameOnly.substring(0, fileNameOnly.lastIndexOf("(")) +
                        parenthesisPrefix + "(" + (l + 1) + ")" + extension);
            } else {
                path = parentPath.resolve(fileNameOnly + " (1)" + extension);
            }

            ++l;
        }

        return path;
    }

    /**
     * This will check if the file name has a duplicate suffix.
     *
     * @param fileName The name of the desired file.
     * @return true if the string has a duplicate suffix.
     */
    private static boolean isADuplicate(String fileName)
    {
        fileName = fileName.trim();

        if (fileName.endsWith(")")) {
            //The minimum length that a duplicated file name can have is 3, because that name could only look like something like "(2)".
            if (fileName.length() >= 3) {
                return isInteger(fileName.charAt(fileName.length() - 2));
            }
        }

        return false;
    }
}
