package me.solymi.commands;


import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;

public class Join implements ICommand {
    private final String name = "join";
    private final String description = "Join a voice channel";
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<OptionData> getOptions() {
        return ICommand.super.getOptions();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildVoiceState memberVoiceState = Objects.requireNonNull(event.getMember()).getVoiceState();

        if (memberVoiceState == null) {
            event.reply("Not in a voice channel").queue();
            return;
        }


        if (memberVoiceState.inAudioChannel()) {
            event.getJDA().getDirectAudioController().connect(memberVoiceState.getChannel());
            event.reply("Joined voice channel").queue();
        } else {
            event.reply("Not in a voice channel").queue();
        }
    }
}
