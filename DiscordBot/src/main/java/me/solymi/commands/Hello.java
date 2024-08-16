package me.solymi.commands;

import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Hello implements ICommand {
    private final String name = "hello";
    private final String description = "Hello World!";
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
        event.reply("Hello World!").queue();
    }
}
