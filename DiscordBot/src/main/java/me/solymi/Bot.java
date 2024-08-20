package me.solymi;

import me.solymi.commands.*;
import me.solymi.io.Config;
import me.solymi.listeners.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Bot {
    private static final Logger LOG = LoggerFactory.getLogger(Bot.class);
    private final Config config;

    public Bot() {

        config = new Config();

        JDA jda = JDABuilder.createDefault(config.getToken()).build();

        CommandListener commandListener;

        commandListener = new CommandListener();
        commandListener.addCommand(new Hello());
        commandListener.addCommand(new Join());
        commandListener.addCommand(new Leave());
        commandListener.addCommand(new Help());
        commandListener.addCommand(new Play());

        jda.addEventListener(commandListener);

        //jda.awaitReady();

        LOG.info("Bot is ready!");
    }

    public Config getConfig() {
        return config;
    }
}
