package me.solymi.commands;

import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.Color;

public class Help implements ICommand {
    private final String name = "help";
    private final String description = "Get help";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        var prefix = "/";
        eb.setColor(Color.BLUE);
        eb.addField("All Bot Commands", "Here are all the commands you can use with this bot", false);
        eb.addField("", "Music COMMANDS", false);
        eb.addField(prefix + "play <query>", "Plays a track instantly", false);

        eb.addField("", "Bot COMMANDS", false);
        eb.addField(prefix + "help", "Shows this message", false);
        eb.addField(prefix + "hello", "Says hello", false);
        eb.addField(prefix + "join", "Joins a voice channel", false);
        eb.addField(prefix + "leave", "Leaves the voice channel", false);

//        eb.addField(prefix + "add <query>", "Adds a track to Queue", true);
//        eb.addField(prefix + "pause", "Pauses the player", true);
//        eb.addField(prefix + "resume", "Resumes after pause", true);
//        eb.addField(prefix + "volume <value>", "Sets the audio volume", true);
//        eb.addField(prefix + "clear", "Clears the queue", true);
//        eb.addField(prefix + "eq <preset>", "Sets the equalizer preset", true);
//        eb.addField("", "Misc COMMANDS", false);
//        eb.addField(prefix + "inspiro", "Generates a random inspirobot image", true);
//        eb.addField(prefix + "cat", "Generates a random cat image", true);
//        eb.addField("", "Complete command refrence:", false);
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
