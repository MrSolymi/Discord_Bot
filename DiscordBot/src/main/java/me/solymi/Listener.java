package me.solymi;


import dev.arbjerg.lavalink.client.FunctionalLoadResultHandler;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.Track;
import me.solymi.music.AudioLoader;
import me.solymi.music.MusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Listener extends ListenerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

    public final Map<Long, MusicManager> musicManagers = new HashMap<>();
    private final LavalinkClient client;

    public Listener(LavalinkClient client) {
        this.client = client;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOG.info("{} is ready!", event.getJDA().getSelfUser().getAsTag());

        event.getJDA().updateCommands().addCommands(
                Commands.slash("ping", "Pong!"),
                Commands.slash("hello", "Hello!"),
                Commands.slash("node", "What node am I on?"),
                Commands.slash("join", "Join voice channel"),
                Commands.slash("leave", "Leave voice channel"),
                Commands.slash("play", "Play a song")
                        .addOption(
                        OptionType.STRING,
                        "identifier",
                        "The identifier of the song you want to play",
                        true
                )
        ).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getFullCommandName()) {
            case "ping" -> event.reply("Pong!").queue();
            case "hello" -> event.reply("Hello!").queue();
            case "node" -> nodeCommand(event);
            case "join" -> joinCommand(event);
            case "leave" -> leaveCommand(event);
            case "play" -> playCommand(event);
            default -> event.reply("Unknown command").queue();
        }
    }

    private void nodeCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        if (guild == null) return;

        var link = this.client.getLinkIfCached(guild.getIdLong());

        if (link == null) {
            event.reply("No link for this guild").queue();
            return;
        }

        event.reply("Connected to: " + link.getNode().getName()).queue();
    }

    private void joinCommand(SlashCommandInteractionEvent event) {
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

    private void leaveCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

//        if (guild == null) {
//            event.reply("Not in a voice channel").queue();
//            return;
//        }

        // leave voice channel
        event.getJDA().getDirectAudioController().disconnect(guild);
        event.reply("Left voice channel").queue();


    }

    private void playCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();

        if (guild == null) return;

        if (guild.getSelfMember().getVoiceState().inAudioChannel()) {
            event.deferReply(false).queue();
        } else {
            joinCommand(event);
        }

        final String identifier = event.getOption("identifier").getAsString();
        final long guildId = guild.getIdLong();
        final Link link = this.client.getOrCreateLink(guildId);
        final var musicManager = this.getOrCreateMusicManager(guildId);

        link.loadItem(identifier).subscribe(new AudioLoader(event, musicManager));
    }

    private MusicManager getOrCreateMusicManager(long guildId) {
        synchronized(this) {
            var musicManager = this.musicManagers.get(guildId);

            if (musicManager == null) {
                musicManager = new MusicManager(guildId, this.client);
                this.musicManagers.put(guildId, musicManager);
            }

            return musicManager;
        }
    }
}
