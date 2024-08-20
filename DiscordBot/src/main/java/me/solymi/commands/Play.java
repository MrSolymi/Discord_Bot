package me.solymi.commands;

import me.solymi.interfaces.ICommand;
import me.solymi.music.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class Play implements ICommand {
    private static final Logger LOG = LoggerFactory.getLogger(Play.class);
    private final String name = "play";
    private final String description = "Play a song";

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
        return List.of(
                new OptionData(OptionType.STRING, "query", "The url or query you want to play").setRequired(true),
                new OptionData(OptionType.BOOLEAN, "priority", "If the song should be played next")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()) {
            event.reply("You need to be in a voice channel to use this command").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                event.reply("You need to be in the same voice channel as me to use this command").queue();
                return;
            }
        }

        String query = event.getOption("query").getAsString();

        boolean priority = false;
        try {
            priority = event.getOption("priority").getAsBoolean();
        } catch (NullPointerException ignored) {}

        try {
            new URI(query);
        } catch (URISyntaxException e) {
            query = "ytsearch:" + query;
        }

        PlayerManager playerManager = PlayerManager.get();
        playerManager.play(event.getGuild(), query, event, priority);
    }
}
