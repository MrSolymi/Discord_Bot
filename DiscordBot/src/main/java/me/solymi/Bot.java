package me.solymi;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.LavalinkNode;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.event.EmittedEvent;
import dev.arbjerg.lavalink.client.event.StatsEvent;
import dev.arbjerg.lavalink.client.event.TrackStartEvent;
import dev.arbjerg.lavalink.client.loadbalancing.RegionGroup;
import dev.arbjerg.lavalink.client.loadbalancing.builtin.VoiceRegionPenaltyProvider;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bot {
    private static final Logger LOG = LoggerFactory.getLogger(Bot.class);
    private final LavalinkClient client;
    private final Listener listener;

    public Bot() throws Exception {
        final var TOKEN = loadTokenFromEnvFile();

        client = new LavalinkClient(Helpers.getUserIdFromToken(TOKEN));

        client.getLoadBalancer().addPenaltyProvider(new VoiceRegionPenaltyProvider());

        registerLavalinkListeners();
        registerLavalinkNodes();

        listener = new Listener(client);

        YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager(true);

        final var builder = JDABuilder.createDefault(TOKEN);
        builder.setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(client));
        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES); // GatewayIntent.MESSAGE_CONTENT
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.addEventListeners(listener);
        builder.build().awaitReady();

//        builder.build().updateCommands().queue();
//
//        try {
//            Thread.sleep(3000); // 3 seconds
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        builder.build().updateCommands().addCommands(
//                Commands.slash("ping", "Pong!"),
//                Commands.slash("hello", "Hello!"),
//                Commands.slash("join", "Join voice channel"),
//                Commands.slash("leave", "Leave voice channel"),
//                Commands.slash("play", "Play a song")
//        ).queue();
//
//        System.out.println("Bot is ready!");
    }

    private void registerLavalinkNodes() {
        List.of(
            /*client.addNode(
                "Testnode",
                URI.create("ws://localhost:2333"),
                "youshallnotpass",
                RegionGroup.EUROPE
            ),*/

                client.addNode(new NodeOptions.Builder().setName("Node")
                        .setServerUri(URI.create("ws://192.168.0.94:2333"))
                        .setPassword("youshallnotpass")
                        .setRegionFilter(RegionGroup.EUROPE)
                        .setHttpTimeout(5000L)
                        .build()
                )
        ).forEach((node) -> {
            node.on(TrackStartEvent.class).subscribe((event) -> {
                final LavalinkNode node1 = event.getNode();

                System.out.printf(
                        "%s: track started: %s%n",
                        node1.getName(),
                        event.getTrack().getInfo()
                );
            });
        });
    }

    private void registerLavalinkListeners() {
        client.on(dev.arbjerg.lavalink.client.event.ReadyEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            LOG.info(
                    "Node '{}' is ready, session id is '{}'",
                    node.getName(),
                    event.getSessionId()
            );
        });

        client.on(StatsEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            LOG.info(
                    "Node '{}' has stats, current players: {}/{} (link count {})",
                    node.getName(),
                    event.getPlayingPlayers(),
                    event.getPlayers(),
                    client.getLinks().size()
            );
        });

        client.on(EmittedEvent.class).subscribe((event) -> {
            if (event instanceof TrackStartEvent) {
                LOG.info("Is a track start event!");
            }

            final var node = event.getNode();

            LOG.info(
                    "Node '{}' emitted event: {}",
                    node.getName(),
                    event
            );
        });

//        client.on(TrackStartEvent.class).subscribe((event) -> {
//            Optional.ofNullable(listener.musicManagers.get(event.getGuildId())).ifPresent(
//                    (mng) -> mng.scheduler.onTrackStart(event.getTrack())
//            );
//        });
//
//        client.on(TrackEndEvent.class).subscribe((event) -> {
//            Optional.ofNullable(listener.musicManagers.get(event.getGuildId())).ifPresent(
//                    (mng) -> mng.scheduler.onTrackEnd(event.getTrack(), event.getEndReason())
//            );
//        });
    }

    public static String loadTokenFromEnvFile() throws Exception {
        try {
            var lines = Files.readAllLines(Path.of("token.env"));

            return lines.getFirst().split("=")[1];
        } catch (FileNotFoundException e) {
            System.out.println("token.env file not found!");
            throw e;
        }
    }
}
