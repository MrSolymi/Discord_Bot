package me.solymi.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.LavalinkPlayer;
import me.solymi.interfaces.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Play implements ICommand {
    private final String name = "play";
    private final String description = "Play a song";

    private final LavalinkClient client;


    public Play(LavalinkClient client) {
        this.client = client;
    }

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
                new OptionData(OptionType.STRING, "url_or_query", "The url or query you want to play").setRequired(true),
                new OptionData(OptionType.BOOLEAN, "priority", "If the song should be played next")
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

    }
}
