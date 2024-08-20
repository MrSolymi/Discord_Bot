package me.solymi.commands;

import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Pause implements ICommand {

    private final String name = "pause";
    private final String description = "Pause the current song";
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

    }
}
