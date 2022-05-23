package com.coin.discordconnection;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

public class Helper {

    public static String messageToText(Message message) {
        JDAUtils jdaUtils = DiscordConnection.instance.jdaUtils;
        StringBuilder text = new StringBuilder();

        if(!message.getAttachments().isEmpty()) {
            text.append(message.getAttachments().stream()
                    .map(attachment -> attachment.getContentType().split("/")[0])
                    .collect(Collectors.joining("][", "[", "]")));
        }
        if(!message.getEmbeds().isEmpty()) {
            text.append(message.getEmbeds().stream()
                    .map(messageEmbed -> messageEmbed.getTitle() + "\n" + messageEmbed.getDescription() + "\n" + messageEmbed.getFields().stream()
                            .map(field -> field.getName() + "\n" + field.getValue()).collect(Collectors.joining("\n"))).collect(Collectors.joining("][", "[", "]")));
        }
        if (message.getAuthor().getIdLong() == jdaUtils.jda.getSelfUser().getIdLong()) {
            if(!message.getContentRaw().isEmpty()) {
                String contentStripped = message.getContentStripped();
                String username = contentStripped.split(">")[0] + ">";
                String content = contentStripped.split(">")[1].trim();
                text = new StringBuilder(username + " " + text + content);
            }
        } else {
            text = new StringBuilder(ChatColor.BLUE + "[" + message.getAuthor().getName() + "] " + ChatColor.WHITE + text + message.getContentStripped());
        }
        return text.toString();
    }
    public static String messageToText(Message message, ChatColor color) {
        return messageToText(message).replaceAll(ChatColor.BLUE.toString(),color.toString()).replaceAll(ChatColor.WHITE.toString(), color.toString());
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
