package me.solymi;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import me.solymi.commands.*;
import me.solymi.io.Config;
import me.solymi.listeners.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Bot {
    private static final Logger LOG = LoggerFactory.getLogger(Bot.class);
    private final JDA jda;
    private final Config config;
    private final CommandListener commandListener;

    public Bot() throws Exception {
        config = new Config();

        final JDABuilder jdaBuilder = JDABuilder.createDefault(config.getToken());

        jdaBuilder.enableIntents(GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.MESSAGE_CONTENT); // GatewayIntent.MESSAGE_CONTENT
        jdaBuilder.enableCache(CacheFlag.VOICE_STATE);

        commandListener = new CommandListener();
        commandListener.addCommand(new Hello());
        commandListener.addCommand(new Join());
        commandListener.addCommand(new Leave());
        commandListener.addCommand(new Play());
        commandListener.addCommand(new Help());
        jdaBuilder.addEventListeners(commandListener);

        jda = jdaBuilder.build();

        jda.awaitReady();

        LOG.info("Bot is ready!");
    }

    public JDA getJda() {
        return jda;
    }

    public Config getConfig() {
        return config;
    }
}
