/*
 * Copyright (c) 2020 Christiano Rangel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.epicnicity322.epicpluginlib.core.logger;

import com.epicnicity322.epicpluginlib.core.util.PathUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErrorLogger
{
    private final static @NotNull DateTimeFormatter fileNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
    private final static @NotNull DateTimeFormatter logFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
    private final int authorsSize;
    private final @NotNull Path errorFolder;
    private final @NotNull String authors;
    private final @NotNull String pluginName;
    private final @NotNull String pluginVersion;
    private final @Nullable String website;
    private final @Nullable Logger logger;

    public ErrorLogger(@NotNull Path errorFolder, @NotNull String pluginName, @NotNull String pluginVersion,
                       @NotNull Collection<String> authors, @Nullable String website, @Nullable Logger logger)
    {
        if (!Files.isDirectory(errorFolder))
            throw new IllegalArgumentException("errorFolder parameter is not a valid directory.");

        if (authors.isEmpty())
            throw new IllegalArgumentException("Empty collection of authors.");

        this.errorFolder = errorFolder;
        this.authorsSize = authors.size();
        this.authors = authors.toString();
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.website = website;
        this.logger = logger;
    }

    public ErrorLogger(@NotNull Path errorFolder, @NotNull String pluginName, @NotNull String pluginVersion,
                       @NotNull Collection<String> authors, @Nullable String website)
    {
        this(errorFolder, pluginName, pluginVersion, authors, website, null);
    }

    public ErrorLogger(@NotNull Path errorFolder, @NotNull String pluginName, @NotNull String pluginVersion,
                       @NotNull Collection<String> authors)
    {
        this(errorFolder, pluginName, pluginVersion, authors, null, null);
    }

    private static String stackTraceToString(Exception exception)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        exception.printStackTrace(pw);
        return sw.toString();
    }

    public void report(@NotNull Exception exception, @NotNull String title)
    {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            Path error = PathUtils.getUniquePath(errorFolder.resolve(localDateTime.format(fileNameFormatter) + ".LOG"));

            PathUtils.write("=====================================================================" +
                    "\n>> Please report this file to " + authors +
                    (website == null ? "" : "\n>> " + website) +
                    "\n=====================================================================" +
                    "\n" +
                    "\n - " + localDateTime.format(logFormatter) +
                    "\n - " + pluginName + " v" + pluginVersion +
                    "\n" +
                    "\n" + title +
                    "\n" + stackTraceToString(exception), error);

            if (logger != null)
                logger.log(Level.WARNING, "New log at " + errorFolder.getFileName().toString() + " folder.");
        } catch (Exception e) {
            System.out.println("\nSomething went wrong while reporting an error of \"" + pluginName + "\" plugin.");
            System.out.println("Please contact the developer" + (authorsSize > 1 ? "s" : "") + ": " + authors + "\n");
            System.out.println("Error that was being reported:\n");
            exception.printStackTrace();
            System.out.println("\nError that occurred while reporting:\n");
            e.printStackTrace();
            System.out.println("\nPlease read the messages above these errors and report them.\n");
        }
    }
}
