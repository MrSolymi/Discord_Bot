package me.solymi.commands;

import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Leave implements ICommand {
    private final String name = "leave";
    private final String description = "Leave a voice channel";
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
        Guild guild = event.getGuild();

        assert guild != null;
        event.getJDA().getDirectAudioController().disconnect(guild);
        event.reply("Left voice channel").queue();
    }
}
