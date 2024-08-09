package me.solymi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        loadTokenFromEnvFile();
        String TOKEN = System.getProperty("BOT_TOKEN");
        //System.out.println(TOKEN);
        JDABuilder builder = JDABuilder.createDefault(TOKEN,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.SCHEDULED_EVENTS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                GatewayIntent.MESSAGE_CONTENT);

        builder.addEventListeners(new MyListener());

        builder.build();
    }
    public static void loadTokenFromEnvFile() throws Exception {
        try {
            File envFile = new File("token.env");
            Scanner scanner = new Scanner(envFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] keyValue = line.split("=");

                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    System.setProperty(key, value);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("token.env file not found!");
        }
    }
}