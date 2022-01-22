package com.coin.discordconnection;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Helper {
    public static String messageToText(Message message) {
        StringBuilder text = new StringBuilder();
        if (message.getAttachments().size() > 0) {
            for (Message.Attachment attachment : message.getAttachments()) {
                text.append("[").append(attachment.getContentType().split("/")[0]).append("] ");
            }
        }
        if (message.getEmbeds().size() > 0) {
            for (MessageEmbed embed : message.getEmbeds()) {
                text.append("[").append(embed.getTitle()).append("] ");
            }
        }
        if (message.getAuthor().getIdLong() == DiscordConnection.jda.getSelfUser().getIdLong()) {
            String contentStripped = message.getContentStripped();
            String username = contentStripped.split(">")[0] + ">";
            String content = contentStripped.split(">")[1].trim();
            text = new StringBuilder(username + " " + text + content);
        } else {
            text = new StringBuilder(ChatColor.BLUE + "[" + message.getAuthor().getName() + "] " + ChatColor.WHITE + text + message.getContentStripped());
        }
        return text.toString();
    }
    public static String messageToText(Message message, ChatColor color) {
        StringBuilder text = new StringBuilder();
        if (message.getAttachments().size() > 0) {
            for (Message.Attachment attachment : message.getAttachments()) {
                text.append("[").append(attachment.getContentType().split("/")[0]).append("] ");
            }
        }
        if (message.getEmbeds().size() > 0) {
            for (MessageEmbed embed : message.getEmbeds()) {
                text.append("[").append(embed.getTitle()).append("] ");
            }
        }
        if (message.getAuthor().getIdLong() == DiscordConnection.jda.getSelfUser().getIdLong()&&message.getEmbeds().isEmpty()) {
            String contentStripped = message.getContentStripped();
            String username = contentStripped.split(">")[0] + ">";
            String content = contentStripped.split(">")[1].trim();
            text = new StringBuilder(color + username + " " + text + content);
        } else {
            text = new StringBuilder(color + "[" + message.getAuthor().getName() + "] " + color + text + message.getContentStripped());
        }
        return text.toString();
    }
    public static String getPublicIp() {
        URL whatismyip = null;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ip = null; //you get the IP as a String
        try {
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
