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

package com.epicnicity322.epicpluginlib.bukkit.logger;

import com.epicnicity322.epicpluginlib.core.logger.ConsoleLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class Logger implements ConsoleLogger<CommandSender>
{
    private static final @NotNull Pattern formatCodes = Pattern.compile("&[a-fk-o0-9r]");
    private final @NotNull String prefix;
    private final @NotNull java.util.logging.Logger logger;

    /**
     * Creates a logger to log colored messages to console.
     *
     * @param prefix The string that should be in the start of every message.
     * @param logger The logger to use on leveled messages, null to use bukkit's default logger.
     */
    public Logger(@NotNull String prefix, @Nullable java.util.logging.Logger logger)
    {
        this.prefix = prefix;

        if (logger == null)
            this.logger = Bukkit.getLogger();
        else
            this.logger = logger;
    }

    @Override
    public @NotNull String getPrefix()
    {
        return prefix;
    }

    public void log(@NotNull String message)
    {
        log(Bukkit.getConsoleSender(), message);
    }

    public void log(@NotNull String message, @NotNull Level level)
    {
        message = formatCodes.matcher(message).replaceAll("");

        switch (level) {
            case ERROR:
                logger.severe(message);
                break;
            case WARN:
                logger.warning(message);
                break;
            case INFO:
                logger.info(message);
                break;
        }
    }

    public void log(@NotNull CommandSender sender, @NotNull String message)
    {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }
}
